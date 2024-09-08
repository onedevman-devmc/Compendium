package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.Trade;
import mc.compendium.chestinterface.components.TradeAction;
import mc.compendium.chestinterface.components.TradeInterface;
import mc.compendium.chestinterface.components.TradeSlotType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;
import java.util.Set;

public class TradeEvent extends TradeInterfaceClickEvent {

    public static final Set<InventoryAction> SINGLE_TRADE_BUKKIT_ACTIONS = Set.of(
            InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_ALL,
            InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_SLOT,
            InventoryAction.HOTBAR_SWAP
    );
    public static final Set<InventoryAction> MULTIPLE_TRADE_BUKKIT_ACTIONS = Set.of(InventoryAction.MOVE_TO_OTHER_INVENTORY);

    //

    public static int calculateMaxTheoricalRecipeUses(MerchantRecipe recipe, ItemStack firstGivenIngredient, ItemStack secondGivenIngredient) {
        List<ItemStack> recipeIngredients = recipe.getIngredients();

        ItemStack firstRecipeIngredient = recipeIngredients.get(0);
        ItemStack secondRecipeIngredient = recipeIngredients.size() > 1 ? recipeIngredients.get(1) : null;

        if(
            (!firstRecipeIngredient.isSimilar(firstGivenIngredient))
            || (secondRecipeIngredient == null && secondGivenIngredient != null)
            || (secondRecipeIngredient != null && !secondRecipeIngredient.isSimilar(secondGivenIngredient))
        ) return 0;

        int[] requiredAmounts = new int[] {
            firstRecipeIngredient.getAmount(),
            secondRecipeIngredient == null ? 1 : secondRecipeIngredient.getAmount()
        };

        int[] givenAmounts = new int[] {
            firstGivenIngredient == null ? 64 : firstGivenIngredient.getAmount(),
            secondGivenIngredient == null ? 64 : secondGivenIngredient.getAmount()
        };

        int firstIngredientCount = givenAmounts[0] / requiredAmounts[0];
        int secondIngredientCount = givenAmounts[1] / requiredAmounts[1];

        int remainingRecipeUses = recipe.getMaxUses() - recipe.getUses();

        return Math.min(remainingRecipeUses, Math.min(firstIngredientCount, secondIngredientCount));
    }

    //

    private final int tradeIndex;
    private final Trade trade;
    private final int requestedUses;
    private final List<ItemStack> givenPrices;
    private final TradeAction tradeAction;

    //

    public TradeEvent(
            InventoryClickEvent originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface,
            ItemStack cursorItem, ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action,
            TradeSlotType tradeSlotType,
            int tradeIndex, Trade trade, int requestedUses, List<ItemStack> givenPrices, TradeAction tradeAction
    ) {
        this(
            originalEvent, entity, inventory, tradeInterface,
            cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action,
            tradeSlotType, tradeIndex, trade, requestedUses, givenPrices, tradeAction,
            true
        );
    }

    public TradeEvent(
            InventoryClickEvent originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface,
            ItemStack cursorItem, ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action,
            TradeSlotType tradeSlotType,
            int tradeIndex, Trade trade, int requestedUses, List<ItemStack> givenPrices, TradeAction tradeAction,
            boolean cancellable
    ) {
        super(originalEvent, entity, inventory, tradeInterface, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action, tradeSlotType, cancellable);

        //

        this.tradeIndex = tradeIndex;
        this.trade = trade;
        this.requestedUses = requestedUses;
        this.givenPrices = givenPrices;
        this.tradeAction = tradeAction;
    }

    //

    public int getTradeIndex() { return this.tradeIndex; }

    public Trade getTrade() { return this.trade; }

    public int getRequestedUses() { return this.requestedUses; }

    public List<ItemStack> getGivenPrices() { return this.givenPrices; }

    public TradeAction getTradeAction() { return this.tradeAction; }

}
