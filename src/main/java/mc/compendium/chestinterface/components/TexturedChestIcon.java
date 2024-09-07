package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.components.configurations.TexturedChestIconConfig;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;

public class TexturedChestIcon extends AbstractChestIcon<TexturedChestIconConfig> {

    public TexturedChestIcon(TexturedChestIconConfig config) {
        super(config);
    }

    //

    @Override
    public ItemStack toBukkit() {
        ItemStack item = super.toBukkit();

        if(item.hasItemMeta()) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            try {
                this.config().applyTexture(meta);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            item.setItemMeta(meta);
        }

        return item;
    }
}
