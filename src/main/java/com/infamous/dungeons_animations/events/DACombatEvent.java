package com.infamous.dungeons_animations.events;

import com.mojang.math.Vector3d;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

import java.util.UUID;

public class DACombatEvent {

    public static void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
        if(event.isCanceled()) return;
        strength = event.getStrength();
        ratioX = event.getRatioX();
        ratioZ = event.getRatioZ();
        strength = (float)((double)strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
        if (!(strength <= 0.0F)) {
            attackTarget.hasImpulse = true;
            Vec3 vector3d = attackTarget.getDeltaMovement();
            Vec3 vector3d1 = (new Vec3(ratioX, 0.0D, ratioZ)).normalize().scale((double)strength);
            attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
        }
    }

    public static void addNewProjectile(Projectile projectile, CompoundTag compoundNBT, Level level, float rotation) {

        Projectile newProjectile = (Projectile) projectile.getType().create(level);
        UUID uuid = newProjectile.getUUID();
        newProjectile.load(compoundNBT);
        newProjectile.setUUID(uuid);
        Vec3 vector3d = newProjectile.getDeltaMovement().yRot((float) (Math.PI / rotation));

        newProjectile.setDeltaMovement(vector3d);
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        newProjectile.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI)));
        newProjectile.setXRot((float) (Mth.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI)));
        newProjectile.yRotO = newProjectile.getYRot();
        newProjectile.xRotO = newProjectile.getXRot();
        if (newProjectile instanceof Projectile) {
            Projectile newDamagingProjectile = (Projectile) newProjectile;
            Vec3 newPower = new Vec3(newDamagingProjectile.getDeltaMovement().x, newDamagingProjectile.getDeltaMovement().y, newDamagingProjectile.getDeltaMovement().z).yRot((float) (Math.PI / rotation));

            newDamagingProjectile.setDeltaMovement(newPower);
        }


        level.addFreshEntity(newProjectile);
    }

    public static void AreaAttack(LivingEntity v, float range, float X, float Y, float Z, float arc, float damage, double DeltaY, boolean ignoreInvincibilityTick) {
        for (LivingEntity entityHit : v.level.getEntitiesOfClass(LivingEntity.class, v.getBoundingBox().inflate(X, Y, Z))) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - v.getZ(), entityHit.getX() - v.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = v.yBodyRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - v.getZ()) * (entityHit.getZ() - v.getZ()) + (entityHit.getX() - v.getX()) * (entityHit.getX() - v.getX()));
            if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                if (!v.isAlliedTo(entityHit) && !(entityHit == v)) {
                    if (ignoreInvincibilityTick) entityHit.invulnerableTime = 0;
                    entityHit.hurt(DamageSource.mobAttack(v), (float) v.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage);

                    float attackKnockback = (float) v.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                    double ratioX = (double) Mth.sin(v.getYRot() * ((float) Math.PI / 180F));
                    double ratioZ = (double) (-Mth.cos(v.getYRot() * ((float) Math.PI / 180F)));
                    double knockbackReduction = 0.35D;
                    entityHit.hurt(DamageSource.mobAttack(v), damage);
                    forceKnockback(entityHit, attackKnockback * 0.8F, ratioX, ratioZ, knockbackReduction);
                    entityHit.setDeltaMovement(entityHit.getDeltaMovement().add(0,DeltaY,0));
                }
            }
        }
    }
}
