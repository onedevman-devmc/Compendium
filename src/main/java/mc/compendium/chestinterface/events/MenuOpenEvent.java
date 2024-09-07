package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.BasicMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public abstract class MenuOpenEvent<
    MenuType extends BasicMenu<?, ?, ?>
> extends BasicMenuEvent<InventoryOpenEvent, MenuType> {

    public MenuOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, Inventory inventory, MenuType menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public MenuOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, Inventory inventory, MenuType menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }

}
