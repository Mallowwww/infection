package world.landfall.infection.infections;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import world.landfall.infection.InfectionMod;
import world.landfall.infection.api.Infection;
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
    public DeferredHolder<InfectionStage, InfectionStage> initialStage() {
        return null;
    }

    @Override
    public Collection<ResourceLocation> validStages() {
        return List.of();
    }
}
