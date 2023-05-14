package me.dreamdevs.github.starterextension.managers;

import lombok.Getter;
import me.dreamdevs.github.randomlootchest.utils.ColourUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class StarterItemBuilder {

    private final Material material;
    private String displayName;
    private List<String> displayLore;
    private @Getter ItemStack itemStack;

    public StarterItemBuilder(Material material) {
        this.material = material;
    }

    public StarterItemBuilder displayName(String displayName) {
        this.displayName = ColourUtil.colorize(displayName);
        return this;
    }

    public StarterItemBuilder displayLore(String... strings) {
        this.displayLore = ColourUtil.colouredLore(strings);
        return this;
    }

    public StarterItemBuilder displayLore(List<String> displayLore) {
        this.displayLore = ColourUtil.colouredLore(displayLore);
        return this;
    }

    public StarterItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public StarterItemBuilder build() {
        this.itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(displayName != null)
            itemMeta.setDisplayName(displayName);
        if(displayLore != null)
            itemMeta.setLore(displayLore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

}