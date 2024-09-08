package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.MerchantInventory;

public class TradeInterfaceOpenEvent extends TradeInterfaceEvent<InventoryOpenEvent> {

    public TradeInterfaceOpenEvent(InventoryOpenEvent originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface) {
        this(originalEvent, entity, inventory, tradeInterface, true);
    }

    public TradeInterfaceOpenEvent(InventoryOpenEvent originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface, boolean cancellable) {
        super(originalEvent, entity, inventory, tradeInterface, cancellable);
    }

}
