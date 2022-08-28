package com.infamous.dungeons_animations.worldgen;

import com.infamous.dungeons_animations.DungeonsAnimations;
import com.infamous.dungeons_animations.entities.ArmoredVindicator;
import com.infamous.dungeons_animations.mod.DungeonsAnimationsEntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsAnimations.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DungeonsAnimationsAttributesSetUp {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(DungeonsAnimationsEntityType.ARMORED_VINDICATOR.get(), ArmoredVindicator.setCustomAttributes().build());
    }

}
