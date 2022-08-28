package com.infamous.dungeons_animations.mixin;

import com.infamous.dungeons_animations.config.DungeonsAnimationsConfig;
import com.infamous.dungeons_animations.events.DACombatEvent;
import com.infamous.dungeons_animations.interfaces.IGeoMeleeAttack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

import java.util.UUID;

@Mixin(IronGolem.class)
public class IronGolemMixin extends AbstractGolem implements IGeoMeleeAttack {

    private static final EntityDataAccessor<Boolean> MELEEATTACKING = SynchedEntityData.defineId(IronGolemMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CANMELEEATTACK = SynchedEntityData.defineId(IronGolemMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MELEE_ATTACK_ID = SynchedEntityData.defineId(IronGolemMixin.class, EntityDataSerializers.INT);

    private int timer;

    protected IronGolemMixin(EntityType<? extends AbstractGolem> p_27508_, Level p_27509_) {
        super(p_27508_, p_27509_);
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(MELEEATTACKING, false);
        this.entityData.define(CANMELEEATTACK, false);
        this.entityData.define(MELEE_ATTACK_ID, 0);
    }

    @Override
    protected void customServerAiStep() {
        if (this.getMELEEATTACKING()) {
            this.timer ++;
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.getLookControl().setLookAt(this.getTarget());
                if (this.getMeleeType() == 1) {
                    if (this.timer >= 12 && this.timer <= 14) {
                        DACombatEvent.AreaAttack(this, 3.5f, 3.5f, 3.5f, 3.5f, 70, 1 , 0, false);
                    }
                    if (this.timer == 30) {
                        this.setMELEEATTACKING(false);
                        this.timer = 0;
                    }
                }else {
                    if (this.timer == 14) {
                        DACombatEvent.AreaAttack(this, 4.5f, 4.5f, 4.5f, 4.5f, 100, 1 , 0.333333, true);
                    }
                    if (this.timer == 24) {
                        this.setMELEEATTACKING(false);
                        this.timer = 0;
                    }
                }
            }else {
                this.setMELEEATTACKING(false);
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
    public int getMeleeType() {
        return this.entityData.get(MELEE_ATTACK_ID);
    }

    public void setMeleeType(int r) {
        this.entityData.set(MELEE_ATTACK_ID, r);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_PIGLIN_ANIMATION.get()) {
            if (!this.getMELEEATTACKING()) {
                this.setMeleeType(this.getMeleeType() == 0 ? 1 : 0);
                this.setMELEEATTACKING(true);
            }
            return true;
        }else {
            return super.doHurtTarget(p_21372_);
        }
    }

}
