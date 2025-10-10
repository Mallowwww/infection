package world.landfall.infection.api;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.List;

public record Immunity(Collection<ResourceLocation> effectiveAgainst, int length, int timeExisted) {
    public static Immunity NONE_IMMUNITY = new Immunity(List.of(), -1, 0);
}
