package world.landfall.infection.infections;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import world.landfall.infection.InfectionMod;
import world.landfall.infection.api.Infection;
import world.landfall.infection.api.InfectionStage;
import world.landfall.infection.api.InfectionStageInstance;

import java.util.Collection;
import java.util.List;

public class PickleboundInfection extends Infection {
    @Override
    public ResourceLocation location() {
        return InfectionMod.path("picklebound");
    }

    @Override
    public ResourceLocation initialStage() {
        return InfectionMod.path("tingling_glow");
    }

    @Override
    public Collection<ResourceLocation> validStages() {
        return List.of(
                InfectionMod.path("tingling_glow"),
                InfectionMod.path("hive_whispers"),
                InfectionMod.path("creeping_instability"),
                InfectionMod.path("the_picklebound")
        );
    }

    @Override
    public float infectionCoefficient() {
        return 0;
    }
    public static class TinglingGlowStage extends InfectionStage {

        public TinglingGlowStage() {
            super(InfectionMod.path("tingling_glow"), InfectionMod.path("picklebound"), InfectionMod.path("hive_whispers"), 0, 24_000 * 3); // Three in-game days
        }

        @Override
        public Collection<Holder<MobEffect>> currentEffects() {
            return List.of();
        }

        @Override
        public void tick(Player player, InfectionStageInstance instance) {
            
        }
    }
}
