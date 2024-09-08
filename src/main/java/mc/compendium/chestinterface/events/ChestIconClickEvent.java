package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AbstractChestIcon;
import mc.compendium.chestinterface.components.ChestInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestIconClickEvent extends ChestIconEvent<InventoryClickEvent> implements ItemClickEvent {

    private final ItemStack cursorItem;
    private final Inventory clickedInventory;
    private final int slot;
    private final InventoryType.SlotType slotType;
    private final ClickType clickType;
    private final InventoryAction action;

    //

    public ChestIconClickEvent(
        InventoryClickEvent originalEvent, HumanEntity entity, Inventory inventory, ChestInterface<?, ?> chestInterface,
        AbstractChestIcon<?> icon, ItemStack cursorItem, ItemStack item, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action
    ) {
        this(originalEvent, entity, inventory, chestInterface, icon, cursorItem, item, clickedInventory, slot, slotType, clickType, action, true);
    }

    public ChestIconClickEvent(
        InventoryClickEvent originalEvent, HumanEntity entity, Inventory inventory, ChestInterface<?, ?> chestInterface,
        AbstractChestIcon<?> icon, ItemStack cursorItem, ItemStack item, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action,
        boolean cancellable
    ) {
        super(originalEvent, entity, inventory, chestInterface, icon, item, cancellable);

        this.cursorItem = cursorItem;
        this.clickedInventory = clickedInventory;
        this.slot = slot;
        this.slotType = slotType;
        this.clickType = clickType;
        this.action = action;
    }

    //

    @Override
    public ItemStack getCursorItem() { return this.cursorItem; }

    @Override
    public ItemStack getClickedItem() { return this.getItem(); }

    @Override
    public Inventory getClickedInventory() { return this.clickedInventory; }

    @Override
    public int getSlot() { return this.slot; }

    @Override
    public InventoryType.SlotType getSlotType() { return this.slotType; }

    @Override
    public ClickType getClickType() { return this.clickType; }

    @Override
    public InventoryAction getAction() { return this.action; }

}
