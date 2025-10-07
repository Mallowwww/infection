package world.landfall.infection;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import world.landfall.infection.api.Infection;

import java.util.function.Supplier;

public class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, InfectionMod.MODID);
    public static final Supplier<AttachmentType<Infection>> ACTIVE_INFECTION = TYPES.register(
            "active_infection", () -> AttachmentType.builder(ModInfections.NONE_INFECTION).build()
    );
    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
    }

}
