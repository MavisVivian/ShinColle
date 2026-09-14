package com.lulan.shincolle.ai;

import com.lulan.shincolle.entity.BasicEntityMount;
import com.lulan.shincolle.entity.BasicEntityShip;
import com.lulan.shincolle.reference.ID;
import com.lulan.shincolle.server.ServerDataManager;
import com.lulan.shincolle.utility.EntityHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Flee goal - activates when HP below threshold.
 * Ported from EntityAIShipFlee (setMutexBits: 7)
 */
public class ShipFleeGoal extends Goal {

    private final BasicEntityShip ship;
    private LivingEntity owner;
    private int pathfindCooldown;

    public ShipFleeGoal(BasicEntityShip ship) {
        this.ship = ship;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return canUseWithOwner(resolveOwner());
    }

    private boolean canUseWithOwner(LivingEntity owner) {
        if (owner == null || !owner.isAlive() || this.ship.getIsSitting()
                || this.ship.getIsLeashed() || this.ship.getStateMinor(ID.M.NumGrudge) <= 0)
            return false;

        float fleeHP = this.ship.getStateMinor(ID.M.FleeHP) * 0.01F;
        float hpRatio = this.ship.getHealth() / this.ship.getMaxHealth();
        double distanceSq = this.ship.distanceToSqr(owner);

        return hpRatio <= fleeHP && distanceSq > 6D && distanceSq < 3600D;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        this.owner = resolveOwner();
        this.pathfindCooldown = 0;
    }

    @Override
    public void stop() {
        this.owner = null;
        ship.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (--this.pathfindCooldown <= 0) {
            this.pathfindCooldown = 16;

            if (this.owner != null && this.owner.isAlive()) {
                if (this.owner.level() != this.ship.level()) {
                    this.stop();
                    return;
                }

                this.ship.getLookControl().setLookAt(this.owner, 10F,
                        (float) this.ship.getMaxHeadXRot());
                boolean canMove;
                if (this.ship.isPassenger() && this.ship.getVehicle() instanceof BasicEntityMount mount) {
                    canMove = mount.getNavigation().moveTo(this.owner, 1.2D);
                } else {
                    canMove = ship.getNavigation().moveTo(this.owner, 1.2D);
                }

                // Legacy behavior only teleports after a failed path request and
                // while the owner is still more than ten blocks away.
                double distanceSq = this.ship.distanceToSqr(this.owner);
                if (!canMove && distanceSq > 100D) {
                    EntityHelper.teleportShipToEntity(this.ship, this.owner, distanceSq, 0.5D);
                }
            }
        }
    }

    private LivingEntity resolveOwner() {
        LivingEntity resolvedOwner = null;
        int uid = this.ship.getPlayerUID();
        if (uid > 0 && !this.ship.level().isClientSide()) {
            ServerPlayer serverPlayer = ServerDataManager.getPlayerByUID(uid);
            if (serverPlayer != null) {
                resolvedOwner = serverPlayer;
            }
        }

        if (resolvedOwner == null) {
            resolvedOwner = this.ship.getOwner();
        }
        return resolvedOwner != null && resolvedOwner.level() == this.ship.level()
                ? resolvedOwner
                : null;
    }
}
