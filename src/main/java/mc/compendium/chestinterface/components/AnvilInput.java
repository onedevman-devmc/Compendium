package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.bukkit.ChestInterfaceBukkitIdentifier;
import mc.compendium.chestinterface.components.configurations.AnvilInputConfig;
import mc.compendium.chestinterface.components.configurations.ChestIconConfig;
import mc.compendium.chestinterface.events.*;
import mc.compendium.events.EventHandler;
import mc.compendium.events.EventHandlerPriority;
import mc.compendium.protocol.PacketWrapper;
import mc.compendium.protocol.ProtocolManager;
import mc.compendium.protocol.events.IncomingPacketEvent;
import mc.compendium.protocol.events.ProtocolEventListener;
import mc.compendium.types.Pair;
import net.minecraft.network.protocol.game.PacketPlayInItemName;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AnvilInput extends ChestInterface<AnvilInputConfig, AnvilInputEvent<?>> implements InterfaceEventListener, ProtocolEventListener {

    public static final int DEFAULT_ANVIL_FIRST_INPUT_SLOT = 0;
    public static final int DEFAULT_ANVIL_SECOND_INPUT_SLOT = 1;
    public static final int DEFAULT_ANVIL_OUTPUT_SLOT = 2;

    //

    @EventHandler
    private void onPlayerItemRenaming(IncomingPacketEvent<PacketPlayInItemName> event) throws IllegalAccessException {
        String playerUuid = event.getPlayer().getUniqueId().toString();
        if(!this.processingInputPlayers.containsKey(playerUuid)) return;

        Pair<Inventory, String> pair = this.processingInputPlayers.remove(playerUuid);
        if(!event.getPlayer().getOpenInventory().getTopInventory().equals(pair.first())) return;

        PacketWrapper<PacketPlayInItemName> wrapper = new PacketWrapper<>(event.getPacket());
        String inputText = wrapper.all(String.class).get(0);

        this.processingInputPlayers.put(playerUuid, Pair.of(pair.first(), inputText));
    }

    //

    @EventHandler(priority = EventHandlerPriority.HIGHEST)
    public void onOpen(AnvilInputOpenEvent event) {
        this.processingInputPlayers.put(event.entity().getUniqueId().toString(), Pair.of(event.inventory(), ""));
    }

    @EventHandler(priority = EventHandlerPriority.LOWEST)
    public void onClose(AnvilInputCloseEvent event) {
        if(event.entity() instanceof Player && !this.config().silent())
            playInteractionSound((Player) event.entity());

        this.processingInputPlayers.remove(event.entity().getUniqueId().toString());
    }

    @EventHandler(priority = EventHandlerPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(AnvilInputClickEvent event) {
        if(event.entity() instanceof Player player) {
            player.setExp(player.getExp());

            if(event.slot() != AnvilInput.DEFAULT_ANVIL_OUTPUT_SLOT) return;

            if(!this.config().silent())
                playInteractionSound(player);

            Pair<Inventory, String> pair = this.processingInputPlayers.get(event.entity().getUniqueId().toString());
            String inputText = pair.last();

            AnvilInputSubmitEvent inputEvent = new AnvilInputSubmitEvent(null, event.entity(), event.inventory(), this, inputText);
            boolean accepted = this.handle(inputEvent);

            player.closeInventory();

            if(!accepted) {
                event.setCancelled(true);
                player.openInventory(this.toBukkit(inputText));
            }
        }
    }

    //

    private final Map<String, Pair<Inventory, String>> processingInputPlayers = new HashMap<>();

    //

    public AnvilInput(Plugin plugin, AnvilInputConfig config) {
        super(config, (Class<AnvilInputEvent<?>>) ((Class<?>) AnvilInputEvent.class));

        //

        this.config().setInputIcon(new ChestIcon(new ChestIconConfig(Material.NAME_TAG, this.config().defaultValue(), 1, this.config().description(), false)));

        //

        ProtocolManager protocolManager = new ProtocolManager(plugin);
        protocolManager.enable();
        protocolManager.addListener(this);

        this.addListener(this);
    }

    //

    public InterfaceEventListener onSubmit(Consumer<AnvilInputSubmitEvent> callback) {
        InterfaceEventListener result = new InterfaceEventListener() {
            @EventHandler(priority = EventHandlerPriority.LOWEST, ignoreCancelled = true)
            public void onSubmit(AnvilInputSubmitEvent event) {
                callback.accept(event);
            }
        };

        this.addListener(result);

        return result;
    }

    //

    @Override
    public Inventory toBukkit() {
        return this.toBukkit(null);
    }

    private Inventory toBukkit(String inputText) {
        Inventory inventory = Bukkit.createInventory(new ChestInterfaceBukkitIdentifier(this), InventoryType.ANVIL, this.config().name());

        inventory.setItem(0, this.config().inputIcon().toBukkit());

        if(inputText != null) {
            ItemMeta meta = inventory.getItem(0).getItemMeta();
            meta.setDisplayName("§r" + inputText);
            inventory.getItem(0).setItemMeta(meta);
        }

        return inventory;
    }

}
