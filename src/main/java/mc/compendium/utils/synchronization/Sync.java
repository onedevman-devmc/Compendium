package mc.compendium.utils.synchronization;

import mc.compendium.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Sync {

    public static BukkitTask call(JavaPlugin plugin, Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static BukkitTask timeout(JavaPlugin plugin, Runnable runnable, long timeoutticks) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, timeoutticks);
    }

}
