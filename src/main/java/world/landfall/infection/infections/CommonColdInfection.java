package world.landfall.infection.infections;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.registries.DeferredHolder;
import world.landfall.infection.InfectionMod;
import world.landfall.infection.ModInfections;
import world.landfall.infection.api.Infection;
import world.landfall.infection.api.InfectionRegistry;
import world.landfall.infection.api.InfectionStage;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class CommonColdInfection extends Infection {

    @Override
    public ResourceLocation location() {
        return InfectionMod.path("common_cold");
    }

    @Override
    public ResourceLocation initialStage() {
        return ResourceLocation.parse("infection:common_cold_initial");
    }

    @Override
    public Collection<ResourceLocation> validStages() {
        return List.of(ModInfections.COMMON_COLD_INITIAL_STAGE.getId());
    }
    public static class CommonColdStageOne extends InfectionStage {

        @Override
        public ResourceLocation nextStage() {
            return ModInfections.NONE_STAGE.getId();
        }

        @Override
        public int stageNumber() {
            return 0;
        }

        @Override
        public int lengthInTicks() {
            return 200;
        }

        @Override
        public Collection<Holder<MobEffect>> currentEffects() {
            return List.of(
                    MobEffects.WEAKNESS
            );
        }

        @Override
        public ResourceLocation location() {
            return InfectionMod.path("common_cold_initial");
        }
    }
}
