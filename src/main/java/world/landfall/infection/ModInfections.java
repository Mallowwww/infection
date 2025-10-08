package world.landfall.infection;

import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import world.landfall.infection.api.Infection;
import world.landfall.infection.api.InfectionRegistry;
import world.landfall.infection.api.InfectionStage;
import world.landfall.infection.api.InfectionStageInstance;
import world.landfall.infection.infections.CommonColdInfection;
import world.landfall.infection.infections.PickleboundInfection;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ModInfections {
    private static final DeferredRegister<Infection> INFECTIONS = DeferredRegister.create(InfectionRegistry.INFECTION_REGISTRY, InfectionMod.MODID);
    private static final DeferredRegister<InfectionStage> STAGES = DeferredRegister.create(InfectionRegistry.STAGE_REGISTRY, InfectionMod.MODID);

    public static final Supplier<InfectionStage> COMMON_COLD_INITIAL_STAGE = STAGES.register("common_cold_initial", CommonColdInfection.CommonColdStageOne::new);
    public static final Supplier<Infection> COMMON_COLD_INFECTION = INFECTIONS.register("common_cold", CommonColdInfection::new);
    public static final Supplier<InfectionStage> TINGLING_GLOW_STAGE = STAGES.register("tingling_glow", PickleboundInfection.TinglingGlowStage::new);
    public static final Supplier<Infection> PICKLEBOUND_INFECTION = INFECTIONS.register("picklebound", PickleboundInfection::new);
    public static void register(IEventBus eventBus) {
        STAGES.register(eventBus);
        INFECTIONS.register(eventBus);
    }
}
