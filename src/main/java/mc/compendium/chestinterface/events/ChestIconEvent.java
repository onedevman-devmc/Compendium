package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AbstractChestIcon;
import mc.compendium.chestinterface.components.ChestInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestIconEvent<
    BukkitEventType extends InventoryEvent
> extends ChestInterfaceEvent<BukkitEventType> {

    private final AbstractChestIcon<?> icon;
    private final ItemStack item;

    //

    public ChestIconEvent(BukkitEventType bukkitEvent, HumanEntity player, Inventory inventory, ChestInterface<?, ?> chestInterface, AbstractChestIcon<?> icon, ItemStack item) {
        this(bukkitEvent, player, inventory, chestInterface, icon, item, true);
    }

    public ChestIconEvent(BukkitEventType bukkitEvent, HumanEntity player, Inventory inventory, ChestInterface<?, ?> chestInterface, AbstractChestIcon<?> icon, ItemStack item, boolean cancellable) {
        super(bukkitEvent, player, inventory, chestInterface, cancellable);

        this.icon = icon;
        this.item = item;
    }

    //

    public AbstractChestIcon<?> getIcon() { return this.icon; }

    public ItemStack getItem() { return this.item; }
}
