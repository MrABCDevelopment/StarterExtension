package me.dreamdevs.github.starterextension.managers;

import lombok.Getter;
import lombok.NonNull;
import me.dreamdevs.github.randomlootchest.utils.ColourUtil;
import me.dreamdevs.github.randomlootchest.utils.ItemUtil;
import me.dreamdevs.github.randomlootchest.utils.Util;
import me.dreamdevs.github.randomlootchest.utils.VersionUtil;
import me.dreamdevs.github.starterextension.StarterExtensionMain;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class StarterManager {

    private @Getter ItemStack[] itemStacks;
    private String text;

    public StarterManager() {
        load();
    }

    public void load() {
        FileConfiguration config = StarterExtensionMain.getInstance().getConfig();
        itemStacks = new ItemStack[config.getStringList("starter-items").size()];
        text = ColourUtil.colorize(config.getString("lore-id-text"));
        int x = 0;
        for(String k : config.getStringList("starter-items")) {
            String[] param = k.split(":");
            try {
                ItemStack itemStack = parsedItem(param);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if(itemMeta.getLore() == null || itemMeta.getLore().isEmpty())
                    itemMeta.setLore(Collections.singletonList(text));
                else {
                    List<String> list = itemMeta.getLore();
                    list.add(text);
                    itemMeta.setLore(list);
                }
                itemStack.setItemMeta(itemMeta);
                itemStacks[x] = itemStack;
                x++;
            } catch (NullPointerException e) {
                Util.sendPluginMessage("&cThere is an error with '"+ param[0] +"' in config.yml");
            }
        }
    }

    public void loadItems(Player player) {
        player.getInventory().addItem(itemStacks);
    }

    public boolean isStarterItem(ItemStack itemStack) {
        return itemStack.getItemMeta().getLore() != null && itemStack.getItemMeta().getLore().contains(text);
    }

    private ItemStack parsedItem(@NonNull String[] strings) {
        try {
            ItemStack itemStack = new ItemStack(Material.getMaterial(strings[0].toUpperCase()),  Integer.parseInt(strings[1]));
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(strings.length >= 3)
                itemMeta.setDisplayName(ColourUtil.colorize(strings[2]));
            if(strings.length >= 4) {
                String[] lore = strings[3].split(";");
                if (lore != null)
                    itemMeta.setLore(ColourUtil.colouredLore(lore));
            }
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        } catch (Exception e) {
            e.printStackTrace();
            Util.sendPluginMessage("Error while parsing an item!");
            return null;
        }
    }

}