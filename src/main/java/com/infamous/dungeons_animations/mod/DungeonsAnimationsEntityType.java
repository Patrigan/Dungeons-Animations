package com.infamous.dungeons_animations.mod;

import com.infamous.dungeons_animations.entities.ArmoredVindicator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.infamous.dungeons_animations.DungeonsAnimations.MODID;

public class DungeonsAnimationsEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<ArmoredVindicator>> ARMORED_VINDICATOR = ENTITY_TYPES.register("armored_vindicator", () ->
            EntityType.Builder.<ArmoredVindicator>of(ArmoredVindicator::new, MobCategory.MONSTER)
                    .sized(0.7F, 1.95F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity,world) -> new ArmoredVindicator(world))
                    .build(new ResourceLocation(MODID, "armored_vindicator").toString())
    );

}
