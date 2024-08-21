package mc.compendium.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public abstract class AbstractMasterCommand extends AbstractCommand {

    private interface CommandSpecificHandler<R> {

        R handle(CommandSender sender, Command cmd, String label, String[] args);

    }

    //

    public static class ChildCommandAlreadyRegisteredException extends RuntimeException {
        public ChildCommandAlreadyRegisteredException() { super(); }
        public ChildCommandAlreadyRegisteredException(String message) { super(message); }
        public ChildCommandAlreadyRegisteredException(String message, Throwable cause) { super(message, cause); }
        public ChildCommandAlreadyRegisteredException(Throwable cause) { super(cause); }
    }

    public static class ChildCommandNotRegisteredException extends RuntimeException {
        public ChildCommandNotRegisteredException() { super(); }
        public ChildCommandNotRegisteredException(String message) { super(message); }
        public ChildCommandNotRegisteredException(String message, Throwable cause) { super(message, cause); }
        public ChildCommandNotRegisteredException(Throwable cause) { super(cause); }
    }

    //

    public static String extractChildCommandLabel(String[] parentArgs) {
        if(parentArgs.length == 0) throw new IllegalArgumentException("Parent arguments must have at least 1 element.");
        return parentArgs[0];
    }

    public static String[] extractChildCommandArguments(String[] parentArgs) {
        if(parentArgs.length == 0) throw new IllegalArgumentException("Parent arguments must have at least 1 element.");

        String[] childArgs = new String[parentArgs.length-1];
        System.arraycopy(parentArgs, 1, childArgs, 0, parentArgs.length - 1);

        return childArgs;
    }

    public static String genChildCommandRawLabel(String parentRawLabel, String[] parentArgs) {
        return genChildCommandRawLabel(parentRawLabel, extractChildCommandLabel(parentArgs));
    }

    public static String genChildCommandRawLabel(String parentRawLabel, String childLabel) {
        return parentRawLabel + " " + childLabel;
    }

    //

    private final Map<String, AbstractCommand> childCommandMap = new HashMap<>();

    //

    protected AbstractMasterCommand(String label) {
        super(label);
    }

    protected AbstractMasterCommand(String label, Set<CommandOption> options) {
        super(label, options);
    }

    //

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> result = new ArrayList<>();

        List<String> childResult = this.handleChildCommandTabCompletion(sender, cmd, label, args);

        if(childResult != null && !childResult.isEmpty()) result.addAll(childResult);
        else {
            List<String> selfResult = super.onTabComplete(sender, cmd, label, args);
            if (selfResult != null) result.addAll(selfResult);
        }

        return result;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!this.handleChildCommand(sender, cmd, label, args)) {
            return super.onCommand(sender, cmd, label, args);
        }

        return true;
    }

    //

    public void registerChildCommand(AbstractCommand childCommand) throws ChildCommandAlreadyRegisteredException {
        if(this.isChildCommandRegistered(childCommand)) {
            throw new ChildCommandAlreadyRegisteredException();
        }

        this.childCommandMap.put(childCommand.label(), childCommand);
    }

    public void unregisterChildCommand(AbstractCommand childCommand) throws ChildCommandNotRegisteredException {
        if(!this.isChildCommandRegistered(childCommand)) {
            throw new ChildCommandNotRegisteredException();
        }

        this.childCommandMap.remove(childCommand.label());
    }

    public boolean isChildCommandRegistered(AbstractCommand childCommand) {
        return this.childCommandMap.containsKey(childCommand.label());
    }

    //

    public AbstractCommand getRegisteredChildCommandByLabel(String label) {
        return this.childCommandMap.get(label);
    }

    //

    private List<String> handleChildCommandTabCompletion(CommandSender sender, Command cmd, String parentRawLabel, String[] parentArgs) {
        if(parentArgs.length <= 1) {
            return this.childCommandMap.values().stream().map(AbstractCommand::label).toList();
        }
        else {
            String childLabel = AbstractMasterCommand.extractChildCommandLabel(parentArgs);
            AbstractCommand childCommand = this.getRegisteredChildCommandByLabel(childLabel);

            return childCommand != null ? childCommand.onTabComplete(sender, cmd, AbstractMasterCommand.genChildCommandRawLabel(parentRawLabel, childLabel), AbstractMasterCommand.extractChildCommandArguments(parentArgs)) : null;
        }
    }

    //

    private boolean handleChildCommand(CommandSender sender, Command cmd, String parentRawLabel, String[] parentArgs) {
        if(parentArgs.length == 0) {
            return false;
        }
        else {
            String childLabel = AbstractMasterCommand.extractChildCommandLabel(parentArgs);
            AbstractCommand childCommand = this.getRegisteredChildCommandByLabel(childLabel);

            return childCommand != null && childCommand.onCommand(sender, cmd, AbstractMasterCommand.genChildCommandRawLabel(parentRawLabel, childLabel), AbstractMasterCommand.extractChildCommandArguments(parentArgs));
        }
    }

}
