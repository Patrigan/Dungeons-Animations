package com.infamous.dungeons_animations.client.renderers.geo;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModExtentedGeoReplacedEntityRenderer<T extends IAnimatable> extends ModGeoReplacedEntityRenderer<T> {
    public ModExtentedGeoReplacedEntityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<IAnimatable> modelProvider, T animatable) {
        super(renderManager, modelProvider, animatable);
    }
}
