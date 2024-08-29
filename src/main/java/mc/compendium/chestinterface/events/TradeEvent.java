package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

public class TradeEvent extends TradeInterfaceEvent<InventoryClickEvent> {

    public TradeEvent(InventoryClickEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface tradeInterface) {
        this(bukkitEvent, entity, inventory, tradeInterface, true);
    }

    public TradeEvent(InventoryClickEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface tradeInterface, boolean cancellable) {
        super(bukkitEvent, entity, inventory, tradeInterface, cancellable);
    }

    //

    public int recipeIndex() { return this.inventory().getSelectedRecipeIndex(); }

    public MerchantRecipe recipe() { return this.inventory().getSelectedRecipe(); }

}
