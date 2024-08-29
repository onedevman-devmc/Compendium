package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.bukkit.ChestInterfaceBukkitIdentifier;
import mc.compendium.chestinterface.components.configurations.InterfaceConfig;
import mc.compendium.chestinterface.events.ChestInterfaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class ChestInterface<
    ConfigType  extends InterfaceConfig,
    EventType extends ChestInterfaceEvent<?>
> extends ConfigurableInterface<ConfigType, Inventory, Inventory, EventType> {

    public static ChestInterface<?, ? extends ChestInterfaceEvent> getAssociated(Inventory inventory) {
        InventoryHolder inventoryHolder = inventory.getHolder();

        if(inventoryHolder == null || !(inventoryHolder instanceof ChestInterfaceBukkitIdentifier chestInterfaceIdentifier))
            return null;

        return chestInterfaceIdentifier.chestInterface();
    }

    //

    protected ChestInterface(ConfigType config, Class<EventType> eventType) {
        super(config, eventType);
    }

}