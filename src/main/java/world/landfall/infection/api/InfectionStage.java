package world.landfall.infection.api;

import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class InfectionStage {
    public abstract ResourceLocation nextStage();
    public abstract int stageNumber();
    public abstract int lengthInTicks(); // make this negative if it does not advance to another stage
    public abstract Collection<Holder<MobEffect>> currentEffects();
    public abstract ResourceLocation location();
    public abstract ResourceLocation infectionType();
    public abstract void tick(Player player, InfectionStageInstance instance);
    public InfectionStageInstance create() {
        return new InfectionStageInstance(location(), new float[16]);
    }
    public InfectionStageInstance create(InfectionInstance infectionInstance) {
        return new InfectionStageInstance(location(), infectionInstance.genes);
    }
}
