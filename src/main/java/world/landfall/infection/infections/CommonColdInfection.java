package world.landfall.infection.infections;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;
import world.landfall.infection.InfectionMod;
import world.landfall.infection.ModInfections;
import world.landfall.infection.api.Infection;
import world.landfall.infection.api.InfectionRegistry;
import world.landfall.infection.api.InfectionStage;
import world.landfall.infection.api.InfectionStageInstance;

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
        return List.of(ResourceLocation.parse("infection:common_cold_initial"));
    }

    @Override
    public float infectionCoefficient() {
        return .3f;
    }

    public static class CommonColdStageOne extends InfectionStage {

        public CommonColdStageOne() {
            super(InfectionMod.path("common_cold_initial"), InfectionMod.path("common_cold"), ResourceLocation.parse("infection:none"), 0, 200);
        }

        @Override
        public Collection<Holder<MobEffect>> currentEffects() {
            return List.of(
                    MobEffects.WEAKNESS
            );
        }
        @Override
        public void tick(Player player, InfectionStageInstance instance) {
            var random = player.getRandom();
            var genes = instance.genes;
            var level = player.level();
            var chance = genes[0] * .1;
            var ticks = player.tickCount;
//            if (ticks%20==0)
//                player.sendSystemMessage(Component.literal(""+genes[0]));

            if (random.nextDouble() < chance && level instanceof ServerLevel serverLevel) {

                serverLevel.<ParticleOptions>sendParticles(
                        ParticleTypes.DRIPPING_WATER, player.getX() + random.nextDouble() * .8 - .4, player.getY() + random.nextDouble() * 1.8, player.getZ() + random.nextDouble() * .8 - .4, 1, 0, 0, 0, 1
                );

            }
        }
    }
}
