package world.landfall.infection.api;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.slf4j.Logger;
import world.landfall.infection.InfectionMod;
import world.landfall.infection.ModAttachments;
import world.landfall.infection.ModInfections;

@EventBusSubscriber(modid = InfectionMod.MODID)
public class InfectionRegistry {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceKey<Registry<Infection>> INFECTION_REGISTRY_KEY = ResourceKey.createRegistryKey(InfectionMod.path("infections"));
    public static final Registry<Infection> INFECTION_REGISTRY = new RegistryBuilder<Infection>(INFECTION_REGISTRY_KEY).create();
    public static final ResourceKey<Registry<InfectionStage>> STAGE_REGISTRY_KEY = ResourceKey.createRegistryKey(InfectionMod.path("stages"));
    public static final Registry<InfectionStage> STAGE_REGISTRY = new RegistryBuilder<InfectionStage>(STAGE_REGISTRY_KEY).create();

    @SubscribeEvent
    private static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        if (!player.hasData(ModAttachments.ACTIVE_INFECTION))
            player.setData(
                    ModAttachments.ACTIVE_INFECTION,
                    ModInfections.NONE_INFECTION.get()
            );
    }

    @SubscribeEvent
    private static void onPlayerPostTick(PlayerTickEvent.Post event) {
        var player = event.getEntity();
        if (!player.hasData(ModAttachments.ACTIVE_INFECTION)) return;
        var infection = player.getData(ModAttachments.ACTIVE_INFECTION);
        if (!INFECTION_REGISTRY.containsKey(infection.location())) {
            throw new IllegalStateException("Error ticking infection "+infection.location().getPath()+" ! Infection is not registered.");
        }
        if (!infection.isActive()) {
            infection.activate(); // TODO: Support infections that "gestate" for a period
            return;
        }
        if (infection.validStages().stream().noneMatch(infection.getCurrentStage().location()::equals)) {
            LOGGER.error("Error ticking infection {} ! Current stage is not valid.", infection.location().getPath());
            return;
        }
        infection.tick(player);


    }
    @SubscribeEvent
    private static void onRegisterRegistries(NewRegistryEvent event) {
        event.register(INFECTION_REGISTRY);
        event.register(STAGE_REGISTRY);

    }
}
