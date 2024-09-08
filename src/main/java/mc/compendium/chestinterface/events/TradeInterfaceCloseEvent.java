package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.MerchantInventory;

public class TradeInterfaceCloseEvent extends TradeInterfaceEvent<InventoryCloseEvent> {

    public TradeInterfaceCloseEvent(InventoryCloseEvent originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface) {
        this(originalEvent, entity, inventory, tradeInterface, true);
    }

    public TradeInterfaceCloseEvent(InventoryCloseEvent originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface, boolean cancellable) {
        super(originalEvent, entity, inventory, tradeInterface, cancellable);
    }

}
