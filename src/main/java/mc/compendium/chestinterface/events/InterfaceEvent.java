package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.BasicInterface;
import mc.compendium.events.Event;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public abstract class InterfaceEvent<
    BukkitEventType extends org.bukkit.event.Event,
    InventoryType extends Inventory,
    I extends BasicInterface<?, InventoryType, ?>
> extends Event {

    private final BukkitEventType bukkitEvent;
    private final HumanEntity entity;
    private final InventoryType inventory;
    private final I relevantInterface;

    //

    public InterfaceEvent(BukkitEventType bukkitEvent, HumanEntity entity, InventoryType inventory, I relevantInterface) {
        this(bukkitEvent, entity, inventory, relevantInterface, true);
    }

    public InterfaceEvent(BukkitEventType bukkitEvent, HumanEntity entity, InventoryType inventory, I relevantInterface, boolean cancellable) {
        super(cancellable);

        this.bukkitEvent = bukkitEvent;
        this.entity = entity;
        this.inventory = inventory;
        this.relevantInterface = relevantInterface;
    }

    //

    public BukkitEventType bukkitEvent() { return this.bukkitEvent; }

    public HumanEntity entity() { return this.entity; }

    public InventoryType inventory() { return this.inventory; }

    public I relevantInterface() { return this.relevantInterface; }

}
