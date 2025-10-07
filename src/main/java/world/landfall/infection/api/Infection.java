package world.landfall.infection.api;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import world.landfall.infection.InfectionMod;
import world.landfall.infection.ModInfections;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class Infection {
    public abstract ResourceLocation location();
    public abstract ResourceLocation initialStage();
    public abstract Collection<ResourceLocation> validStages();
    public abstract float infectionCoefficient();
    public InfectionInstance create() {
        return new InfectionInstance(location(), Minecraft.getInstance().level != null ? Minecraft.getInstance().level.random : RandomSource.create());
    }
    public InfectionInstance createMutated(InfectionInstance infectionInstance) {
        var random = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.random : RandomSource.create();
        var newGenes = new float[16];
        for (int i = 0; i < 16; i++) {
            newGenes[i] = (infectionInstance.genes[i] * 2 + random.nextFloat())/3;
        }
        return new InfectionInstance(location(), newGenes);
    }
}
