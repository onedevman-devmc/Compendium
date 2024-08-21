package mc.compendium.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;

public interface CommandAction {

    boolean action(CommandSender sender, Command cmd, String label, Map<String, String> argumentMap, String[] labelPath);

}
