package mc.compendium.chestinterface.components.configurations;

import org.bukkit.Material;

import java.util.List;

public class ChestIconConfig extends BasicInterfaceConfig {

    private Material material;
    private int count;
    private List<String> description;
    private boolean enchanted;

    //

    public ChestIconConfig(Material material, String name, int count, List<String> description, boolean enchanted) {
        super(name, false);

        this.setMaterial(material);
        this.setName(name);
        this.setCount(count);
        this.setDescription(description);
        this.setEnchanted(enchanted);
    }

    //

    public Material material() { return this.material; }

    public int count() { return this.count; }

    public List<String> description() { return this.description; }

    public boolean enchanted() { return this.enchanted; }

    //

    public Material setMaterial(Material material) { return this.material = material; }

    public int setCount(int count) { return this.count = count; }

    public List<String> setDescription(List<String> description) { return this.description = description; }

    public boolean setEnchanted(boolean enchanted) { return this.enchanted = enchanted; }

}
