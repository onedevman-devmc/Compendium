package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AnvilInputClickEvent extends AnvilInputEvent<InventoryClickEvent> implements ItemClickEvent {

    private final ItemStack cursorItem;
    private final Inventory clickedInventory;
    private final ItemStack clickedItem;
    private final int slot;
    private final InventoryType.SlotType slotType;
    private final ClickType clickType;

    //

    public AnvilInputClickEvent(
        InventoryClickEvent bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, ItemStack cursorItem,
        ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType
    ) {
        this(bukkitEvent, entity, inventory, anvilInput, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, true);
    }

    public AnvilInputClickEvent(
        InventoryClickEvent bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, ItemStack cursorItem,
        ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, boolean cancellable
    ) {
        super(bukkitEvent, entity, inventory, anvilInput, cancellable);

        this.cursorItem = cursorItem;
        this.clickedItem = clickedItem;
        this.clickedInventory = clickedInventory;
        this.slot = slot;
        this.slotType = slotType;
        this.clickType = clickType;
    }

    //


    @Override
    public ItemStack getCursorItem() { return this.cursorItem; }

    @Override
    public ItemStack getClickedItem() { return this.clickedItem; }

    @Override
    public Inventory getClickedInventory() { return this.clickedInventory; }

    @Override
    public int getSlot() { return this.slot; }

    @Override
    public InventoryType.SlotType getSlotType() { return this.slotType; }

    @Override
    public ClickType getClickType() { return this.clickType; }

}
