package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.HopperMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class HopperMenuOpenEvent extends MenuOpenEvent<HopperMenu<?>> {

    public HopperMenuOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public HopperMenuOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }

}
