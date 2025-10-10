package world.landfall.infection.infections;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import world.landfall.infection.InfectionMod;
import world.landfall.infection.api.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class InternalInfections {
    private static final DeferredRegister<Infection> INFECTIONS = DeferredRegister.create(InfectionRegistry.INFECTION_REGISTRY, InfectionMod.MODID);
    private static final DeferredRegister<InfectionStage> STAGES = DeferredRegister.create(InfectionRegistry.STAGE_REGISTRY, InfectionMod.MODID);

    public static final Supplier<InfectionStage> NONE_STAGE = STAGES.register("none", () -> new InfectionStage(
            InfectionMod.path("none"), InfectionMod.path("none"), InfectionMod.path("none"), 0, -1
    ) {
        @Override
        public Collection<Holder<MobEffect>> currentEffects() {
            return List.of();
        }
        @Override
        public void tick(Player player, InfectionStageInstance instance) {

        }
    });
    public static final DeferredHolder<Infection,Infection> NONE_INFECTION = INFECTIONS.register("none", () -> new Infection() {
        @Override
        public ResourceLocation location() {
            return InfectionMod.path("none");
        }

        @Override
        public ResourceLocation initialStage() {
            return ResourceLocation.parse("infection:none");
        }

        @Override
        public Collection<ResourceLocation> validStages() {
            return List.of(InfectionMod.path("none"));
        }

        @Override
        public float infectionCoefficient() {
            return 0f;
        }

        @Override
        public void onEnd(Player player, InfectionInstance infectionInstance) {

        }
    });
    public static void register(IEventBus eventBus) {
        STAGES.register(eventBus);
        INFECTIONS.register(eventBus);
    }

}
