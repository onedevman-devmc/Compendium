package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

public class BasicMenuEvent<
    BukkitEventType extends InventoryEvent
> extends ChestInterfaceEvent<BukkitEventType> {

    private final ChestMenu menu;

    //

    public BasicMenuEvent(BukkitEventType bukkitEvent, HumanEntity entity, Inventory inventory, ChestMenu menu) {
        super(bukkitEvent, entity, inventory, menu);

        this.menu = menu;
    }

    //

    public ChestMenu menu() { return this.menu; }

    //

    @Override
    public BukkitEventType bukkitEvent() { return super.bukkitEvent(); }

}
