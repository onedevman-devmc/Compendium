package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.components.configurations.TradeInterfaceConfig;
import mc.compendium.chestinterface.events.InterfaceEventListener;
import mc.compendium.chestinterface.events.TradeEvent;
import mc.compendium.chestinterface.events.TradeInterfaceEvent;
import mc.compendium.events.EventHandler;
import mc.compendium.events.EventHandlerPriority;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;

import java.util.*;

public class TradeInterface<
    TradeType extends Trade
> extends ConfigurableInterface<
    TradeInterfaceConfig,
    Merchant,
    MerchantInventory,
    TradeInterfaceEvent<?>
> implements InterfaceEventListener {

    private static final Map<Merchant, TradeInterface<?>> MERCHANT_REGISTRY = new WeakHashMap<>();

    public static TradeInterface<?> getAssociated(Merchant merchant) {
        return MERCHANT_REGISTRY.get(merchant);
    }

    //

    @EventHandler(priority = EventHandlerPriority.LOWEST)
    public void onTrade(TradeEvent event) {
        Trade trade = event.getTrade();
        List<ItemStack> givenPrices = event.getGivenPrices();
        TradeAction tradeAction = event.getTradeAction();

        if(TradeAction.UNKNOWN.equals(tradeAction)) return;

        Map<TradeSlotType, ItemStack> tradeResult = trade.trade(givenPrices.get(0), givenPrices.get(1), event.getRequestedUses());

        Set<TradeSlotType> tradeSlotTypesToUpdate = Set.of();

        if(TradeAction.SINGLE_TRADE.equals(tradeAction)) {
            tradeSlotTypesToUpdate = Set.of(TradeSlotType.RESULT);
        }
        else if(TradeAction.MULTIPLE_TRADE.equals(tradeAction)) {
            event.setCancelled(true);

            tradeSlotTypesToUpdate = Set.of(TradeSlotType.FIRST_INGREDIENT, TradeSlotType.SECOND_INGREDIENT);

            ItemStack resultItem = tradeResult.get(TradeSlotType.RESULT);

            int previousMaxStackSize = event.getPlayer().getInventory().getMaxStackSize();
            event.getPlayer().getInventory().setMaxStackSize(resultItem.getMaxStackSize());

            event.getPlayer().getInventory().addItem(resultItem);
            event.getPlayer().getInventory().setMaxStackSize(previousMaxStackSize);
        }

        for(TradeSlotType tradeSlotTypeToUpdate : tradeSlotTypesToUpdate)
            event.getInventory().setItem(tradeSlotTypeToUpdate.getSlot(), tradeResult.get(tradeSlotTypeToUpdate));
    }

    //

    private List<TradeType> tradeList = Collections.synchronizedList(new ArrayList<>());

    //

    public TradeInterface(TradeInterfaceConfig config) {
        super(config, (Class<TradeInterfaceEvent<?>>) ((Class<?>) TradeInterfaceEvent.class));

        //

        this.addListener(this);
    }

    //

    public List<TradeType> getTradeList() { return tradeList; }

    //

    public void setTradeList(List<TradeType> tradeList) { this.tradeList = Collections.synchronizedList(tradeList); }

    //

    @Override
    public Merchant toBukkit() {
        Merchant merchant = Bukkit.createMerchant(this.config().name());

        merchant.setRecipes(this.getTradeList().stream().map(Trade::asRecipe).toList());

        MERCHANT_REGISTRY.put(merchant, this);
        return merchant;
    }

}
