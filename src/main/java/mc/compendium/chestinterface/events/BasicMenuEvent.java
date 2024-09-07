package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.BasicMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

public class BasicMenuEvent<
    BukkitEventType extends InventoryEvent,
    MenuType extends BasicMenu<?, ?, ?>
> extends ChestInterfaceEvent<BukkitEventType> {

    private final MenuType menu;

    //

    public BasicMenuEvent(BukkitEventType bukkitEvent, HumanEntity entity, Inventory inventory, MenuType menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public BasicMenuEvent(BukkitEventType bukkitEvent, HumanEntity entity, Inventory inventory, MenuType menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);

        //

        this.menu = menu;
    }

    //

    public MenuType getMenu() { return this.menu; }

}
