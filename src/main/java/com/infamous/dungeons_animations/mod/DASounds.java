package com.infamous.dungeons_animations.mod;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeons_animations.DungeonsAnimations.MODID;

public class DASounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create((ResourceLocation) ForgeRegistries.SOUND_EVENTS, MODID);

}
