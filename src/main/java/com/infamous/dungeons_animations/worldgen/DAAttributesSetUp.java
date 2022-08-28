package com.infamous.dungeons_animations.worldgen;

import com.infamous.dungeons_animations.DungeonsAnimations;
import com.infamous.dungeons_animations.entities.minecraft_dungeons_animation.ArmoredVindicator;
import com.infamous.dungeons_animations.mod.DAEntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod.EventBusSubscriber(modid = DungeonsAnimations.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DAAttributesSetUp {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        if (!FMLEnvironment.production) {
            event.put(DAEntityType.ARMORED_VINDICATOR.get(), ArmoredVindicator.setCustomAttributes().build());
        }
    }

}
