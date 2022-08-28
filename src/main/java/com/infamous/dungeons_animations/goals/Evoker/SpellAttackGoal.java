package com.infamous.dungeons_animations.goals.Evoker;

import com.infamous.dungeons_animations.interfaces.IEvoker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumSet;

public class SpellAttackGoal extends Goal {

    private Evoker v;

    private int SpellTicks;
    private int nextAttackTickCount;

    public SpellAttackGoal(Evoker evoker) {
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

    public void start() {
        super.start();
        this.SpellTicks = 0;
        ((IEvoker)v).setEvokerSpellType(0);
        ((IEvoker)v).setEvokerSpelling(true);
        v.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
    }

    public void tick() {
        v.getLookControl().setLookAt(v.getTarget(), (float) v.getMaxHeadYRot(), (float) v.getMaxHeadXRot());
        Evoker mob = v;

        mob.getNavigation().stop();
        this.SpellTicks ++;
        if (this.SpellTicks == 14) {

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

    }

    public void stop() {
        super.stop();
        ((IEvoker)v).setEvokerSpelling(false);
        this.nextAttackTickCount = v.tickCount + 80;
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
        LivingEntity livingentity = v.getTarget();
        return livingentity != null && this.SpellTicks <= 25;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }
}
