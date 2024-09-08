package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AbstractChestIcon;
import mc.compendium.chestinterface.components.ChestInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestIconEvent<
    OriginalEventType extends InventoryEvent
> extends ChestInterfaceEvent<OriginalEventType> {

    private final AbstractChestIcon<?> icon;
    private final ItemStack item;

    //

    public ChestIconEvent(OriginalEventType originalEvent, HumanEntity player, Inventory inventory, ChestInterface<?, ?> chestInterface, AbstractChestIcon<?> icon, ItemStack item) {
        this(originalEvent, player, inventory, chestInterface, icon, item, true);
    }

    public ChestIconEvent(OriginalEventType originalEvent, HumanEntity player, Inventory inventory, ChestInterface<?, ?> chestInterface, AbstractChestIcon<?> icon, ItemStack item, boolean cancellable) {
        super(originalEvent, player, inventory, chestInterface, cancellable);

        this.icon = icon;
        this.item = item;
    }

    //

    public AbstractChestIcon<?> getIcon() { return this.icon; }

    public ItemStack getItem() { return this.item; }
}
