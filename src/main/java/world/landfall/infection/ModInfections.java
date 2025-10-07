package world.landfall.infection;

import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import world.landfall.infection.api.Infection;
import world.landfall.infection.api.InfectionRegistry;
import world.landfall.infection.api.InfectionStage;
import world.landfall.infection.infections.CommonColdInfection;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ModInfections {
    private static final DeferredRegister<Infection> INFECTIONS = DeferredRegister.create(InfectionRegistry.INFECTION_REGISTRY, InfectionMod.MODID);
    private static final DeferredRegister<InfectionStage> STAGES = DeferredRegister.create(InfectionRegistry.STAGE_REGISTRY, InfectionMod.MODID);
    public static final DeferredHolder<InfectionStage,InfectionStage> NONE_STAGE = STAGES.register("none", () -> new InfectionStage() {
        @Override
        public ResourceLocation nextStage() {
            return NONE_STAGE.getId();
        }

        @Override
        public int stageNumber() {
            return 0;
        }

        @Override
        public int lengthInTicks() {
            return 0;
        }

        @Override
        public Collection<Holder<MobEffect>> currentEffects() {
            return List.of();
        }

        @Override
        public ResourceLocation location() {
            return InfectionMod.path("none");
        }
    });
    public static final DeferredHolder<Infection,Infection> NONE_INFECTION = INFECTIONS.register("none", () -> new Infection() {
        @Override
        public ResourceLocation location() {
            return InfectionMod.path("none");
        }

        @Override
        public ResourceLocation initialStage() {
            return NONE_STAGE.getId();
        }

        @Override
        public Collection<ResourceLocation> validStages() {
            return List.of(InfectionMod.path("none"));
        }
    });
    public static final DeferredHolder<InfectionStage, InfectionStage> COMMON_COLD_INITIAL_STAGE = STAGES.register("common_cold_initial", CommonColdInfection.CommonColdStageOne::new);
    public static final DeferredHolder<Infection, Infection> COMMON_COLD_INFECTION = INFECTIONS.register("common_cold", CommonColdInfection::new);
    public static void register(IEventBus eventBus) {
        STAGES.register(eventBus);
        INFECTIONS.register(eventBus);
    }
}
