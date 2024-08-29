package mc.compendium.utils.synchronization;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Async {

    public static BukkitTask call(JavaPlugin plugin, Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static BukkitTask timeout(JavaPlugin plugin, Runnable runnable, long timeoutticks) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, timeoutticks);
    }

}
