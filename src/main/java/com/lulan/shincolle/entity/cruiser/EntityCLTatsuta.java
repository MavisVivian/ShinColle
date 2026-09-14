package com.lulan.shincolle.entity.cruiser;

import com.lulan.shincolle.ai.ShipPickItemGoal;
import com.lulan.shincolle.ai.ShipRangeAttackGoal;
import com.lulan.shincolle.ai.ShipSkillAttackGoal;
import com.lulan.shincolle.entity.BasicEntityShipSmall;
import com.lulan.shincolle.entity.other.EntityProjectileBeam;
import com.lulan.shincolle.handler.ConfigHandler;
import com.lulan.shincolle.init.ModEntities;
import com.lulan.shincolle.init.ModSounds;
import com.lulan.shincolle.reference.ID;
import com.lulan.shincolle.utility.TeamHelper;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

/**
 * Light Cruiser Tatsuta entity.
 * model state: 0:cannon, 1:head, 2:weapon
 */
public class EntityCLTatsuta extends BasicEntityShipSmall {

    private Vec3 skillMotion = Vec3.ZERO;
    private int remainSkillAttacks;
    private final Set<Integer> damagedSkillTargets = new HashSet<>();

    public EntityCLTatsuta(EntityType<? extends EntityCLTatsuta> type, Level level) {
        super(type, level);
        this.setStateMinor(ID.M.ShipType, ID.ShipType.LIGHT_CRUISER);
        this.setStateMinor(ID.M.ShipClass, ID.ShipClass.CLTatsuta);
        this.setStateMinor(ID.M.DamageType, ID.ShipDmgType.CRUISER);
        this.setStateMinor(ID.M.NumState, 3);
        this.setGrudgeConsumption(ConfigHandler.consumeGrudgeShip[ID.ShipConsume.CL]);
        this.setAmmoConsumption(ConfigHandler.consumeAmmoShip[ID.ShipConsume.CL]);
        this.ModelPos = new float[]{0F, 22F, 0F, 42F};

        // set attack type
        this.StateFlag[ID.F.AtkType_AirLight] = false;
        this.StateFlag[ID.F.AtkType_AirHeavy] = false;
        this.StateFlag[ID.F.CanPickItem] = true;

        this.postInit();
    }

    /**
     * Equip type: 1=cannon+misc, 2=cannon+airplane+misc, 3=airplane+misc
     */
    public int getEquipType() {
        return 1;
    }

    @Override
    public void setAIList() {
        super.setAIList();

        // [PORT] 1.10.2 -> 1.20.1: CLTatsuta used skill attack at priority 0.
        this.goalSelector.removeAllGoals(goal -> goal instanceof ShipSkillAttackGoal);
        this.goalSelector.addGoal(0, new ShipSkillAttackGoal(this));

        // range attack
        this.goalSelector.addGoal(11, new ShipRangeAttackGoal(this));

        // pick item
        this.goalSelector.addGoal(20, new ShipPickItemGoal(this, 4.0F));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            if (this.tickCount % 128 == 0) {
                // marriage aura: night vision to owner
                java.util.UUID ownerUUID = this.getOwnerUUID();
                Player player = ownerUUID != null ? this.level().getPlayerByUUID(ownerUUID) : null;
                if (player != null && getStateFlag(ID.F.IsMarried) && getStateFlag(ID.F.UseRingEffect) &&
                        getStateMinor(ID.M.NumGrudge) > 0 &&
                        this.distanceToSqr(player) < 256.0D) {
                    int level = getStateMinor(ID.M.ShipLevel);
                    player.addEffect(new MobEffectInstance(
                            MobEffects.NIGHT_VISION,
                            100 + level, 0, false, false));
                }
            }
        }
    }

    @Override
    public boolean attackEntityWithHeavyAmmo(Entity target) {
        boolean attacked = super.attackEntityWithHeavyAmmo(target);
        if (!attacked || this.level().isClientSide()) {
            return attacked;
        }

        int phase = getStateEmotion(ID.S.Phase);
        if (phase == 0) {
            this.level().playSound(null, this.blockPosition(), ModSounds.SHIP_AP_P1.get(),
                    this.getSoundSource(), (float) ConfigHandler.volumeAttack(), 1.0F);
            setStateEmotion(ID.S.Phase, -1, true);
        } else if (phase == -1) {
            setStateEmotion(ID.S.Phase, 1, true);
            setStateTimer(ID.T.AttackTime3, 10);
            remainSkillAttacks = 2 + (int) (getLevel() * 0.015F);
        }
        return true;
    }

    @Override
    public boolean updateSkillAttack(Entity target) {
        if (target == null || !target.isAlive()
                || this.distanceToSqr(target) > getAttrs().getAttackRange() * getAttrs().getAttackRange()) {
            resetSkillAttack();
            return false;
        }

        int timer = getStateTimer(ID.T.AttackTime3);
        int phase = getStateEmotion(ID.S.Phase);
        if (timer <= 0) {
            if (phase >= 3) {
                resetSkillAttack();
                return false;
            }
            phase++;
            setStateEmotion(ID.S.Phase, phase, true);
        }

        switch (phase) {
            case 1 -> updateSkillCharge(target);
            case 2 -> updateSkillSpin(target);
            case 3 -> updateSkillFinal(target);
            default -> {
                return false;
            }
        }

        timer = getStateTimer(ID.T.AttackTime3);
        if (timer > 0) {
            setStateTimer(ID.T.AttackTime3, timer - 1);
        }
        return false;
    }

    private void updateSkillCharge(Entity target) {
        int timer = getStateTimer(ID.T.AttackTime3);
        if (timer <= 0) {
            setStateTimer(ID.T.AttackTime3, 10);
            timer = 10;
        }
        if (timer == 8) {
            skillMotion = target.position().subtract(this.position()).scale(0.14D);
            this.getLookControl().setLookAt(target, 180.0F, 180.0F);
        }
        this.setDeltaMovement(skillMotion);
    }

    private void updateSkillSpin(Entity target) {
        int timer = getStateTimer(ID.T.AttackTime3);
        if (timer <= 0) {
            skillMotion = new Vec3(0.0D, 0.3D, 0.0D);
            setStateTimer(ID.T.AttackTime3, 25);
            timer = 25;
        }
        this.setDeltaMovement(skillMotion);
        damageNearbySkillTargets();
        if ((timer & 7) == 0) {
            remainSkillAttacks--;
            damagedSkillTargets.clear();
            this.level().playSound(null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,
                    this.getSoundSource(), (float) ConfigHandler.volumeAttack(), this.getVoicePitch() * 1.1F);
            this.level().playSound(null, this.blockPosition(), ModSounds.SHIP_JET.get(),
                    this.getSoundSource(), (float) ConfigHandler.volumeAttack(), this.getVoicePitch());
            if (remainSkillAttacks <= 1) {
                setStateTimer(ID.T.AttackTime3, 0);
            }
        }
    }

    private void updateSkillFinal(Entity target) {
        int timer = getStateTimer(ID.T.AttackTime3);
        if (timer <= 0) {
            skillMotion = target.getBoundingBox().getCenter().subtract(this.getEyePosition()).normalize();
            remainSkillAttacks = 0;
            setStateTimer(ID.T.AttackTime3, 15);
            timer = 15;
        }
        this.setDeltaMovement(0.0D, 0.1D, 0.0D);
        if (timer == 6) {
            EntityProjectileBeam beam = new EntityProjectileBeam(ModEntities.PROJECTILE_BEAM.get(), this.level());
            beam.initBeam(this, skillMotion.x, skillMotion.y, skillMotion.z,
                    getAttackBaseDamage(3, target), 32.0F, 15);
            this.level().addFreshEntity(beam);
        } else if (timer == 4) {
            this.level().playSound(null, this.blockPosition(), ModSounds.SHIP_AP_ATTACK.get(),
                    this.getSoundSource(), (float) ConfigHandler.volumeAttack() * 1.1F, this.getVoicePitch() * 0.6F);
        }
    }

    private void damageNearbySkillTargets() {
        for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(4.0D, 3.0D, 4.0D),
                target -> target != this && target.isAlive())) {
            if (!damagedSkillTargets.add(target.getId()) || TeamHelper.checkSameOwner(this, target)) {
                continue;
            }
            target.hurt(this.damageSources().mobAttack(this), getAttackBaseDamage(2, target));
        }
    }

    private void resetSkillAttack() {
        setStateEmotion(ID.S.Phase, 0, true);
        setStateTimer(ID.T.AttackTime3, 0);
        remainSkillAttacks = 0;
        skillMotion = Vec3.ZERO;
        damagedSkillTargets.clear();
    }

    // night bonus: +0.15 CRI, +0.15 DODGE
    @Override
    public void calcShipAttributesAddRaw() {
        super.calcShipAttributesAddRaw();

        if (!this.level().isDay()) {
            this.shipAttrs.setAttrsBuffed(ID.Attrs.CRI,
                    this.shipAttrs.getAttrsBuffed(ID.Attrs.CRI) + 0.15F);
            this.shipAttrs.setAttrsBuffed(ID.Attrs.DODGE,
                    this.shipAttrs.getAttrsBuffed(ID.Attrs.DODGE) + 0.15F);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.isOrderedToSit()) {
            if (getStateEmotion(ID.S.Emotion) == ID.Emotion.BORED) {
                return this.getBbHeight() * 0.2F;
            } else {
                return this.getBbHeight() * 0.27F;
            }
        } else {
            return this.getBbHeight() * 0.7F;
        }
    }
}
