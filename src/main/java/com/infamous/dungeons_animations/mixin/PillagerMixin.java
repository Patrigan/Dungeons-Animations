package com.infamous.dungeons_animations.mixin;

import com.google.common.collect.Maps;
import com.infamous.dungeons_animations.interfaces.ICrossbowUser;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Pillager.class)
public abstract class PillagerMixin extends AbstractIllager implements ICrossbowUser, CrossbowAttackMob, InventoryCarrier {

    @Shadow public abstract boolean isChargingCrossbow();

    private static final EntityDataAccessor<Boolean> MELEEATTACKING = SynchedEntityData.defineId(PillagerMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CANMELEEATTACK = SynchedEntityData.defineId(PillagerMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOOTATTACKING = SynchedEntityData.defineId(PillagerMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOOTTYPE = SynchedEntityData.defineId(PillagerMixin.class, EntityDataSerializers.BOOLEAN);

    private int timer;

    @Override
    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {
        this.setSHOOT(true);
        this.entityData.set(SHOOTTYPE, false);
    }

    public void rangedAttack(LivingEntity p_33317_, float p_33318_) {
        this.performCrossbowAttack(this, 1.6F);
    }

    protected PillagerMixin(EntityType<? extends AbstractIllager> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(MELEEATTACKING, false);
        this.entityData.define(CANMELEEATTACK, false);
        this.entityData.define(SHOOTATTACKING, false);
        this.entityData.define(SHOOTTYPE, false);
    }



    @Override
    protected void customServerAiStep() {
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
                if (this.timer == 24 && this.entityData.get(SHOOTTYPE)) {
                    this.setSHOOT(false);
                }
            }else {
                this.setSHOOT(false);
            }
            return;
        }

        this.timer = 0;
        super.customServerAiStep();
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
    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }

    public void applyRaidBuffs(int p_33267_, boolean p_33268_) {
        Raid raid = this.getCurrentRaid();
        boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
        if (flag) {
            ItemStack itemstack = new ItemStack(Items.CROSSBOW);
            Map<Enchantment, Integer> map = Maps.newHashMap();
            if (p_33267_ > raid.getNumGroups(Difficulty.NORMAL)) {
                map.put(Enchantments.QUICK_CHARGE, 2);
            } else if (p_33267_ > raid.getNumGroups(Difficulty.EASY)) {
                map.put(Enchantments.QUICK_CHARGE, 1);
            }

            map.put(Enchantments.MULTISHOT, 1);
            EnchantmentHelper.setEnchantments(map, itemstack);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
        }

    }

}
