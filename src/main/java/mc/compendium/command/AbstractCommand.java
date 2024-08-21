package mc.compendium.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    public static class SyntaxAlreadyRegisteredException extends RuntimeException {
        public SyntaxAlreadyRegisteredException() { super(); }
        public SyntaxAlreadyRegisteredException(String message) { super(message); }
        public SyntaxAlreadyRegisteredException(String message, Throwable cause) { super(message, cause); }
        public SyntaxAlreadyRegisteredException(Throwable cause) { super(cause); }
    }

    public static class SyntaxNotRegisteredException extends RuntimeException {
        public SyntaxNotRegisteredException() { super(); }
        public SyntaxNotRegisteredException(String message) { super(message); }
        public SyntaxNotRegisteredException(String message, Throwable cause) { super(message, cause); }
        public SyntaxNotRegisteredException(Throwable cause) { super(cause); }
    }

    //

    public static String[] extractLabelPath(String rawLabel) { return rawLabel.split(" "); }

    public static String extractLastLabel(String rawLabel) { return rawLabel.substring(Math.max(0, rawLabel.lastIndexOf(' '))); }

    //

    private final String label;
    private final Set<CommandOption> options;

    private CommandLocaleRegistry locales = new CommandLocaleRegistry();

    private final List<String> permissions = new ArrayList<>();
    private final List<CommandSyntax> syntaxes = new ArrayList<>();
    
    //    
    
    protected AbstractCommand(String label) {
        this(label, Set.of());
    }

    protected AbstractCommand(String label, Set<CommandOption> options) {
        this.label = label;
        this.options = options;
    }

    //

    public String label() { return label; };

    public Set<CommandOption> options() { return this.options; }

    public CommandLocaleRegistry locales() { return this.locales; }

    //

    public boolean isSyntaxRegistered(CommandSyntax syntax) { return this.syntaxes.contains(syntax); }

    public AbstractCommand registerSyntax(CommandSyntax syntax) throws SyntaxAlreadyRegisteredException {
        if(this.isSyntaxRegistered(syntax)) throw new SyntaxAlreadyRegisteredException();
        this.syntaxes.add(syntax);
        return this;
    }

    public AbstractCommand unregisterSyntax(CommandSyntax syntax) throws SyntaxNotRegisteredException {
        if(!this.isSyntaxRegistered(syntax)) throw new SyntaxNotRegisteredException();
        this.syntaxes.remove(syntax);
        return this;
    }

    //

    public AbstractCommand addPermission(String permission) {
        if(!this.permissions.contains(permission)) this.permissions.add(permission);
        return this;
    }

    public AbstractCommand removePermission(String permission) {
        this.permissions.remove(permission);
        return this;
    }

    public boolean checkPermissions(CommandSender sender) {
        int permissionCount = this.permissions.size();
        boolean hasPermission = false;

        for(int i = 0; i < permissionCount && !hasPermission; ++i) {
            String permission = this.permissions.get(i);
            hasPermission = sender.hasPermission(permission);
        }

        return hasPermission;
    }

    //

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String rawLabel, String[] args) {
        if(
            ( this.options().contains(CommandOption.PLAYER_SIDE_ONLY) && !(sender instanceof Player) )
            || ( this.options().contains(CommandOption.SERVER_SIDE_ONLY) && (sender instanceof Player) )
        ) return null;

        //

        if(!this.checkPermissions(sender)) return null;

        //

        String label = extractLastLabel(rawLabel);
        String[] labelPath = extractLabelPath(rawLabel);

        //

        List<String> result = new ArrayList<>();
        for(CommandSyntax syntax : this.syntaxes) {
            CommandSyntax.Parameter parameter = syntax.getParameter(args.length-1);
            CommandCompleter completer = parameter != null ? parameter.completer() : null;
            List<String> syntaxResult = completer != null ? completer.complete(sender, cmd, label, args, labelPath) : null;
            if(syntaxResult != null) result.addAll(syntaxResult);
        }

        //

        return result;
    }

    //

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String rawLabel, String[] args) {
        if(this.options().contains(CommandOption.PLAYER_SIDE_ONLY) && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is player-side only.");
            return true;
        }
        else if(this.options().contains(CommandOption.SERVER_SIDE_ONLY) && (sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is server-side only.");
            return true;
        }

        //

        if(!this.checkPermissions(sender)) {
            sender.sendMessage(this.locales().used().get(CommandLocale.DefaultLocaleKey.MISSING_PERMISSION.key()));
            return true;
        }

        //

        String label = extractLastLabel(rawLabel);
        String[] labelPath = extractLabelPath(rawLabel);

        //

        List<String> syntaxMessages = new ArrayList<>();
        int syntaxCount = this.syntaxes.size();
        boolean hasSuccessSyntax = false;
        for(int i = 0; i < syntaxCount && !hasSuccessSyntax; ++i) {
            CommandSyntax syntax = this.syntaxes.get(i);
            hasSuccessSyntax = syntax.handle(sender, cmd, label, args, labelPath);
            if(!hasSuccessSyntax) syntaxMessages.add(syntax.generateSyntaxMessage(labelPath));
        }

        //

        if(!hasSuccessSyntax && syntaxCount > 0) {
            StringBuilder syntaxErrorMessageBuilder = new StringBuilder();

            syntaxErrorMessageBuilder.append(ChatColor.RED + "Syntax error, please use one of these :");
            for(String message : syntaxMessages) syntaxErrorMessageBuilder.append("§c - ").append(message);

            sender.sendMessage(syntaxErrorMessageBuilder.toString());
        }

        //

        return true;
    }

}

