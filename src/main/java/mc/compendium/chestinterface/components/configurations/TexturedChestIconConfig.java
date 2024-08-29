package mc.compendium.chestinterface.components.configurations;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import mc.compendium.reflection.MethodUtil;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class TexturedChestIconConfig extends ChestIconConfig {

    private String textureUrl;

    //

    public TexturedChestIconConfig(String textureUrl, String name, int amount, List<String> description, boolean enchanted) {
        super(Material.PLAYER_HEAD, name, amount, description, enchanted);

        this.setTextureUrl(textureUrl);
    }

    //

    public String textureUrl() { return this.textureUrl; }

    public String setTextureUrl(String textureUrl) { return this.textureUrl = textureUrl; }

    //

    public void applyTexture(SkullMeta meta) throws InvocationTargetException, IllegalAccessException {
        GameProfile textureProfile = new GameProfile(UUID.randomUUID(), "null");

        String finalTexturePropertyData = "{\"textures\":{\"SKIN\":{\"url\":\"" + this.textureUrl() + "\"}}}";

        Property texturesProperty = new Property(
            "textures",
            new String(Base64.getEncoder().encode(finalTexturePropertyData.getBytes()))
        );

        textureProfile.getProperties().put("textures", texturesProperty);

        MethodUtil.getInstance().get(meta, (m) -> m.getName().equals("setProfile")).invoke(meta, textureProfile);
    }

}