package world.landfall.infection;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import world.landfall.infection.api.Infection;
import world.landfall.infection.api.InfectionRegistry;

import java.util.function.Supplier;

public class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, InfectionMod.MODID);
    public static final Supplier<AttachmentType<Infection>> ACTIVE_INFECTION = TYPES.register(
            "active_infection", () -> AttachmentType.builder(ModInfections.NONE_INFECTION).serialize(
                    RecordCodecBuilder.create(instance -> instance.group(
                            Codec.STRING.fieldOf("infection").forGetter((Infection infection) -> infection.location().toString()),
                            Codec.STRING.fieldOf("stage").forGetter((Infection infection) -> {
                                if (infection.getCurrentStage() == null) {
                                    return "infections:none";
                                }

                                return infection.getCurrentStage().type.toString();
                            })
                    ).apply(instance, (infection, stage) -> {
                        var i = InfectionRegistry.INFECTION_REGISTRY.get(ResourceLocation.parse(infection));
                        System.out.println("A "+infection+" "+stage);
                        if (i == null)
                            return ModInfections.NONE_INFECTION.get();
                        i.setCurrentStage(ResourceLocation.parse(stage));
                        i.activate();
                        return i;
                    }))
            ).build()
    );
    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
    }

}
