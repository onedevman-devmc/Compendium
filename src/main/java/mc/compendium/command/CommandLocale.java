package mc.compendium.command;

import mc.compendium.types.AbstractUniMap;

public class CommandLocale extends AbstractUniMap<String, String, CommandLocale> {

    public enum DefaultLocaleKey {
        MISSING_PERMISSION("missing-permission"),
        SYNTAX_ERROR_HEADER("syntax-error-header");

        //

        private final String key;

        //

        DefaultLocaleKey(String key) {
            this.key = key;
        }

        //

        public String key() { return this.key; }

    }

    //

    public static CommandLocale getDefault() {
        return null;
    };

    //

//    public final Map<String, String> localeMap = new HashMap<>();

    //

    public CommandLocale() {

    }

    //

    @Override
    protected CommandLocale superThis() { return this; }

}
