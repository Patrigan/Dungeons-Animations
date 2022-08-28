package com.infamous.dungeons_animations;

import com.infamous.dungeons_animations.config.DungeonsAnimationsConfig;
import com.infamous.dungeons_animations.goals.AvoidAndApproachTargetGoal;
import com.infamous.dungeons_animations.goals.Evoker.SpellAttackGoal;
import com.infamous.dungeons_animations.goals.Evoker.SummonVexGoal;
import com.infamous.dungeons_animations.goals.GeoVindicatorAttackGoal;
import com.infamous.dungeons_animations.goals.GeoVindicatorMeleeGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_animations.DungeonsAnimations.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class DungeonsAnimationsEvent {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_VINDICATOR_ANIMATION.get()) {
            if (event.getEntity() instanceof Vindicator vindicator) {
                vindicator.getAttribute(Attributes.MAX_HEALTH).setBaseValue(24 + vindicator.getRandom().nextInt(10) - vindicator.getRandom().nextInt(6));
                vindicator.setHealth((float) vindicator.getAttributeValue(Attributes.MAX_HEALTH));
                ((GoalSelector) vindicator.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 4 && pg.getGoal() instanceof MeleeAttackGoal);
                vindicator.goalSelector.addGoal(2, new GeoVindicatorAttackGoal(vindicator, 1.0D));
                vindicator.goalSelector.addGoal(0, new GeoVindicatorMeleeGoal(vindicator));
            }
        }
        if (DungeonsAnimationsConfig.CLIENT.ENABLE_EVOKER_ANIMATION.get()) {
            if (event.getEntity() instanceof Evoker evoker) {
                evoker.getAttribute(Attributes.MAX_HEALTH).setBaseValue(24 + evoker.getRandom().nextInt(10) - evoker.getRandom().nextInt(6));
                evoker.setHealth((float) evoker.getAttributeValue(Attributes.MAX_HEALTH));
                ((GoalSelector) evoker.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 4);
                ((GoalSelector) evoker.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 5);
                ((GoalSelector) evoker.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 6);
                evoker.goalSelector.addGoal(6, new AvoidAndApproachTargetGoal(evoker, 0.70f, 90, 45, 7.5));
                evoker.goalSelector.addGoal(5, new SpellAttackGoal(evoker));
                evoker.goalSelector.addGoal(4, new SummonVexGoal(evoker));
            }
        }
    }

}
