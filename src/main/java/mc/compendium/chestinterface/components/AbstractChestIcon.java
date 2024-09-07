package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.components.configurations.ChestIconConfig;
import mc.compendium.chestinterface.events.ChestIconClickEvent;
import mc.compendium.chestinterface.events.ChestIconEvent;
import mc.compendium.chestinterface.events.InterfaceEventListener;
import mc.compendium.events.EventHandler;
import mc.compendium.types.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractChestIcon<
    ConfigType extends ChestIconConfig
> extends ConfigurableInterface<ConfigType, ItemStack, Inventory, ChestIconEvent<?>> implements InterfaceEventListener {

    public AbstractChestIcon(ConfigType config) {
        super(config, (Class<ChestIconEvent<?>>) ((Class<?>) ChestIconEvent.class));

        //

        this.addListener(this);
    }

    //

    public InterfaceEventListener onClick(Consumer<ChestIconClickEvent> callback) {
        InterfaceEventListener result = new InterfaceEventListener() {
            @EventHandler
            public void onClick(ChestIconClickEvent event) {
                callback.accept(event);
            }
        };

        this.addListener(result);

        return result;
    }

    //

    public ItemStack toBukkit() {
        ItemStack result = new ItemStack(this.config().getMaterial(), this.config().getAmount());

        ItemMeta meta = result.getItemMeta();

        if(meta != null) {
            meta.setDisplayName("§r"+this.config().name());

            //

            List<String> finalDescription = new ArrayList<>(this.config().getDescription());
            int finalDescriptionLineCount = finalDescription.size();

            for(int i = 0; i < finalDescriptionLineCount; ++i)
                finalDescription.set(i, "§7"+finalDescription.get(i));

            meta.setLore(finalDescription);

            //

            List<Pair<Enchantment, Integer>> enchantmentList = this.config().getEnchantmentList();

            if(enchantmentList != null && !enchantmentList.isEmpty()) {
                for(Pair<Enchantment, Integer> enchantmentPair : enchantmentList)
                    meta.addEnchant(enchantmentPair.first(), enchantmentPair.last(), true);
            }
            else if(this.config().getEnchanted()) {
                meta.addEnchant(Enchantment.VANISHING_CURSE, 0, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            //

            result.setItemMeta(meta);
        }

        return result;
    }

}
