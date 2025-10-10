package world.landfall.infection;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import world.landfall.infection.infections.InternalInfections;
import world.landfall.infection.treatments.InternalTreatments;

@Mod(InfectionMod.MODID)
public class InfectionMod {
    public static final String MODID = "infection";
    public static final Logger LOGGER = LogUtils.getLogger();
    public InfectionMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("register infections");
        InternalInfections.register(modEventBus);
        InternalTreatments.register(modEventBus);
        ModInfections.register(modEventBus);

        LOGGER.info("register attachments");
        ModAttachments.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
    public static ResourceLocation path(String p) {
        return ResourceLocation.fromNamespaceAndPath(InfectionMod.MODID,p);
    }
}
