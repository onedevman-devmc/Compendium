package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public class AnvilInputEvent<
    BukkitEventType extends org.bukkit.event.Event
> extends ChestInterfaceEvent<BukkitEventType> {

    public AnvilInputEvent(BukkitEventType bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput) {
        this(bukkitEvent, entity, inventory, anvilInput, true);
    }

    public AnvilInputEvent(BukkitEventType bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, boolean cancellable) {
        super(bukkitEvent, entity, inventory, anvilInput, cancellable);
    }

}
