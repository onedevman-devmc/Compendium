package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.MerchantInventory;

public class TradeInterfaceEvent<
    OriginalEventType
> extends InterfaceEvent<OriginalEventType, MerchantInventory, TradeInterface<?>> {

    public TradeInterfaceEvent(OriginalEventType originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface) {
        this(originalEvent, entity, inventory, tradeInterface, true);
    }

    public TradeInterfaceEvent(OriginalEventType originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface, boolean cancellable) {
        super(originalEvent, entity, inventory, tradeInterface, cancellable);
    }

}
