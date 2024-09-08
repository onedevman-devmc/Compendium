package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.BasicMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public abstract class MenuCloseEvent<
    MenuType extends BasicMenu<?, ?, ?>
> extends BasicMenuEvent<InventoryCloseEvent, MenuType> {

    public MenuCloseEvent(InventoryCloseEvent originalEvent, HumanEntity entity, Inventory inventory, MenuType menu) {
        this(originalEvent, entity, inventory, menu, true);
    }

    public MenuCloseEvent(InventoryCloseEvent originalEvent, HumanEntity entity, Inventory inventory, MenuType menu, boolean cancellable) {
        super(originalEvent, entity, inventory, menu, cancellable);
    }

}
