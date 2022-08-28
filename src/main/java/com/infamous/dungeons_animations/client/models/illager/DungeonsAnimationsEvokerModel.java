package com.infamous.dungeons_animations.client.models.illager;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import static com.infamous.dungeons_animations.DungeonsAnimations.MODID;

public class DungeonsAnimationsEvokerModel extends AnimatedGeoModel {
    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone leftEye = this.getAnimationProcessor().getBone("lefteye");
        IBone rightEye = this.getAnimationProcessor().getBone("righteye");
        IBone eyeBrow = this.getAnimationProcessor().getBone("head3");
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);


        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {

            rightEye.setPositionX((float) Math.max(Math.min((extraData.netHeadYaw / 80) + Math.sin(leftEye.getPositionX() * Math.PI / 180F), 1), 0.025));
            leftEye.setPositionX((float) Math.min(Math.max((extraData.netHeadYaw / 80) + Math.sin(rightEye.getPositionX() * Math.PI / 180F), -1), -0.025));

            rightEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 1F), 0F));
            leftEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 1F), -0F));
            eyeBrow.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 1F), -0F));

            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
    }

    @Override
    public ResourceLocation getModelResource(Object object) {
        return new ResourceLocation(MODID, "geo/illager_geo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Object object) {
        return new ResourceLocation(MODID, "textures/geo_entity/illager/evoker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Object animatable) {
        return new ResourceLocation(MODID, "animations/evoker.animation.json");
    }
}
