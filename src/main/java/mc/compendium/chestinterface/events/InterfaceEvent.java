package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.BasicInterface;
import mc.compendium.events.Event;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public abstract class InterfaceEvent<
    OriginalEventType,
    InventoryType extends Inventory,
    I extends BasicInterface<?, InventoryType, ?>
> extends Event {

    private final OriginalEventType originalEvent;
    private final HumanEntity entity;
    private final InventoryType inventory;
    private final I relevantInterface;

    //

    public InterfaceEvent(OriginalEventType originalEvent, HumanEntity entity, InventoryType inventory, I relevantInterface) {
        this(originalEvent, entity, inventory, relevantInterface, true);
    }

    public InterfaceEvent(OriginalEventType originalEvent, HumanEntity entity, InventoryType inventory, I relevantInterface, boolean cancellable) {
        super(cancellable);

        this.originalEvent = originalEvent;
        this.entity = entity;
        this.inventory = inventory;
        this.relevantInterface = relevantInterface;
    }

    //

    public OriginalEventType getOriginalEvent() { return this.originalEvent; }

    public HumanEntity getPlayer() { return this.entity; }

    public InventoryType getInventory() { return this.inventory; }

    public I getInterface() { return this.relevantInterface; }

}
