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
import world.landfall.infection.api.InfectionInstance;
import world.landfall.infection.api.InfectionRegistry;

import java.util.function.Supplier;

public class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, InfectionMod.MODID);
    public static final Supplier<AttachmentType<InfectionInstance>> ACTIVE_INFECTION = TYPES.register(
            "active_infection", () -> AttachmentType.builder(ModInfections.NONE_INFECTION.get()::create).serialize(
                    RecordCodecBuilder.create(instance -> instance.group(
                            Codec.STRING.fieldOf("infection").forGetter((InfectionInstance infection) -> infection.type.toString()),
                            Codec.STRING.fieldOf("stage").forGetter((InfectionInstance infection) -> {
                                if (infection.getCurrentStage() == null) {
                                    return "infections:none";
                                }

                                return infection.getCurrentStage().type.toString();
                            })
                    ).apply(instance, (infection, stage) -> {
                        var i = InfectionRegistry.INFECTION_REGISTRY.get(ResourceLocation.parse(infection)).create();
                        System.out.println("A "+infection+" "+stage);
                        if (i == null)
                            return ModInfections.NONE_INFECTION.get().create();
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
