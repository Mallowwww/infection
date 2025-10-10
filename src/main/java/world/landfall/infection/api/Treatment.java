package world.landfall.infection.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

public abstract class Treatment {
    private final ResourceLocation location;
    private final Collection<ResourceLocation> effectiveAgainst;
    private final int length;
    public Treatment(ResourceLocation _location, Collection<ResourceLocation> _effectiveAgainst, int _length) {
        location = _location;
        effectiveAgainst = _effectiveAgainst;
        length = _length;
    }
    public abstract void tick(Player player, TreatmentInstance treatmentInstance, InfectionInstance infectionInstance);
    public Collection<ResourceLocation> effectiveAgainst() { return effectiveAgainst; }
    public int length() { return length; }
    public ResourceLocation location() { return location; }
    public TreatmentInstance create() {
        return new TreatmentInstance(location(), effectiveAgainst(), length(), 0);
    }
}
