package com.infamous.dungeons_animations.client;

import com.infamous.dungeons_animations.client.renderers.illager.ModVindicatorRenderer;
import com.infamous.dungeons_animations.client.renderers.illager.REvokerRenderer;
import com.infamous.dungeons_animations.client.renderers.illager.RPillagerRenderer;
import com.infamous.dungeons_animations.client.renderers.illager.RVindicatorRenderer;
import com.infamous.dungeons_animations.client.renderers.piglin.RPiglinRenderer;
import com.infamous.dungeons_animations.mod.DAEntityType;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static com.infamous.dungeons_animations.DungeonsAnimations.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientsEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        if (!FMLEnvironment.production) {
            event.registerEntityRenderer(DAEntityType.ARMORED_VINDICATOR.get(), ModVindicatorRenderer::new);

            event.registerEntityRenderer(EntityType.VINDICATOR, RVindicatorRenderer::new);
            event.registerEntityRenderer(EntityType.PILLAGER, RPillagerRenderer::new);
            event.registerEntityRenderer(EntityType.EVOKER, REvokerRenderer::new);
            event.registerEntityRenderer(EntityType.PIGLIN, RPiglinRenderer::new);
            event.registerEntityRenderer(EntityType.PIGLIN_BRUTE, RPiglinRenderer::new);
        }
    }

}
