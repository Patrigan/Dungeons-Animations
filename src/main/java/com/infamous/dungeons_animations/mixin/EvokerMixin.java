package com.infamous.dungeons_animations.mixin;

import com.infamous.dungeons_animations.interfaces.IEvoker;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Evoker.class)
public class EvokerMixin extends SpellcasterIllager implements IEvoker {

    private static final EntityDataAccessor<Boolean> SPELLATTACKING = SynchedEntityData.defineId(EvokerMixin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SPELLTYPE = SynchedEntityData.defineId(EvokerMixin.class, EntityDataSerializers.INT);

    protected EvokerMixin(EntityType<? extends SpellcasterIllager> p_33724_, Level p_33725_) {
        super(p_33724_, p_33725_);
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return null;
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {

    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    @Override
    public int EvokerSpellType() {
        return this.entityData.get(SPELLTYPE);
    }

    @Override
    public boolean EvokerSpelling() {
        return this.entityData.get(SPELLATTACKING);
    }

    @Override
    public void setEvokerSpellType(int p) {
        this.entityData.set(SPELLTYPE, p);
    }

    @Override
    public void setEvokerSpelling(boolean p) {
        this.entityData.set(SPELLATTACKING, p);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELLATTACKING, false);
        this.entityData.define(SPELLTYPE, 0);
    }
}
