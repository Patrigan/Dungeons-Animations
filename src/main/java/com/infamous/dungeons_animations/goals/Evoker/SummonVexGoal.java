package com.infamous.dungeons_animations.goals.Evoker;

import com.infamous.dungeons_animations.interfaces.IEvoker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumSet;

public class SummonVexGoal extends Goal {

    private Evoker v;

    private int SpellTicks;
    private int nextAttackTickCount;
    private int spellType;

    public SummonVexGoal(Evoker evoker) {
        this.v = evoker;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    private void createSpellEntity(double p_32673_, double p_32674_, double p_32675_, double p_32676_, float p_32677_, int p_32678_) {
        BlockPos blockpos = new BlockPos(p_32673_, p_32676_, p_32674_);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = v.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(v.level, blockpos1, Direction.UP)) {
                if (!v.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = v.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(v.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(p_32675_) - 1);

        if (flag) {
            v.level.addFreshEntity(new EvokerFangs(v.level, p_32673_, (double)blockpos.getY() + d0, p_32674_, p_32677_, p_32678_, v));
        }

    }

    private void spellAttack() {

        Evoker mob = v;

        LivingEntity livingentity = v.getTarget();
        double d0 = Math.min(livingentity.getY(), v.getY());
        double d1 = Math.max(livingentity.getY(), v.getY()) + 1.0D;
        float f = (float) Mth.atan2(livingentity.getZ() - v.getZ(), livingentity.getX() - v.getX());
        int k;

        if (mob.getTarget() != null) {
            if (v.distanceToSqr(livingentity) < 60.0D && mob.distanceToSqr(livingentity) > 30) {
                float f2;

                for (k = 0; k < 35; ++k) {
                    f2 = f + (float)k * (float)Math.PI * 11.0F / 35.0F + 10.2566371F;
                    this.createSpellEntity(v.getX() + (double)Mth.cos(f2) * 5.5D, v.getZ() + (double)Mth.sin(f2) * 5.5D, d0, d1, f2, k / 6);
                }

            } else {
                for (k = 0; k < 24; ++k) {
                    {
                        double d2 = 1.15 * (double) (k + 1);
                        int j = (int) (k / 1.72);
                        this.createSpellEntity(v.getX() + (double) Mth.cos(f) * d2, v.getZ() + (double) Mth.sin(f) * d2, d0, d1, f, j);
                    }
                }
            }
        }

    }

    protected void performSpellCasting() {
        ServerLevel serverlevel = (ServerLevel)v.level;

        for(int i = 0; i < 3; ++i) {
            BlockPos blockpos = v.blockPosition().offset(-2 + v.getRandom().nextInt(5), 1, -2 + v.getRandom().nextInt(5));
            Vex vex = EntityType.VEX.create(v.level);
            vex.moveTo(blockpos, 0.0F, 0.0F);
            vex.finalizeSpawn(serverlevel, v.level.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
            vex.setOwner(v);
            vex.setBoundOrigin(blockpos);
            vex.setLimitedLife(20 * (30 + v.getRandom().nextInt(90)));
            serverlevel.addFreshEntityWithPassengers(vex);
        }

    }

    public void start() {
        super.start();
        this.SpellTicks = 0;
        ((IEvoker)v).setEvokerSpellType(2);
        ((IEvoker)v).setEvokerSpelling(true);
        v.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1.0F, 1.0F);
    }

    public void tick() {
        if (v.getTarget() != null) {
            v.getLookControl().setLookAt(v.getTarget(), (float) v.getMaxHeadYRot(), (float) v.getMaxHeadXRot());
        }
        Evoker mob = v;

        mob.getNavigation().stop();
        this.SpellTicks ++;
        if (this.SpellTicks == 17) {
            performSpellCasting();
        }
        if (this.SpellTicks == 22 && v.getTarget() != null) {
            spellAttack();
        }

    }

    public void stop() {
        super.stop();
        ((IEvoker)v).setEvokerSpelling(false);
        this.nextAttackTickCount = v.tickCount + 300;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = v.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            if (v.isCastingSpell()) {
                return false;
            } else {
                return v.tickCount >= this.nextAttackTickCount;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.SpellTicks <= 40;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }
}
