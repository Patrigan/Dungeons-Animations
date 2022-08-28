package com.infamous.dungeons_animations.entities.animation;

import com.infamous.dungeons_animations.interfaces.IEvoker;
import com.infamous.dungeons_animations.interfaces.IGeoReplacedEntity;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EvokerAnimation implements IAnimatable, IGeoReplacedEntity {

    public EvokerAnimation() {
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
        if (((IEvoker)this.getMob()).EvokerSpelling()) {
            event.getController().animationSpeed = 1;
            if (((IEvoker)this.getMob()).EvokerSpellType() == 0) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_attack_spell", false));
            }else if (((IEvoker)this.getMob()).EvokerSpellType() == 1) {
                event.getController().animationSpeed = 1.25;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_summon_spell", true));
            } else {
                event.getController().animationSpeed = 1.25;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_comber_spell", true));
            }
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().animationSpeed = 1.33333;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_walk", true));
        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_idel", true));
        }
        return PlayState.CONTINUE;
    }

    public boolean isAggressive() {
        return (this.getMob()).isAggressive();
    }

}
