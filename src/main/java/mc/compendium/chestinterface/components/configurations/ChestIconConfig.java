package mc.compendium.chestinterface.components.configurations;

import mc.compendium.types.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class ChestIconConfig extends BasicInterfaceConfig {

    private Material material;
    private int amount;
    private List<String> description;
    private boolean enchanted;
    private List<Pair<Enchantment, Integer>> enchantmentList;

    //

    public ChestIconConfig(Material material, String name, int amount, List<String> description, boolean enchanted) {
        super(name, false);

        this.setMaterial(material);
        this.setName(name);
        this.setAmount(amount);
        this.setDescription(description);
        this.setEnchanted(enchanted);
        this.setEnchantmentList(new ArrayList<>());
    }

    //

    public Material getMaterial() { return this.material; }

    public int getAmount() { return this.amount; }

    public List<String> getDescription() { return this.description; }

    public boolean getEnchanted() { return this.enchanted; }

    public List<Pair<Enchantment, Integer>> getEnchantmentList() { return this.enchantmentList; }

    //

    public void setMaterial(Material material) { this.material = material; }

    public void setAmount(int amount) { this.amount = amount; }

    public void setDescription(List<String> description) { this.description = description; }

    public void setEnchanted(boolean enchanted) { this.enchanted = enchanted; }

    public void setEnchantmentList(List<Pair<Enchantment, Integer>> enchantmentList) { this.enchantmentList = enchantmentList; }

}
