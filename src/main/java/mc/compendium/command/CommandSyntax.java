package mc.compendium.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class CommandSyntax {

    public static class ParameterKeyAlreadyUsedException extends RuntimeException {
        public ParameterKeyAlreadyUsedException() { super(); }
        public ParameterKeyAlreadyUsedException(String message) { super(message); }
        public ParameterKeyAlreadyUsedException(String message, Throwable cause) { super(message, cause); }
        public ParameterKeyAlreadyUsedException(Throwable cause) { super(cause); }
    }

    //

    public record Parameter(String key, String pattern, CommandCompleter completer) {

        public static final String DEFAULT_PATTERN = ".+";

        //

        public static Parameter of(String key) { return of(key, DEFAULT_PATTERN, null); }

        public static Parameter of(String key, String pattern) { return new Parameter(key, pattern, null); }

        public static Parameter of(String key, CommandCompleter completer) { return new Parameter(key, DEFAULT_PATTERN, completer); }

        public static Parameter of(String key, String pattern, CommandCompleter completer) { return new Parameter(key, pattern, completer); }

    }

    //

    private final Map<Integer, Parameter> parameterMap = new HashMap<>();
    private CommandAction action = null;

    //

    public boolean hasParameter(Parameter parameter) {
        return this.parameterMap.containsValue(parameter);
    }

    public boolean hasParameter(String key) {
        return this.parameterMap.values().stream().anyMatch(parameter -> parameter.key().equals(key));
    }

    public Parameter getParameter(int index) { return this.parameterMap.get(index); }

    public CommandSyntax setParameter(int index, String key) throws ParameterKeyAlreadyUsedException {
        return this.setParameter(index, Parameter.of(key));
    }

    public CommandSyntax setParameter(int index, String key, String pattern) throws ParameterKeyAlreadyUsedException {
        return this.setParameter(index, Parameter.of(key, pattern));
    }

    public CommandSyntax setParameter(int index, String key, CommandCompleter completer) throws ParameterKeyAlreadyUsedException {
        return this.setParameter(index, Parameter.of(key, completer));
    }

    public CommandSyntax setParameter(int index, String key, String pattern, CommandCompleter completer) throws ParameterKeyAlreadyUsedException {
        return this.setParameter(index, Parameter.of(key, pattern, completer));
    }

    public CommandSyntax setParameter(int index, Parameter parameter) throws ParameterKeyAlreadyUsedException {
        if(this.hasParameter(parameter.key())) throw new ParameterKeyAlreadyUsedException();

        this.removeParameter(index);
        this.parameterMap.put(index, parameter);
        return this;
    }

    public CommandSyntax removeParameter(int index) {
        this.parameterMap.remove(index);
        return this;
    }

    //

    public CommandSyntax setAction(CommandAction action) {
        this.action = action;
        return this;
    }

    public CommandAction getAction() { return this.action; }

    //

    public boolean handle(CommandSender sender, Command cmd, String label, String[] args, String[] labelPath) {
        Set<Integer> indexSet = this.parameterMap.keySet();
        if(!indexSet.isEmpty() && indexSet.size() != args.length) return false;

        List<Integer> indexes = indexSet.stream().toList();
        Map<String, String> argumentMap = new HashMap<>();

        boolean isMatching = true;
        for(int i = 0; i < args.length && isMatching; ++i) {
            Parameter parameter = this.getParameter(indexes.get(i));
            String argumentValue = args[i];

            if(!Pattern.matches(parameter.pattern(), argumentValue)) isMatching = false;
            else argumentMap.put(parameter.key(), argumentValue);
        }

        return isMatching && this.getAction().action(sender, cmd, label, argumentMap, labelPath);
    }

    //

    public String generateSyntaxMessage(String[] labelPath) {
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append("/");
        for(String label : labelPath) messageBuilder.append(label + " ");

        for(int index : this.parameterMap.keySet()) {
            Parameter parameter = this.parameterMap.get(index);
            messageBuilder.append("<" + parameter.key() + ": " + parameter.pattern() + "> ");
        }

        return messageBuilder.toString();
    }

}
