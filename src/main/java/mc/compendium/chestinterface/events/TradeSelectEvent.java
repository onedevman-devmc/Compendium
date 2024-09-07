package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.Trade;
import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.MerchantInventory;

public class TradeSelectEvent extends TradeInterfaceEvent<org.bukkit.event.inventory.TradeSelectEvent> {

    private final int tradeIndex;
    private final Trade trade;

    //

    public TradeSelectEvent(
            org.bukkit.event.inventory.TradeSelectEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface,
            int tradeIndex, Trade trade
    ) {
        this(bukkitEvent, entity, inventory, tradeInterface, tradeIndex, trade, true);
    }

    public TradeSelectEvent(
            org.bukkit.event.inventory.TradeSelectEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface,
            int tradeIndex, Trade trade,
            boolean cancellable
    ) {
        super(bukkitEvent, entity, inventory, tradeInterface, cancellable);

        //

        this.tradeIndex = tradeIndex;
        this.trade = trade;
    }

    //

    public int getTradeIndex() { return this.tradeIndex; }

    public Trade getTrade() { return this.trade; }

}
