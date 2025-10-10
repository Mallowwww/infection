package world.landfall.infection.api;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
import world.landfall.infection.infections.InternalInfections;
import world.landfall.infection.treatments.InternalTreatments;

@EventBusSubscriber(modid = InfectionMod.MODID)
public class InfectionRegistry {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceKey<Registry<Infection>> INFECTION_REGISTRY_KEY = ResourceKey.createRegistryKey(InfectionMod.path("infections"));
    public static final Registry<Infection> INFECTION_REGISTRY = new RegistryBuilder<Infection>(INFECTION_REGISTRY_KEY).create();
    public static final ResourceKey<Registry<InfectionStage>> STAGE_REGISTRY_KEY = ResourceKey.createRegistryKey(InfectionMod.path("stages"));
    public static final Registry<InfectionStage> STAGE_REGISTRY = new RegistryBuilder<InfectionStage>(STAGE_REGISTRY_KEY).create();
    public static final ResourceKey<Registry<Treatment>> TREATMENT_REGISTRY_KEY = ResourceKey.createRegistryKey(InfectionMod.path("treatments"));
    public static final Registry<Treatment> TREATMENT_REGISTRY = new RegistryBuilder<Treatment>(TREATMENT_REGISTRY_KEY).create();
    @SubscribeEvent
    private static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        if (!player.hasData(ModAttachments.ACTIVE_INFECTION))
            player.setData(
                    ModAttachments.ACTIVE_INFECTION,
                    InternalInfections.NONE_INFECTION.get().create()
            );
        if (!player.hasData(ModAttachments.TREATMENT))
            player.setData(ModAttachments.TREATMENT,
                    InternalTreatments.NONE_TREATMENT.get().create());
        if (!player.hasData(ModAttachments.IMMUNITY))
            player.setData(ModAttachments.IMMUNITY,
                    Immunity.NONE_IMMUNITY);
    }

    @SubscribeEvent
    private static void onPlayerPostTick(PlayerTickEvent.Post event) {
        var player = event.getEntity();
        if (player.hasData(ModAttachments.IMMUNITY)) {
            var immunity = player.getData(ModAttachments.IMMUNITY);
            if (immunity.length() >= 0 && immunity.timeExisted() >= immunity.length()) {
                player.setData(ModAttachments.IMMUNITY, Immunity.NONE_IMMUNITY);
            }
            else
                player.setData(ModAttachments.IMMUNITY, new Immunity(immunity.effectiveAgainst(), immunity.length(), immunity.timeExisted()+1));
        }
        if (!player.hasData(ModAttachments.ACTIVE_INFECTION)) return;
        var infection = player.getData(ModAttachments.ACTIVE_INFECTION);
        if (!INFECTION_REGISTRY.containsKey(infection.type)) {
            throw new IllegalStateException("Error ticking infection "+infection.type.toString()+" ! Infection is not registered.");
        }
        if (!infection.isActive()) {
            return;
        }
        if (infection.getCurrentStage().type == null) {
            LOGGER.error("Error ticking infection {} ! Current stage has no location.", infection.type.getPath());
        }
        if (infection.getType().validStages().stream().noneMatch(infection.getCurrentStage().type::equals)) {
            //LOGGER.error("Error ticking infection {} ! Current stage is not valid.", infection.location().getPath());
            infection.getType().onEnd(player, infection);
            player.setData(ModAttachments.ACTIVE_INFECTION, InternalInfections.NONE_INFECTION.get().create());
            player.setData(ModAttachments.TREATMENT, InternalTreatments.NONE_TREATMENT.get().create());
            return;
        }

        infection.tick(player);
        var ticks = player.tickCount;
        if (ticks % 20 == 0 && !infection.type.equals(ResourceLocation.parse("infections:none"))) {
            var randomSource = player.level().random;
            var level = player.level();
            var playerPos = player.position();
            for (var nearbyPlayer : level.getNearbyPlayers(TargetingConditions.DEFAULT, player, AABB.of(BoundingBox.fromCorners(new Vec3i(-3, -3, -3), new Vec3i(3, 3, 3))))) {
                var nearbyInfection = nearbyPlayer.hasData(ModAttachments.ACTIVE_INFECTION) ? nearbyPlayer.getData(ModAttachments.ACTIVE_INFECTION) : null;
                if (nearbyInfection == null || !nearbyInfection.type.equals(ResourceLocation.parse("infections:none"))) continue;
                if (nearbyPlayer.hasData(ModAttachments.IMMUNITY) && nearbyPlayer.getData(ModAttachments.IMMUNITY).effectiveAgainst().contains(infection.type))
                    return;
                var nearbyPos = nearbyPlayer.position();
                var dist = nearbyPos.subtract(playerPos).distanceTo(Vec3.ZERO);
                var infectionCoefficient = .3f;
                var chance = 1./dist * infectionCoefficient; // Closer you are, the more likely you are to become infected
                var randomDouble = randomSource.nextDouble();
                if (randomDouble * chance > .5f) {
                    var newInfection = infection.getType().createMutated(infection);
                    newInfection.activate();
                    nearbyPlayer.setData(ModAttachments.ACTIVE_INFECTION, newInfection);
                }
            }
        }


    }
    @SubscribeEvent
    private static void onRegisterRegistries(NewRegistryEvent event) {
        event.register(INFECTION_REGISTRY);
        event.register(STAGE_REGISTRY);
        event.register(TREATMENT_REGISTRY);

    }
}
