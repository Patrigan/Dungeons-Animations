package com.infamous.dungeons_animations;

import com.infamous.dungeons_animations.goals.AvoidAndApproachTargetGoal;
import com.infamous.dungeons_animations.goals.Evoker.SpellAttackGoal;
import com.infamous.dungeons_animations.goals.Evoker.SummonVexGoal;
import com.infamous.dungeons_animations.goals.GeoVindicatorAttackGoal;
import com.infamous.dungeons_animations.goals.GeoVindicatorMeleeGoal;
import com.infamous.dungeons_animations.mod.DAEntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_animations.DungeonsAnimations.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class DAEvent {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Vindicator){
            Vindicator v = (Vindicator) event.getEntity();
            v.getAttribute(Attributes.MAX_HEALTH).setBaseValue(24 + v.getRandom().nextInt(10) - v.getRandom().nextInt(6));
            v.setHealth((float) v.getAttributeValue(Attributes.MAX_HEALTH));
            ((GoalSelector)v.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 4 && pg.getGoal() instanceof MeleeAttackGoal);
            v.goalSelector.addGoal(2, new GeoVindicatorAttackGoal(v, 1.0D));
            v.goalSelector.addGoal(0, new GeoVindicatorMeleeGoal(v));
        }
        if(event.getEntity() instanceof Evoker){
            Evoker v = (Evoker) event.getEntity();
            v.getAttribute(Attributes.MAX_HEALTH).setBaseValue(24 + v.getRandom().nextInt(10) - v.getRandom().nextInt(6));
            v.setHealth((float) v.getAttributeValue(Attributes.MAX_HEALTH));
            ((GoalSelector)v.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 4);
            ((GoalSelector)v.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 5);
            ((GoalSelector)v.goalSelector).getAvailableGoals().removeIf(pg -> pg.getPriority() == 6);
            v.goalSelector.addGoal(6, new AvoidAndApproachTargetGoal(v, 0.70f, 90, 45, 7.5));
            v.goalSelector.addGoal(5, new SpellAttackGoal(v));
            v.goalSelector.addGoal(4, new SummonVexGoal(v));
        }
    }

}
