package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.bukkit.ChestInterfaceBukkitIdentifier;
import mc.compendium.chestinterface.components.configurations.HopperMenuConfig;
import mc.compendium.chestinterface.events.BasicMenuEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class HopperMenu extends BasicMenu<HopperMenuConfig, BasicMenuEvent<?>> {

    public static final int MAX_HOPPER_INVENTORY_SLOT_COUNT = 5;

    //

    public HopperMenu(HopperMenuConfig config) {
        super(config, (Class<BasicMenuEvent<?>>) ((Class<?>) BasicMenuEvent.class));
    }

    //

    @Override
    public Inventory toBukkit() {
        Inventory inventory = Bukkit.createInventory(new ChestInterfaceBukkitIdentifier(this), InventoryType.HOPPER, this.config().name());

        for(int i = 0; i < HopperMenu.MAX_HOPPER_INVENTORY_SLOT_COUNT; ++i)
            inventory.setItem(i, this.getIcon(i).toBukkit());

        return inventory;
    }

}
