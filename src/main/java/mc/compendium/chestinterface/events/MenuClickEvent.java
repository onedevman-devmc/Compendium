package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AbstractChestIcon;
import mc.compendium.chestinterface.components.BasicMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class MenuClickEvent<
    MenuType extends BasicMenu<?, ?, ?>
> extends BasicMenuEvent<InventoryClickEvent, MenuType> implements ItemClickEvent {

    private final ItemStack cursorItem;
    private final ItemStack clickedItem;
    private final Inventory clickedInventory;
    private final int slot;
    private final InventoryType.SlotType slotType;
    private final ClickType clickType;
    private final InventoryAction action;
    private final AbstractChestIcon<?> icon;

    //

    public MenuClickEvent(
        InventoryClickEvent originalEvent, HumanEntity entity, Inventory inventory, MenuType menu, ItemStack cursorItem,
        ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action,
        AbstractChestIcon<?> icon
    ) {
        this(originalEvent, entity, inventory, menu, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action, icon, true);
    }

    public MenuClickEvent(
        InventoryClickEvent originalEvent, HumanEntity entity, Inventory inventory, MenuType menu, ItemStack cursorItem,
        ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action,
        AbstractChestIcon<?> icon,
        boolean cancellable
    ) {
        super(originalEvent, entity, inventory, menu, cancellable);

        this.cursorItem = cursorItem;
        this.clickedItem = clickedItem;
        this.clickedInventory = clickedInventory;
        this.slot = slot;
        this.slotType = slotType;
        this.clickType = clickType;
        this.action = action;

        this.icon = icon;
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

    @Override
    public InventoryAction getAction() { return this.action; }

    //

    public AbstractChestIcon<?> getIcon() { return this.icon; }

}
