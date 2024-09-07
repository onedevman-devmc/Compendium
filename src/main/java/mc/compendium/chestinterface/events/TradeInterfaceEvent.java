package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.MerchantInventory;

public class TradeInterfaceEvent<
    BukkitEventType extends org.bukkit.event.Event
> extends InterfaceEvent<BukkitEventType, MerchantInventory, TradeInterface<?>> {

    public TradeInterfaceEvent(BukkitEventType bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface) {
        this(bukkitEvent, entity, inventory, tradeInterface, true);
    }

    public TradeInterfaceEvent(BukkitEventType bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface, boolean cancellable) {
        super(bukkitEvent, entity, inventory, tradeInterface, cancellable);
    }

}
