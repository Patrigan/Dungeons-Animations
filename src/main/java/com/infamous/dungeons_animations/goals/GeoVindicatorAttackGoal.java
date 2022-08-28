package com.infamous.dungeons_animations.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class GeoVindicatorAttackGoal extends MeleeAttackGoal {
    private int maxAttackTimer = 30;
    private final double moveSpeed;
    private int delayCounter;
    private int sr;
    private int attackTimer;

    public GeoVindicatorAttackGoal(PathfinderMob creatureEntity, double moveSpeed) {
        super(creatureEntity, moveSpeed, true);
        this.moveSpeed = moveSpeed;
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    @Override
    public void start() {
        this.mob.setAggressive(true);
        this.delayCounter = 0;
        //this.attackTimer = 0;
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            return;
        }
        this.sr = (int) Math.max(this.sr - 1, 0);
        if (this.attackTimer <= 0) {
            this.mob.setAggressive(true);
            this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + this.mob.getRandom().nextInt(7);
                this.mob.getNavigation().moveTo(livingentity, (double) this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, this.mob.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        } else {
            if (--this.delayCounter <= 0) {
                this.mob.setAggressive(false);
                this.delayCounter = 35 + this.mob.getRandom().nextInt(10);
                this.mob.getNavigation().moveTo(
                        livingentity.getX() + (this.mob.getRandom().nextInt(3) + 6) * (this.mob.getRandom().nextBoolean() ? 2.14 : -2.14),
                        livingentity.getY(),
                        livingentity.getZ() + (this.mob.getRandom().nextInt(3) + 6) * (this.mob.getRandom().nextBoolean() ? 2.14 : -2.14),
                        (double) this.moveSpeed * 0.9
                );
                if (this.sr > 0) {
                    if (this.mob.distanceToSqr(livingentity) >= 15 + livingentity.getBbWidth()) {
                        this.attackTimer = 0;
                        if (this.mob.distanceToSqr(livingentity) <= 50 + livingentity.getBbWidth()) {
                            this.mob.doHurtTarget(livingentity);
                        }
                    }
                    this.mob.getNavigation().stop();
                    this.delayCounter = 0;
                }
            }
            if (this.mob.hurtTime > 0) {
                this.attackTimer = 0;
            }
            // this.sr = (int) Math.max(this.sr - 1, 0);
            this.attackTimer = (int) Math.max(this.attackTimer - 1, 0);
            if (this.attackTimer == 0) {
                this.delayCounter = 0;
            }
        }
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        if ((distToEnemySqr+5 <= this.getAttackReachSqr(enemy) || this.mob.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
            this.attackTimer = 50;
            this.sr = 15;
            this.delayCounter = 0;
            this.mob.doHurtTarget(enemy);
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        if (this.mob.getTarget() == null) {
            this.mob.setAggressive(false);
        }
    }

    public GeoVindicatorAttackGoal setMaxAttackTick(int max) {
        this.maxAttackTimer = max;
        return this;
    }
}
