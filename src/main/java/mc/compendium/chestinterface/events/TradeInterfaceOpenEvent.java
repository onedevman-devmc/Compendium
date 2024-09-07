package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.MerchantInventory;

public class TradeInterfaceOpenEvent extends TradeInterfaceEvent<InventoryOpenEvent> {

    public TradeInterfaceOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface) {
        this(bukkitEvent, entity, inventory, tradeInterface, true);
    }

    public TradeInterfaceOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface, boolean cancellable) {
        super(bukkitEvent, entity, inventory, tradeInterface, cancellable);
    }

}
