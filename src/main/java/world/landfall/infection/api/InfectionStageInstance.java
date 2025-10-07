package world.landfall.infection.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class InfectionStageInstance {
    private int timeExisted = 0;
    public final ResourceLocation type;
    public final float[] genes;
    public InfectionStageInstance(ResourceLocation type, float[] genes) {
        this.type = type;
        this.genes = genes;
    }
    public int getTimeExisted() {
        return timeExisted;
    }
    public void tick(Player player) {
        timeExisted++;
        for (var x : InfectionRegistry.STAGE_REGISTRY.get(type).currentEffects())
            player.addEffect(new MobEffectInstance(x,1, 1, false, false), null);
    }
    public InfectionStage getType() {
        return InfectionRegistry.STAGE_REGISTRY.get(type);
    }
}
