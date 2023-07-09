package me.dreamdevs.github.starterextension.managers;

import lombok.Getter;
import lombok.NonNull;
import me.dreamdevs.github.randomlootchest.utils.ColourUtil;
import me.dreamdevs.github.randomlootchest.utils.Util;
import me.dreamdevs.github.starterextension.StarterExtensionMain;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StarterManager {

    private @Getter ItemStack[] itemStacks;
    private @Getter ItemStack[] armorItems;
    private String text;

    public StarterManager() {
        loadConfig();
    }

    public void loadConfig() {
        FileConfiguration config = StarterExtensionMain.getInstance().getConfig();
        int amount = config.getConfigurationSection("starter-items").getKeys(false).stream().filter(s -> !config.isSet("starter-items."+s+".ArmorContent")).collect(Collectors.toList()).size();
        int amountArmor = config.getConfigurationSection("starter-items").getKeys(false).stream().filter(s -> config.isSet("starter-items."+s+".ArmorContent")).collect(Collectors.toList()).size();

        itemStacks = new ItemStack[amount];
        armorItems = new ItemStack[amountArmor];
        text = ColourUtil.colorize(config.getString("lore-id-text"));

        ConfigurationSection section = config.getConfigurationSection("starter-items");

        int x = 0;
        for(String key : section.getKeys(false)) {
            if(section.isSet(key+".ArmorContent")) {
                if(section.getString(key+".ArmorContent").equalsIgnoreCase("boots")) {
                    armorItems[0] = parsedItem(key, section);
                    continue;
                }
                if(section.getString(key+".ArmorContent").equalsIgnoreCase("leggings")) {
                    armorItems[1] = parsedItem(key, section);
                    continue;
                }
                if(section.getString(key+".ArmorContent").equalsIgnoreCase("chestplate")) {
                    armorItems[2] = parsedItem(key, section);
                    continue;
                }
                if(section.getString(key+".ArmorContent").equalsIgnoreCase("helmet")) {
                    armorItems[3] = parsedItem(key, section);
                    continue;
                }
            } else {
                itemStacks[x] = parsedItem(key, section);
                x++;
            }
        }

        Util.sendPluginMessage("&aLoaded "+x+" items!");
    }

    public void loadItems(Player player) {
        player.getInventory().setArmorContents(armorItems);
        player.getInventory().addItem(itemStacks);
        player.updateInventory();
    }

    public boolean isStarterItem(ItemStack itemStack) {
        return itemStack.getItemMeta().getLore() != null && itemStack.getItemMeta().getLore().contains(text);
    }

    private ItemStack parsedItem(@NonNull String key, ConfigurationSection section) {
        try {
            ItemStack itemStack = new ItemStack(Material.getMaterial(section.getString(key+".Material", "STONE").toUpperCase()), section.getInt(key+".Amount", 1));
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(section.isSet(key+".DisplayName"))
                itemMeta.setDisplayName(ColourUtil.colorize(section.getString(key+".DisplayName")));
            if(section.isSet(key+".DisplayLore"))
                itemMeta.setLore(ColourUtil.colouredLore(section.getStringList(key+".DisplayLore")));

            if(section.isSet(key+".Unbreakable"))
                itemMeta.setUnbreakable(section.getBoolean(key+".Unbreakable"));

            itemStack.setItemMeta(itemMeta);

            if(section.getConfigurationSection(key+".Enchantments") != null) {
                Map<String, Integer> enchantments = new HashMap<>();
                ConfigurationSection enchantmentSection = section.getConfigurationSection(key+".Enchantments");
                for(String enchantment : enchantmentSection.getKeys(false))
                    enchantments.put(enchantment.toUpperCase(), enchantmentSection.getInt(enchantment, 1));

                for(Map.Entry<String, Integer> entry : enchantments.entrySet()) {
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(entry.getKey().toUpperCase()), entry.getValue());
                }
            }
            return itemStack;
        } catch (Exception e) {
            e.printStackTrace();
            Util.sendPluginMessage("Error while parsing an item!");
            return null;
        }
    }

}