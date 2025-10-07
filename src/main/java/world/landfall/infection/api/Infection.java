package world.landfall.infection.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import world.landfall.infection.InfectionMod;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class Infection {
    private InfectionStage currentStage;
    public Infection() {

    }
    public void tick(Player player) {
        if (currentStage == null)
            return;
        currentStage.tick(player);
        if (currentStage.getTimeExisted() >= currentStage.lengthInTicks() && currentStage.lengthInTicks() >= 0)
            currentStage = currentStage.nextStage().get();
    }
    public void activate() {
        currentStage = initialStage().get();
    }
    public boolean isActive() {
        return currentStage != null;
    }
    public InfectionStage getCurrentStage() {
        return currentStage;
    }
    public abstract ResourceLocation location();
    public abstract DeferredHolder<InfectionStage,InfectionStage> initialStage();
    public abstract Collection<ResourceLocation> validStages();
}
