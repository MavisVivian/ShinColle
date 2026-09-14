package com.lulan.shincolle.ai.path;

import com.lulan.shincolle.entity.IShipNavigator;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ShipMoveControl extends MoveControl {

    private final boolean fallbackCanFly;
    /** 1.10.2版: rotateLimit相当 (毎tickの最大回転角) */
    private final float rotateLimit;

    public ShipMoveControl(Mob mob, boolean canFly, float rotateLimit) {
        super(mob);
        this.fallbackCanFly = canFly;
        this.rotateLimit = rotateLimit;
    }

    @Override
    public void tick() {
        // 1.20.1 separates the movement-speed scalar from forward input.
        // Legacy setAIMoveSpeed() supplied both; without zza the navigator can
        // keep producing MOVE_TO operations while the ship remains stationary.
        mob.setZza(0F);

        if (this.operation != Operation.MOVE_TO) {
            mob.setSpeed(0F);
            return;
        }

        this.operation = Operation.WAIT;

        double dx = this.wantedX - mob.getX();
        double dy = this.wantedY - mob.getY();
        double dz = this.wantedZ - mob.getZ();
        double distSq = dx*dx + dy*dy + dz*dz;

        if (distSq < 1.0E-3D) {
            mob.setSpeed(0F);
            return;
        }

        // 向きの更新 (1.10.2版: limitAngle相当はバニラrotationYawWrapDegrees)
        float targetYaw = (float)(Mth.atan2(dz, dx) * (180D / Math.PI)) - 90F;
        mob.setYRot(rotlerp(mob.getYRot(), targetYaw, this.rotateLimit));

        // 移動速度の取得
        float moveSpeed = (float)(mob.getAttributeValue(
                net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED) * this.speedModifier);

        // y軸移動 (1.10.2版: ShipMoveHelper.onUpdateMoveHelper内のy軸処理)
        Vec3 delta = mob.getDeltaMovement();

        if (canFlyNow()) {
            // 1.10.2版: fly entityのy軸処理
            if (dy > 0.5D) {
                mob.setDeltaMovement(delta.x, delta.y + moveSpeed * 0.12D, delta.z);
                moveSpeed *= 0.8F;
            } else if (dy < -0.5D) {
                mob.setDeltaMovement(delta.x, delta.y - moveSpeed * 0.16D, delta.z);
                moveSpeed *= 0.92F;
            }
        } else if (mob.isInWater() || mob.isInLava()) {
            // 1.10.2版: 水中entityのy軸処理
            if (dy > 1D) {
                mob.setDeltaMovement(delta.x, delta.y + moveSpeed * 0.2D, delta.z);
                moveSpeed *= 0.5F;
            } else if (dy > 0.35D) {
                mob.setDeltaMovement(delta.x, delta.y + moveSpeed * 0.1D, delta.z);
                moveSpeed *= 0.5F;
            } else if (dy < -1D) {
                mob.setDeltaMovement(delta.x, delta.y - moveSpeed * 0.25D, delta.z);
                moveSpeed *= 0.82F;
            }
        }

        mob.setSpeed(moveSpeed);
        // Mob#setSpeed also copies the scalar into zza.  Ship travel applies
        // the scalar itself, so retain a normalized forward direction instead
        // of multiplying the requested speed twice.
        mob.setZza(1F);
    }

    private boolean canFlyNow() {
        return this.mob instanceof IShipNavigator navigator
                ? navigator.canFly()
                : this.fallbackCanFly;
    }

}
