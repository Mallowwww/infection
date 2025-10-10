package world.landfall.infection.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import world.landfall.infection.ModAttachments;

import java.util.Collection;

public class TreatmentInstance {
    public final ResourceLocation type;
    public final Collection<ResourceLocation> effectiveAgainst;
    final int length; int timeExisted;
    public TreatmentInstance(ResourceLocation _type, Collection<ResourceLocation> _effectiveAgainst, int _length, int _timeExisted) {
        type = _type;
        effectiveAgainst = _effectiveAgainst;
        length = _length;
        timeExisted = _timeExisted;
    }
    public void tick(Player player) {
        var treatmentType = getType();
        if (!player.hasData(ModAttachments.ACTIVE_INFECTION) || effectiveAgainst.contains(player.getData(ModAttachments.ACTIVE_INFECTION).type))
            return;
        treatmentType.tick(player, this, player.getData(ModAttachments.ACTIVE_INFECTION));
        timeExisted += 1;
    }
    public Treatment getType() {
        return InfectionRegistry.TREATMENT_REGISTRY.get(type);
    }
    public ResourceLocation type() {
        return type;
    }
    public Collection<ResourceLocation> effectiveAgainst() {
        return effectiveAgainst;
    }
    public int length() {
        return length;
    }
    public int timeExisted() {
        return timeExisted;
    }
}
