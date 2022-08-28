package com.infamous.dungeons_animations.entities;

import com.infamous.dungeons_animations.interfaces.IGeoMeleeAttack;
import com.infamous.dungeons_animations.mod.DungeonsAnimationsEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ArmoredVindicator extends Vindicator implements IAnimatable, IGeoMeleeAttack {

    AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<Boolean> MELEEATTACKING = SynchedEntityData.defineId(ArmoredVindicator.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CANMELEEATTACK = SynchedEntityData.defineId(ArmoredVindicator.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DIAMOND = SynchedEntityData.defineId(ArmoredVindicator.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_GOLD = SynchedEntityData.defineId(ArmoredVindicator.class, EntityDataSerializers.BOOLEAN);

    public ArmoredVindicator(EntityType<? extends Vindicator> p_34074_, Level p_34075_) {
        super(p_34074_, p_34075_);
    }

    public ArmoredVindicator(Level p_34075_) {
        super(DungeonsAnimationsEntityType.ARMORED_VINDICATOR.get(), p_34075_);
    }

    public static AttributeSupplier.Builder setCustomAttributes(){
        return Vindicator.createAttributes()
                .add(Attributes.MAX_HEALTH, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.25D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_DIAMOND, false);
        this.entityData.define(IS_GOLD, false);
        this.entityData.define(MELEEATTACKING, false);
        this.entityData.define(CANMELEEATTACK, false);
    }

    public boolean isDiamond(){
        return this.entityData.get(IS_DIAMOND);
    }

    public boolean isGold(){
        return this.entityData.get(IS_GOLD);
    }

    public void setDiamond(boolean isDiamond){
        this.entityData.set(IS_DIAMOND, isDiamond);
    }

    public void setGold(boolean IsGold){
        this.entityData.set(IS_GOLD, IsGold);
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
    public boolean canBeLeader() {
        return false;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @Nullable SpawnGroupData p_34091_, @Nullable CompoundTag p_34092_) {

        float diamondChance = random.nextFloat();
        float goldChance = random.nextFloat();

        if (this.getCurrentRaid() == null) {
            if (diamondChance < 0.17F) {
                this.setDiamond(true);
                this.applyDiamondArmorBoosts();
            }else if (goldChance < 0.375F) {
                this.setGold(true);
                this.applyGoldArmorBoosts();
            }
        }else {
            if (diamondChance < 0.25F) {
                this.setDiamond(true);
                this.applyDiamondArmorBoosts();
            }else if (goldChance < 0.5F) {
                this.setGold(true);
                this.applyGoldArmorBoosts();
            }
        }


        return super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
    }

    private void applyDiamondArmorBoosts() {
        this.addEffect(new MobEffectInstance(MobEffects.HEAL, 10, 6, (false), (false)));
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Diamond health boost", 16.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("Diamond armor boost", 10.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Diamond knockback resistance boost", 1.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Diamond attack boost", 2.0D, AttributeModifier.Operation.ADDITION));
    }

    private void applyGoldArmorBoosts() {
        this.addEffect(new MobEffectInstance(MobEffects.HEAL, 10, 6, (false), (false)));
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Gold health boost", 8.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("Gold armor boost", 6.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Gold knockback resistance boost", 0.5D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Gold attack boost", 1.0D, AttributeModifier.Operation.ADDITION));
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource p_219149_, DifficultyInstance p_219150_) {
        ItemStack mainhandWeapon;
        if(ModList.get().isLoaded("dungeons_gear")){
            Item DOUBLE_AXE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "double_axe"));
            Item WHIRLWIND = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "whirlwind"));

            ItemStack doubleAxe = new ItemStack(DOUBLE_AXE);
            ItemStack whirlwind = new ItemStack(WHIRLWIND);
            ItemStack ironAxe = new ItemStack(Items.IRON_AXE);

            mainhandWeapon = this.isDiamond() ? whirlwind : this.isGold() ? doubleAxe : ironAxe;
        }
        else{
            mainhandWeapon = this.isDiamond() ? new ItemStack(Items.DIAMOND_AXE) : new ItemStack(Items.IRON_AXE);
        }

        this.setItemSlot(EquipmentSlot.MAINHAND, mainhandWeapon);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.getMELEEATTACKING()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_attack_run", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (!this.isAggressive()) {
                event.getController().animationSpeed = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_walk", true));
            } else {
                event.getController().animationSpeed = 1.25;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator.run", true));
            }
        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        this.setcanMELEEATTACKING(true);
        return true;
    }
}
