package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestIcon;
import mc.compendium.chestinterface.components.ChestInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestIconClickEvent extends ChestIconEvent<InventoryClickEvent> {

    private final int slot;
    private final ClickType mouseClick;

    //

    public ChestIconClickEvent(InventoryClickEvent bukkitEvent, HumanEntity entity, Inventory inventory, ChestInterface<?, ?> chestInterface, ChestIcon icon, ItemStack item, int slot, ClickType mouseClick) {
        super(bukkitEvent, entity, inventory, chestInterface, icon, item);

        this.mouseClick = mouseClick;
        this.slot = slot;
    }

    //

    public InventoryClickEvent bukkitEvent() { return (InventoryClickEvent) super.bukkitEvent(); }

    //

    public ClickType mouseClick() { return this.mouseClick; }

    public int slot() { return this.slot; }

}
