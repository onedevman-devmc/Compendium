package mc.compendium.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandCompleter {

    List<String> complete(CommandSender sender, Command cmd, String label, String[] args, String[] labelPath);

}
