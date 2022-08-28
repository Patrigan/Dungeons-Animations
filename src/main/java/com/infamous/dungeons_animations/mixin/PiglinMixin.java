package com.infamous.dungeons_animations.mixin;

import com.infamous.dungeons_animations.config.DungeonsAnimationsConfig;
import com.infamous.dungeons_animations.interfaces.ICrossbowUser;
import com.infamous.dungeons_animations.interfaces.IPiglin;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

@Mixin(Piglin.class)
public abstract class PiglinMixin extends AbstractPiglin implements CrossbowAttackMob, IPiglin {

    public PiglinMixin(EntityType<? extends AbstractPiglin> p_34652_, Level p_34653_) {
        super(p_34652_, p_34653_);
    }
    @Shadow private boolean cannotHunt;

    @Shadow public abstract boolean isDancing();

    @Shadow protected abstract boolean isChargingCrossbow();

    @Shadow protected abstract void playSoundEvent(SoundEvent p_219196_);

    private static final EntityDataAccessor<Boolean> MELEEATTACKING = SynchedEntityData.defineId(PiglinMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CANMELEEATTACK = SynchedEntityData.defineId(PiglinMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOOTATTACKING = SynchedEntityData.defineId(PiglinMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOOTTYPE = SynchedEntityData.defineId(PiglinMixin.class, EntityDataSerializers.BOOLEAN);

    private int timer;

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(MELEEATTACKING, false);
        this.entityData.define(CANMELEEATTACK, false);
        this.entityData.define(SHOOTATTACKING, false);
        this.entityData.define(SHOOTTYPE, false);
    }

    @Inject(at = @At("HEAD"), method = "customServerAiStep")
    protected void customServerAiStep(CallbackInfo ci) {
        if (this.getSHOOT()) {
            this.timer ++;
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.getLookControl().setLookAt(this.getTarget());
                if (this.timer == 20 && !this.entityData.get(SHOOTTYPE)) {
                    this.entityData.set(SHOOTTYPE, true);
                    this.timer = 0;
                }

                if (this.timer == 1 && this.entityData.get(SHOOTTYPE)) {
                    this.rangedAttack(this.getTarget(), 0);
                    ItemStack itemstack1 = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem));
                    CrossbowItem.setCharged(itemstack1, false);
                }
                if (this.timer == 14 && this.entityData.get(SHOOTTYPE)) {
                    this.setSHOOT(false);
                }
            }else {
                this.setSHOOT(false);
            }
            return;
        }

        if (this.getMELEEATTACKING()) {
            this.timer ++;
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.getLookControl().setLookAt(this.getTarget());
                if (this.timer == 8) {
                    if (this.distanceTo(this.getTarget()) <= 10) {
                        float attackKnockback = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                        attackKnockback += (float)EnchantmentHelper.getKnockbackBonus(this);
                        LivingEntity attackTarget = this.getTarget();
                        double ratioX = (double) MathHelper.wrapDegrees(this.yRotO * ((float) Math.PI / 180F));
                        double ratioZ = (double) (-MathHelper.wrapDegrees(this.yRotO * ((float) Math.PI / 180F)));
                        double knockbackReduction = 0.5D;
                        this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.2f, 1);
                        attackTarget.hurt(DamageSource.mobAttack(this), ((float)(
                                this.getAttributeValue(Attributes.ATTACK_DAMAGE) +
                                        EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)attackTarget).getMobType()))) * (1 + (this.getRandom().nextFloat() / 2))
                        );
                        attackTarget.setSecondsOnFire((int) (EnchantmentHelper.getFireAspect(this) * 5.5));
                        this.forceKnockback(attackTarget, attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    }
                }
                if (this.timer == 20) {
                    this.setMELEEATTACKING(false);
                    this.timer = 0;
                }
            }else {
                this.setMELEEATTACKING(false);
            }
            return;
        }

        this.timer = 0;
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

    @Override
    public Boolean getMELEEATTACKING() {
        return this.entityData.get(MELEEATTACKING);
    }

    @Override
    public Boolean getSHOOT() {
        return this.entityData.get(SHOOTATTACKING);
    }

    @Override
    public boolean getSHOOTTYPE() {
        return this.entityData.get(SHOOTTYPE);
    }

    @Override
    public Boolean getCHANGE() {
        return this.isChargingCrossbow();
    }

    @Override
    public int getCHANGESPEED() {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, this);
    }

    @Override
    public void setMELEEATTACKING(boolean r) {
        this.entityData.set(MELEEATTACKING, r);
    }

    @Override
    public void setcanMELEEATTACKING(boolean r) {
        this.entityData.set(CANMELEEATTACK, r);
    }

    @Override
    public void setSHOOT(boolean r) {
        this.entityData.set(SHOOTATTACKING, r);
    }

    @Override
    public boolean canMELEEATTACKING() {
        return this.entityData.get(CANMELEEATTACK);
    }

    @Override
    protected boolean canHunt() {
        return this.cannotHunt;
    }

    @Override
    public boolean isDANCING() {
        return this.isDancing();
    }

    @Override
    public boolean isADMIRING_ITEM() {
        return isLovedItem(this.getOffhandItem());
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (this.isDancing()){
            return PiglinArmPose.DANCING;
        } else if (isLovedItem(this.getOffhandItem())) {
            return PiglinArmPose.ADMIRING_ITEM;
        } else if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
            return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
        } else if (this.isChargingCrossbow()) {
            return PiglinArmPose.CROSSBOW_CHARGE;
        } else {
            return this.isAggressive() && this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem) ? PiglinArmPose.CROSSBOW_HOLD : PiglinArmPose.DEFAULT;
        }
    }

    @Override
    public void setBaby(boolean p_21451_) {
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected void playConvertedSound() {
        this.playSoundEvent(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }

    private static boolean isLovedItem(ItemStack p_149966_) {
        return p_149966_.is(ItemTags.PIGLIN_LOVED);
    }

    @Override
    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_PIGLIN_ANIMATION.get()) {
            this.setSHOOT(true);
            this.entityData.set(SHOOTTYPE, false);
        }
    }

    public void rangedAttack(LivingEntity p_33317_, float p_33318_) {
        this.performCrossbowAttack(this, 1.6F);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_PIGLIN_ANIMATION.get()) {
            if (!this.getMELEEATTACKING()) {
                this.setMELEEATTACKING(true);
            }
            return true;
        }else {
            return super.doHurtTarget(p_21372_);
        }
    }
}