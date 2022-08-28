package com.infamous.dungeons_animations.client.models.piglin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import static com.infamous.dungeons_animations.DungeonsAnimations.MODID;

public class DAPiglinModel extends AnimatedGeoModel {
    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);


        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
    }

    @Override
    public ResourceLocation getModelResource(Object object) {
        return new ResourceLocation(MODID, "geo/piglin.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Object object) {
        return new ResourceLocation(MODID,
                object instanceof PiglinBrute ?
                "textures/geo_entity/piglin/netherite_armored_piglin.png" :
                "textures/geo_entity/piglin/piglin.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(Object animatable) {
        return new ResourceLocation(MODID, "animations/piglin.animation.json");
    }
}
