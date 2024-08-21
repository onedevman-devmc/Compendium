package mc.compendium.command;


import mc.compendium.types.AbstractUniMap;

public class CommandLocaleRegistry extends AbstractUniMap<String, CommandLocale, CommandLocaleRegistry> {

    private final String DEFAULT_LOCALE = "DEFAULT_" + System.currentTimeMillis();
    private String usedLocale = DEFAULT_LOCALE;

    //

    public CommandLocaleRegistry() {}

    //

    @Override
    protected CommandLocaleRegistry superThis() { return this; }

    //

    public CommandLocaleRegistry use(String key) {
        this.usedLocale = key;
        return this;
    }
    public CommandLocale used() { return this.get(usedLocale); }

    //

    public CommandLocaleRegistry setDefault(CommandLocale locale) {
        this.set(DEFAULT_LOCALE, locale);
        return this;
    }

    public CommandLocale getDefault() {
        return this.get(DEFAULT_LOCALE);
    }

    public CommandLocaleRegistry useDefault() {
        this.use(DEFAULT_LOCALE);
        return this;
    }

}
