package com.infamous.dungeons_animations.client;

import com.infamous.dungeons_animations.client.renderers.ReplacedIronGolemRenderer;
import com.infamous.dungeons_animations.client.renderers.illager.ModVindicatorRenderer;
import com.infamous.dungeons_animations.client.renderers.illager.ReplacedEvokerRenderer;
import com.infamous.dungeons_animations.client.renderers.illager.ReplacedPillagerRenderer;
import com.infamous.dungeons_animations.client.renderers.illager.ReplacedVindicatorRenderer;
import com.infamous.dungeons_animations.client.renderers.piglin.ReplacedPiglinRenderer;
import com.infamous.dungeons_animations.config.DungeonsAnimationsConfig;
import com.infamous.dungeons_animations.mod.DungeonsAnimationsEntityType;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_animations.DungeonsAnimations.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientsEvents {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DungeonsAnimationsEntityType.ARMORED_VINDICATOR.get(), ModVindicatorRenderer::new);

        if (DungeonsAnimationsConfig.CLIENT.ENABLE_VINDICATOR_ANIMATION.get()) event.registerEntityRenderer(EntityType.VINDICATOR, ReplacedVindicatorRenderer::new);
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_IRON_GOLEM_ANIMATION.get()) event.registerEntityRenderer(EntityType.IRON_GOLEM, ReplacedIronGolemRenderer::new);
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_PILLAGER_ANIMATION.get()) event.registerEntityRenderer(EntityType.PILLAGER, ReplacedPillagerRenderer::new);
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_EVOKER_ANIMATION.get()) event.registerEntityRenderer(EntityType.EVOKER, ReplacedEvokerRenderer::new);
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_PIGLIN_ANIMATION.get()) event.registerEntityRenderer(EntityType.PIGLIN, ReplacedPiglinRenderer::new);
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_PIGLIN_BRUTE_ANIMATION.get()) event.registerEntityRenderer(EntityType.PIGLIN_BRUTE, ReplacedPiglinRenderer::new);

    }

}
