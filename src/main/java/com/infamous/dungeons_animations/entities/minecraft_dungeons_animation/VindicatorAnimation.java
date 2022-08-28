package com.infamous.dungeons_animations.entities.minecraft_dungeons_animation;

import com.infamous.dungeons_animations.interfaces.IGeoReplacedEntity;
import com.infamous.dungeons_animations.interfaces.IGeoMeleeAttack;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class VindicatorAnimation implements IAnimatable, IGeoReplacedEntity {

    public VindicatorAnimation() {
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
        return ((IGeoMeleeAttack)this.getMob()).getMELEEATTACKING();
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
        if (this.getMELEEATTACKING()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_attack_run", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            if (!this.isAggressive()) {
                event.getController().animationSpeed = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_walk", true));
            } else {
                event.getController().animationSpeed = 1.25;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator.run", true));
            }
        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vindicator_idel", true));
        }
        return PlayState.CONTINUE;
    }

    public boolean isAggressive() {
        return (this.getMob()).isAggressive();
    }

}
