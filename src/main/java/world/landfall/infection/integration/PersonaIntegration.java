package world.landfall.infection.integration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import world.landfall.persona.data.CharacterProfile;
import world.landfall.persona.data.PlayerCharacterCapability;
import world.landfall.persona.data.PlayerCharacterData;
import world.landfall.persona.features.landfalladdon.LandfallAddonData;
import world.landfall.persona.registry.GlobalCharacterRegistry;
import world.landfall.persona.util.CharacterUtils;

public class PersonaIntegration {

    public static void setCharacterData(Player player, ResourceLocation location, CompoundTag data) {
        if (!player.hasData(PlayerCharacterCapability.CHARACTER_DATA))
            return;
        PlayerCharacterData characters = player.getData(PlayerCharacterCapability.CHARACTER_DATA);
        var character = characters.getCharacter(characters.getActiveCharacterId());
        character.setModData(location, data);
    }
    public static CompoundTag getCharacterData(Player player, ResourceLocation location) {
        if (!player.hasData(PlayerCharacterCapability.CHARACTER_DATA))
            return new CompoundTag();
        PlayerCharacterData characters = player.getData(PlayerCharacterCapability.CHARACTER_DATA);
        var character = characters.getCharacter(characters.getActiveCharacterId());

        return character.getModData(location);
    }
}
