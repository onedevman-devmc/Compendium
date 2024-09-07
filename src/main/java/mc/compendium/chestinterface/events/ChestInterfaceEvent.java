package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public abstract class ChestInterfaceEvent<
    BukkitEventType extends org.bukkit.event.Event
> extends InterfaceEvent<
    BukkitEventType,
    Inventory,
    ChestInterface<?, ?>
> {

    public ChestInterfaceEvent(BukkitEventType bukkitEvent, HumanEntity entity, Inventory inventory, ChestInterface<?, ?> relevantInterface) {
        this(bukkitEvent, entity, inventory, relevantInterface, true);
    }

    public ChestInterfaceEvent(BukkitEventType bukkitEvent, HumanEntity entity, Inventory inventory, ChestInterface<?, ?> relevantInterface, boolean cancellable) {
        super(bukkitEvent, entity, inventory, relevantInterface, cancellable);
    }

}
