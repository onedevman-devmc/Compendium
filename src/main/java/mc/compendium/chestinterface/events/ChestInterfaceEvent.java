package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public abstract class ChestInterfaceEvent<
    OriginalEventType
> extends InterfaceEvent<
    OriginalEventType,
    Inventory,
    ChestInterface<?, ?>
> {

    public ChestInterfaceEvent(OriginalEventType originalEvent, HumanEntity entity, Inventory inventory, ChestInterface<?, ?> relevantInterface) {
        this(originalEvent, entity, inventory, relevantInterface, true);
    }

    public ChestInterfaceEvent(OriginalEventType originalEvent, HumanEntity entity, Inventory inventory, ChestInterface<?, ?> relevantInterface, boolean cancellable) {
        super(originalEvent, entity, inventory, relevantInterface, cancellable);
    }

}
