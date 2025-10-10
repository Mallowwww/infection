package world.landfall.infection;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import world.landfall.infection.api.*;
import world.landfall.infection.infections.InternalInfections;

import java.util.*;
import java.util.function.Supplier;

public class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, InfectionMod.MODID);
    public static final Supplier<AttachmentType<InfectionInstance>> ACTIVE_INFECTION = TYPES.register(
            "active_infection", () -> AttachmentType.builder(() -> InternalInfections.NONE_INFECTION.get().create()).serialize(
                    RecordCodecBuilder.create(instance -> instance.group(
                            Codec.STRING.fieldOf("infection").forGetter((InfectionInstance infection) -> infection.type.toString()),
                            Codec.STRING.fieldOf("stage").forGetter((InfectionInstance infection) -> {
                                if (infection.getCurrentStage() == null) {
                                    return "infections:none";
                                }
                                return infection.getCurrentStage().type.toString();
                            }),
                            Codec.list(Codec.FLOAT).fieldOf("genes").forGetter((InfectionInstance infection) -> {
                                var list = new ArrayList<Float>();
                                for (var x : infection.genes)
                                    list.add(x);
                                return list;
                            }),
                            Codec.INT.fieldOf("ticks").forGetter((infectionInstance -> infectionInstance.getCurrentStage().timeExisted))
                    ).apply(instance, (infectionName, stage, genes, time) -> {
                        var genesArray = new float[16];
                        for (int j = 0; j < 16; j++)
                            genesArray[j] = genes.get(j);
                        var infection = InfectionRegistry.INFECTION_REGISTRY.get(ResourceLocation.parse(infectionName)).create(genesArray);
                        System.out.println("A "+infection+" "+stage);
                        if (infection == null)
                            return InternalInfections.NONE_INFECTION.get().create();
                        infection.setCurrentStage(ResourceLocation.parse(stage));
                        infection.activate();
                        infection.getCurrentStage().setTimeExisted(time);
                        return infection;
                    }))
            ).build()
    );
    public static final Supplier<AttachmentType<Immunity>> IMMUNITY = TYPES.register(
            "immunity", () -> AttachmentType.builder(() -> new Immunity(List.of(), -1, 0)).serialize(
                    RecordCodecBuilder.create(instance -> instance.group(
                            Codec.STRING.listOf().fieldOf("effective_against").forGetter((Immunity immunity) -> immunity.effectiveAgainst().stream().map(ResourceLocation::toString).toList()),
                            Codec.INT.fieldOf("length").forGetter(Immunity::length),
                            Codec.INT.fieldOf("timeExisted").forGetter(Immunity::timeExisted)
                    ).apply(instance, (effectiveAgainst, length, startTick) -> new Immunity(effectiveAgainst.stream().map(ResourceLocation::parse).toList(), length, startTick)))
            ).build()
    );
    public static final Supplier<AttachmentType<TreatmentInstance>> TREATMENT = TYPES.register(
            "treatment", () -> AttachmentType.<TreatmentInstance>builder(() -> new TreatmentInstance(ResourceLocation.parse("infection:none"), List.of(), -1, 0)).serialize(
                    RecordCodecBuilder.create(instance -> instance.group(
                            Codec.STRING.fieldOf("type").forGetter((treatment) -> treatment.type().toString()),
                            Codec.STRING.listOf().fieldOf("effective_against").forGetter((TreatmentInstance treatment) -> treatment.effectiveAgainst().stream().map(ResourceLocation::toString).toList()),
                            Codec.INT.fieldOf("length").forGetter(TreatmentInstance::length),
                            Codec.INT.fieldOf("timeExisted").forGetter(TreatmentInstance::timeExisted)

                    ).apply(instance, (type, effectiveAgainst, length, startTick) -> new TreatmentInstance(ResourceLocation.parse(type) ,effectiveAgainst.stream().map(ResourceLocation::parse).toList(), length, startTick)))
            ).build()
    );
    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
    }

}
