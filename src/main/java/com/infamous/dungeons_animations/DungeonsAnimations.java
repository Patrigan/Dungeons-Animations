package com.infamous.dungeons_animations;

import com.infamous.dungeons_animations.mod.DAEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DungeonsAnimations.MODID)
public class DungeonsAnimations
{
    // Directly reference a log4j logger.
    public static final String MODID = "dungeons_animations";
    public static final Logger LOGGER = LogManager.getLogger();


    public DungeonsAnimations() {

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        GeckoLib.initialize();

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DAEntityType.ENTITY_TYPES.register(modEventBus);
        //DASounds.SOUNDS.register(modEventBus);


    }

    private void processIMC(final InterModProcessEvent event) {
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
       //MinecraftForge.EVENT_BUS.register(new ClientsEvents());
    }
}
