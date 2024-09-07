package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.bukkit.ChestInterfaceBukkitIdentifier;
import mc.compendium.chestinterface.components.configurations.ChestMenuConfig;
import mc.compendium.chestinterface.events.BasicMenuEvent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class ChestMenu<
    IconType extends AbstractChestIcon<?>
> extends BasicMenu<ChestMenuConfig, IconType, BasicMenuEvent<?, ChestMenu<IconType>>> {

    public ChestMenu(ChestMenuConfig config) {
        super(config, (Class<BasicMenuEvent<?, ChestMenu<IconType>>>) ((Class<?>) BasicMenuEvent.class));
    }

    //

    @Override
    public Inventory toBukkit() {
        Inventory inventory = Bukkit.createInventory(new ChestInterfaceBukkitIdentifier(this), this.config().rows() * 9, this.config().name());

        IconType icon;
        for(int i = 0; i < this.config().rows()*9; ++i) {
            icon = this.getIcon(i);

            if(icon != null)
                inventory.setItem(i, icon.toBukkit());
        }

        return inventory;
    }

}
