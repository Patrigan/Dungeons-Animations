package com.infamous.dungeons_animations.config;

import com.infamous.dungeons_animations.DungeonsAnimations;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class DungeonsAnimationsConfig {

    public static class Client {
        public final ForgeConfigSpec.BooleanValue ENABLE_ENTITY_EYES_MOVE;
        public final ForgeConfigSpec.BooleanValue ENABLE_VINDICATOR_ANIMATION;
        public final ForgeConfigSpec.BooleanValue ENABLE_PILLAGER_ANIMATION;
        public final ForgeConfigSpec.BooleanValue ENABLE_EVOKER_ANIMATION;
        public final ForgeConfigSpec.BooleanValue ENABLE_PIGLIN_ANIMATION;
        public final ForgeConfigSpec.BooleanValue ENABLE_PIGLIN_BRUTE_ANIMATION;
        public final ForgeConfigSpec.BooleanValue ENABLE_IRON_GOLEM_ANIMATION;

        public Client(ForgeConfigSpec.Builder builder) {
            ENABLE_ENTITY_EYES_MOVE = builder
                    .translation(DungeonsAnimations.MODID + ".config.eye_moving")
                    .define("Enable entity eyes moving", true);
            ENABLE_VINDICATOR_ANIMATION = builder
                    .translation(DungeonsAnimations.MODID + ".config.vindicator_animation")
                    .define("Enable vindicator animation", true);
            ENABLE_PILLAGER_ANIMATION = builder
                    .translation(DungeonsAnimations.MODID + ".config.pillager_animation")
                    .define("Enable pillager animation", true);
            ENABLE_EVOKER_ANIMATION = builder
                    .translation(DungeonsAnimations.MODID + ".config.evoker_animation")
                    .define("Enable evoker animation", true);
            ENABLE_PIGLIN_ANIMATION = builder
                    .translation(DungeonsAnimations.MODID + ".config.piglin_animation")
                    .define("Enable piglin animation", true);
            ENABLE_PIGLIN_BRUTE_ANIMATION = builder
                    .translation(DungeonsAnimations.MODID + ".config.piglin_brute_animation")
                    .define("Enable piglin brute animation", true);
            ENABLE_IRON_GOLEM_ANIMATION = builder
                    .translation(DungeonsAnimations.MODID + ".config.iron_golem_animation")
                    .define("Enable iron golem animation", true);
        }
    }

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        Pair<Client, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair2.getRight();
        CLIENT = specPair2.getLeft();
    }
}
