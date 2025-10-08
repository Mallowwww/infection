package world.landfall.infection.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import world.landfall.infection.ModInfections;

public class InfectionInstance {
    private InfectionStageInstance currentStage;
    public final ResourceLocation type;
    public final float[] genes;
    public InfectionInstance(ResourceLocation type, RandomSource randomSource) {
        this.type = type;
        genes = new float[16];
        for (int i = 0; i < 16; i++)
            genes[i] = randomSource.nextFloat();

    }
    public InfectionInstance(ResourceLocation type, float[] genes) {
        this.type = type;
        this.genes = genes;
    }
    public void tick(Player player) {
        if (currentStage == null)
            return;
        currentStage.tick(player);
        if (currentStage.getTimeExisted() >= currentStage.getType().lengthInTicks() && currentStage.getType().lengthInTicks() >= 0)
            currentStage = ModInfections.NONE_STAGE.get().create();
    }
    public void activate() {
        currentStage = InfectionRegistry.STAGE_REGISTRY.get(getType().initialStage()).create(this);
//        if (currentStage!=null)
//            currentStage.reset();
    }
    public boolean isActive() {
        return currentStage != null;
    }
    public void setCurrentStage(ResourceLocation location) {
        var stage = InfectionRegistry.STAGE_REGISTRY.get(location);
        if (stage != null)
            currentStage = stage.create(this);
    }
    public Infection getType() {
        return InfectionRegistry.INFECTION_REGISTRY.get(type);
    }
    public InfectionStageInstance getCurrentStage() {
        return currentStage;
    }
}
