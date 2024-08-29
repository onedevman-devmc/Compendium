package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.components.configurations.TradeInterfaceConfig;
import mc.compendium.chestinterface.events.InterfaceEventListener;
import mc.compendium.chestinterface.events.TradeEvent;
import mc.compendium.chestinterface.events.TradeInterfaceEvent;
import mc.compendium.chestinterface.events.TradeSelectEvent;
import mc.compendium.events.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class TradeInterface extends ConfigurableInterface<TradeInterfaceConfig, Merchant, MerchantInventory, TradeInterfaceEvent<?>> {

    private static final Map<Merchant, TradeInterface> MERCHANT_REGISTRY = new WeakHashMap<>();

    public static TradeInterface getAssociated(Merchant merchant) {
        return MERCHANT_REGISTRY.get(merchant);
    }

    //

    private final List<MerchantRecipe> recipes = new ArrayList<>();

    //

    public TradeInterface(TradeInterfaceConfig config) {
        super(config, (Class<TradeInterfaceEvent<?>>) ((Class<?>) TradeInterfaceEvent.class));
    }

    //

    public List<MerchantRecipe> recipes() { return recipes; }

    //

    @Override
    public Merchant toBukkit() {
        Merchant merchant = Bukkit.createMerchant(this.config().name());

        merchant.setRecipes(recipes);

        MERCHANT_REGISTRY.put(merchant, this);
        return merchant;
    }

}
