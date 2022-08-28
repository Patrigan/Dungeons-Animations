package com.infamous.dungeons_animations.entities.animation;

import com.infamous.dungeons_animations.interfaces.ICrossbowUser;
import com.infamous.dungeons_animations.interfaces.IGeoMeleeAttack;
import com.infamous.dungeons_animations.interfaces.IGeoReplacedEntity;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class IronGolemAnimation implements IAnimatable, IGeoReplacedEntity {

    public IronGolemAnimation() {
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

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (((IGeoMeleeAttack)this.getMob()).getMELEEATTACKING()) {
            event.getController().animationSpeed = 1;
            if (((IGeoMeleeAttack)this.getMob()).getMeleeType() == 0)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.iron_golem.attack2", false));
            else
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.iron_golem.attack1", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().animationSpeed = 1.0;
            if (this.getMob().isAggressive())
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.iron_golem.walk_fast", true));
            else
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.iron_golem.walk", true));
        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.iron_golem.idle", true));
        }
        return PlayState.CONTINUE;
    }

}
