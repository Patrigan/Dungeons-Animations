package com.infamous.dungeons_animations.client.renderers.piglin;

import com.infamous.dungeons_animations.client.models.illager.DAEvokerModel;
import com.infamous.dungeons_animations.client.models.piglin.DAPiglinModel;
import com.infamous.dungeons_animations.client.renderers.geo.ModGeoReplacedEntityRenderer;
import com.infamous.dungeons_animations.entities.minecraft_dungeons_animation.EvokerAnimation;
import com.infamous.dungeons_animations.entities.minecraft_dungeons_animation.piglin.PiglinAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class RPiglinRenderer extends ModGeoReplacedEntityRenderer<PiglinAnimation> {

    public RPiglinRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DAPiglinModel(),
                new PiglinAnimation()
        );
    }

    @Override
    protected void preRenderCallback(LivingEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale((float) 0.9375, (float) 0.9375, (float) 0.9375);
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entity, animatable, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        bufferIn = this.rtb.getBuffer(RenderType.entityTranslucent(whTexture));

        if (bone.getName().equals("leftHand")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
            stack.pushPose();
            //You'll need to play around with these to get item to render in the correct orientation
            stack.mulPose(Vector3f.XP.rotationDegrees(15));
            stack.mulPose(Vector3f.YP.rotationDegrees(0));
            stack.mulPose(Vector3f.ZP.rotationDegrees(0));
            //You'll need to play around with this to render the item in the correct spot.
            stack.translate(-0.4D, 0.6D, -0.7D);
            //Sets the scaling of the item.
            stack.scale(1.0f, 1.0f, 1.0f);
            // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
            Minecraft.getInstance().getItemRenderer().renderStatic(this.offHand, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, OverlayTexture.NO_OVERLAY, stack, this.rtb,0);
            stack.popPose();

        }

        if (bone.getName().equals("rightHand")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
            stack.pushPose();
            //You'll need to play around with these to get item to render in the correct orientation
            stack.mulPose(Vector3f.XP.rotationDegrees(-75));
            stack.mulPose(Vector3f.YP.rotationDegrees(0));
            stack.mulPose(Vector3f.ZP.rotationDegrees(0));
            //You'll need to play around with this to render the item in the correct spot.
            stack.translate(0.4D, 0.4D, 0.8D);
            //Sets the scaling of the item.
            stack.scale(1.0f, 1.0f, 1.0f);
            // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
            Minecraft.getInstance().getItemRenderer().renderStatic(this.mainHand, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, OverlayTexture.NO_OVERLAY, stack, this.rtb,0);
            stack.popPose();

        }

        if (bone.getName().equals("headWear") && !(this.helmet.getItem() instanceof ArmorItem)) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
            stack.pushPose();
            //You'll need to play around with these to get item to render in the correct orientation
            stack.mulPose(Vector3f.XP.rotationDegrees(0));
            stack.mulPose(Vector3f.YP.rotationDegrees(0));
            stack.mulPose(Vector3f.ZP.rotationDegrees(0));
            //You'll need to play around with this to render the item in the correct spot.
            stack.translate(0.0D, 1.75D, 0.0D);
            //Sets the scaling of the item.
            stack.scale(0.6F, 0.6F, 0.6F);
            // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
            Minecraft.getInstance().getItemRenderer().renderStatic(this.helmet, ItemTransforms.TransformType.HEAD, packedLightIn, packedOverlayIn, stack, this.rtb,0);
            stack.popPose();
            bufferIn = this.rtb.getBuffer(RenderType.entityTranslucent(this.whTexture));
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}