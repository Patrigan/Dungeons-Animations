package com.infamous.dungeons_animations.mixin;

import com.google.common.collect.Maps;
import com.infamous.dungeons_animations.interfaces.IGeoMeleeAttack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(Vindicator.class)
public class VindicatorMixin extends AbstractIllager implements IGeoMeleeAttack {

    private static final EntityDataAccessor<Boolean> MELEEATTACKING = SynchedEntityData.defineId(VindicatorMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CANMELEEATTACK = SynchedEntityData.defineId(VindicatorMixin.class, EntityDataSerializers.BOOLEAN);


    protected VindicatorMixin(EntityType<? extends AbstractIllager> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MELEEATTACKING, false);
        this.entityData.define(CANMELEEATTACK, false);
    }

    @Override
    public Boolean getMELEEATTACKING() {
        return this.entityData.get(MELEEATTACKING);
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
    public boolean canMELEEATTACKING() {
        return this.entityData.get(CANMELEEATTACK);
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {
        ItemStack itemstack = new ItemStack(Items.IRON_AXE);
        Raid raid = this.getCurrentRaid();
        int i = 1;
        if (p_37844_ > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 2;
        }

        boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
        if (flag) {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemstack);
        }

        this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        this.setcanMELEEATTACKING(true);
        return true;
    }


}
