package com.infamous.dungeons_animations.client.models;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import static com.infamous.dungeons_animations.DungeonsAnimations.MODID;

public class DungeonsAnimationsIronGolemModel extends AnimatedGeoModel {
    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");
        IBone leftEye = this.getAnimationProcessor().getBone("lefteye");
        IBone rightEye = this.getAnimationProcessor().getBone("righteye");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);


        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));

            rightEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 1F), 0F));
            leftEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 1F), -0F));
        }
    }

    @Override
    public ResourceLocation getModelResource(Object object) {
        return new ResourceLocation(MODID, "geo/iron_golem.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Object object) {
        return new ResourceLocation(MODID, "textures/geo_entity/iron_golem.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Object animatable) {
        return new ResourceLocation(MODID, "animations/iron_golem.animation.json");
    }
}
