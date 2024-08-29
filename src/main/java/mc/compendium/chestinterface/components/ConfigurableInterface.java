package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.components.configurations.InterfaceConfig;
import mc.compendium.chestinterface.events.InterfaceEvent;
import org.bukkit.inventory.Inventory;

public abstract class ConfigurableInterface<
    ConfigType extends InterfaceConfig,
    BukkitType,
    InventoryType extends Inventory,
    EventType extends InterfaceEvent<
        ?,
        InventoryType,
        ? extends BasicInterface<?, InventoryType, ?>
    >
> extends BasicInterface<BukkitType, InventoryType, EventType> {

    private ConfigType config;

    //

    protected ConfigurableInterface(ConfigType config, Class<EventType> eventType) {
        super(eventType);

        //

        this.config = config;
    }

    //

    public ConfigType config() { return config; }

}
