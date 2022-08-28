package com.infamous.dungeons_animations.entities.animation;

import com.infamous.dungeons_animations.interfaces.IGeoReplacedEntity;
import com.infamous.dungeons_animations.interfaces.ICrossbowUser;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PillagerAnimation implements IAnimatable, IGeoReplacedEntity {

    public PillagerAnimation() {
    }

    public Mob entity;

    @Override
    public Mob getMob(){
        return this.entity;
    }

    @Override
    public void setMob(Mob v){
        this.entity = v;
    }

    AnimationFactory factory = new AnimationFactory(this);

    public boolean getMELEEATTACKING() {
        return ((ICrossbowUser)this.getMob()).getMELEEATTACKING();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (((ICrossbowUser)this.getMob()).getSHOOT() && !((ICrossbowUser)this.getMob()).getSHOOTTYPE()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.attack", false));
        } else if (((ICrossbowUser)this.getMob()).getSHOOT()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.shot", false));
        } else if (((ICrossbowUser)this.getMob()).getCHANGE()) {
            event.getController().animationSpeed = 1 + ((ICrossbowUser)this.getMob()).getCHANGESPEED();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.charging", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().animationSpeed = 1.25;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.walk", true));
        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.idle", true));
        }
        return PlayState.CONTINUE;
    }

    public boolean isAggressive() {
        return (this.getMob()).isAggressive();
    }

}
