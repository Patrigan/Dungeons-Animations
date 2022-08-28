package com.infamous.dungeons_animations.entities.minecraft_dungeons_animation.piglin;

import com.infamous.dungeons_animations.interfaces.IPiglin;
import com.infamous.dungeons_animations.interfaces.IGeoReplacedEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CrossbowItem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PiglinAnimation implements IAnimatable, IGeoReplacedEntity {

    public PiglinAnimation() {
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
        return ((IPiglin)this.getMob()).getMELEEATTACKING();
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
        if (((IPiglin)this.getMob()).isDANCING()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.piglin.dancing", true));
        } else if (((IPiglin)this.getMob()).isADMIRING_ITEM()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.piglin.check", true));
        } else if (((IPiglin)this.getMob()).getSHOOT()) {
            event.getController().animationSpeed = 1;
            if (!((IPiglin) this.getMob()).getSHOOTTYPE()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.piglin.attack_ranged_charge", false));
            }else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.piglin.attack_range_shot", true));
            }
        } else if (this.getMELEEATTACKING()) {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.piglin.attack_melee", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            {
                event.getController().animationSpeed = this.getMob().getTarget() != null ? 1.35 : 0.7;
                if (this.getMob().getMainHandItem().getItem() instanceof CrossbowItem)
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.piglin.walk_ranged", true));
                else
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.piglin.walk", true));
            }
        } else {
            event.getController().animationSpeed = 1;
            if (this.getMob().getMainHandItem().getItem() instanceof CrossbowItem)
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.piglin.idle_ranged", true));
            else
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.piglin.idle", true));
        }
        return PlayState.CONTINUE;
    }

    public boolean isAggressive() {
        return (this.getMob()).isAggressive();
    }

}
