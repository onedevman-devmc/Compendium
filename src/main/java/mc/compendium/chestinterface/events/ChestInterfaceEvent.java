package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestInterface;
import mc.compendium.events.Event;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public abstract class ChestInterfaceEvent extends Event {

    private final org.bukkit.event.Event _bukkit_event;
    private final HumanEntity _entity;
    private final Inventory _inventory;
    private final ChestInterface<?> _chest_interface;

    //

    public ChestInterfaceEvent(org.bukkit.event.Event bukkit_event, HumanEntity entity, Inventory inventory, ChestInterface<?> chest_interface) {
        this(bukkit_event, entity, inventory, chest_interface, true);
    }

    public ChestInterfaceEvent(org.bukkit.event.Event bukkit_event, HumanEntity entity, Inventory inventory, ChestInterface<?> chest_interface, boolean cancellable) {
        super(cancellable);

        this._bukkit_event = bukkit_event;
        this._entity = entity;
        this._inventory = inventory;
        this._chest_interface = chest_interface;
    }

    //

    public org.bukkit.event.Event bukkitEvent() { return this._bukkit_event; }

    public HumanEntity entity() { return this._entity; }

    public Inventory inventory() { return this._inventory; }

    public ChestInterface<?> chestInterface() { return this._chest_interface; }

}
