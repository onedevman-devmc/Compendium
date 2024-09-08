package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.BasicMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

public class BasicMenuEvent<
    OriginalEventType extends InventoryEvent,
    MenuType extends BasicMenu<?, ?, ?>
> extends ChestInterfaceEvent<OriginalEventType> {

    private final MenuType menu;

    //

    public BasicMenuEvent(OriginalEventType originalEvent, HumanEntity entity, Inventory inventory, MenuType menu) {
        this(originalEvent, entity, inventory, menu, true);
    }

    public BasicMenuEvent(OriginalEventType originalEvent, HumanEntity entity, Inventory inventory, MenuType menu, boolean cancellable) {
        super(originalEvent, entity, inventory, menu, cancellable);

        //

        this.menu = menu;
    }

    //

    public MenuType getMenu() { return this.menu; }

}
