package world.landfall.infection.treatments;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import world.landfall.infection.InfectionMod;
import world.landfall.infection.api.InfectionInstance;
import world.landfall.infection.api.InfectionRegistry;
import world.landfall.infection.api.Treatment;
import world.landfall.infection.api.TreatmentInstance;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class InternalTreatments {
    public static final DeferredRegister<Treatment> TREATMENTS = DeferredRegister.create(InfectionRegistry.TREATMENT_REGISTRY, InfectionMod.MODID);
    public static final Supplier<Treatment> NONE_TREATMENT = TREATMENTS.register("none", () -> new Treatment(InfectionMod.path("none"), List.of(), -1) {
        @Override
        public void tick(Player player, TreatmentInstance treatmentInstance, InfectionInstance infectionInstance) {

        }
    });
    public static void register(IEventBus eventBus) {
        TREATMENTS.register(eventBus);
    }
}
