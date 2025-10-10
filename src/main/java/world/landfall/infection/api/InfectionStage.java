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
    private final ResourceLocation nextStage;
    private final int stageNumber;
    private final int length;
    private final ResourceLocation location;
    private final ResourceLocation infectionType;
    public InfectionStage(ResourceLocation _location, ResourceLocation _infectionType, ResourceLocation _nextStage, int _stageNumber, int _length) {
        nextStage = _nextStage;
        stageNumber = _stageNumber;
        length = _length; // make this negative if it does not advance to another stage or end
        location = _location;
        infectionType = _infectionType;
    }
    public ResourceLocation nextStage() { return nextStage; }
    public int stageNumber() { return stageNumber; }
    public int lengthInTicks() { return length; }
    public abstract Collection<Holder<MobEffect>> currentEffects();
    public ResourceLocation location() { return location; }
    public ResourceLocation infectionType() { return infectionType; }
    public abstract void tick(Player player, InfectionStageInstance instance);
    public InfectionStageInstance create() {
        return new InfectionStageInstance(location(), new float[16]);
    }
    public InfectionStageInstance create(InfectionInstance infectionInstance) {
        return new InfectionStageInstance(location(), infectionInstance.genes);
    }
}
