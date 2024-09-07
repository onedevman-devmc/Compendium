package mc.compendium.chestinterface;

import mc.compendium.chestinterface.bukkit.InterfaceEventBukkitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ChestInterfaceApi {

    private static final ChestInterfaceApi INSTANCE = new ChestInterfaceApi();
    public static ChestInterfaceApi getInstance() { return INSTANCE; }

    //

    private Plugin plugin;

    private final InterfaceEventBukkitListener bukkitListener = new InterfaceEventBukkitListener(this);

    private boolean enabled = false;

    //

    private ChestInterfaceApi() {}

    //

    public boolean enabled() { return enabled; }

    //

    public void enable(Plugin plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(bukkitListener, plugin);

        this.enabled = true;
    }

    //

    public void disable() {
        this.plugin = null;

        this.enabled = false;
    }

    public Plugin getPlugin() { return this.plugin; }
}
