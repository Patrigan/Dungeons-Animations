package com.infamous.dungeons_animations.goals;

import com.infamous.dungeons_animations.interfaces.IGeoMeleeAttack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

import java.util.EnumSet;

public class GeoVindicatorMeleeGoal extends Goal {

    private int Timer;
    private Vindicator v;


    public GeoVindicatorMeleeGoal(Vindicator e) {
        this.v = e;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean canUse() {
        return this.v.getTarget() != null && this.v instanceof IGeoMeleeAttack && ((IGeoMeleeAttack)this.v).canMELEEATTACKING();
    }

    @Override
    public boolean canContinueToUse() {
        //animation tick
        return this.Timer < 12 && (this.v.getTarget() != null && this.v.getTarget().isAlive());
    }

    @Override
    public void start() {
        this.Timer=0;
        ((IGeoMeleeAttack)this.v).setMELEEATTACKING(true);
        //v.playSound(DASounds.VINDICATOR_ATTACK.get(), v.getSoundVolume(), v.getVoicePitch());
    }

    @Override
    public void stop() {
        this.Timer=0;
        ((IGeoMeleeAttack)this.v).setMELEEATTACKING(false);
        ((IGeoMeleeAttack)this.v).setcanMELEEATTACKING(false);
        if (v.getTarget() == null) {
            v.setAggressive(false);
        }
        v.setDeltaMovement(v.getDeltaMovement().multiply(-1D, 1.0D, -1D));
    }

    @Override
    public void tick() {
        this.Timer++;
        if (this.v.getTarget() != null && this.v.getTarget().isAlive()) {
            this.v.getNavigation().moveTo(this.v.getTarget(), 1.6667);
            this.v.getLookControl().setLookAt(this.v.getTarget(), 30.0F, 30.0F);
            if (this.Timer == 9 && this.v.distanceToSqr(v.getTarget()) <= 6.0D+ this.v.getTarget().getBbWidth()) {
                float attackKnockback = (float) this.v.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                attackKnockback += (float)EnchantmentHelper.getKnockbackBonus(this.v);
                LivingEntity attackTarget = this.v.getTarget();
                double ratioX = (double) MathHelper.wrapDegrees(this.v.yRotO * ((float) Math.PI / 180F));
                double ratioZ = (double) (-MathHelper.wrapDegrees(this.v.yRotO * ((float) Math.PI / 180F)));
                double knockbackReduction = 0.5D;
                this.v.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.2f, 1);
                attackTarget.hurt(DamageSource.mobAttack(v), ((float)(
                        this.v.getAttributeValue(Attributes.ATTACK_DAMAGE) +
                                EnchantmentHelper.getDamageBonus(v.getMainHandItem(), ((LivingEntity)attackTarget).getMobType()))) * (1 + (v.getRandom().nextFloat() / 2))
                );
                attackTarget.setSecondsOnFire((int) (EnchantmentHelper.getFireAspect(v) * 5.5));
                this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
            }
        }

    }

    private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
        if(event.isCanceled()) return;
        strength = event.getStrength();
        ratioX = event.getRatioX();
        ratioZ = event.getRatioZ();
        strength = (float)((double)strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
        if (!(strength <= 0.0F)) {
            attackTarget.hasImpulse = true;
            Vec3 vector3d = attackTarget.getDeltaMovement();
            Vec3 vector3d1 = (new Vec3(ratioX, 0.0D, ratioZ)).scale((double)strength);
            attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
        }
    }
}
