package mc.compendium.chestinterface.components.configurations;

import mc.compendium.chestinterface.components.ChestIcon;

import java.util.List;

public class AnvilInputConfig extends BasicInterfaceConfig {

    private List<String> description;
    private String defaultValue;
    private ChestIcon inputIcon;

    //

    public AnvilInputConfig(String title, List<String> description, String defaultValue, boolean silent) {
        super(title, silent);

        this.setDescription(description);
        this.setDefaultValue(defaultValue);
    }

    //

    public List<String> description() { return this.description; }

    public String defaultValue() { return this.defaultValue; }

    public ChestIcon inputIcon() { return this.inputIcon; }

    //

    public List<String> setDescription(List<String> description) { return this.description = description; }

    public String setDefaultValue(String defaultValue) { return this.defaultValue = defaultValue; }

    public ChestIcon setInputIcon(ChestIcon icon) { return this.inputIcon = icon; }

}
