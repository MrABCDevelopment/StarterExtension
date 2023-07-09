package me.dreamdevs.github.starterextension;

import lombok.Getter;
import me.dreamdevs.github.randomlootchest.api.extensions.Extension;
import me.dreamdevs.github.starterextension.listeners.PlayerListeners;
import me.dreamdevs.github.starterextension.managers.StarterManager;

@Getter
public class StarterExtensionMain extends Extension {

    private @Getter static StarterExtensionMain instance;
    private StarterManager starterManager;

    @Override
    public void onExtensionEnable() {
        instance = this;
        saveDefaultConfig();
        this.starterManager = new StarterManager();
        registerListener(new PlayerListeners());
    }

    @Override
    public void onExtensionDisable() {
        saveConfig();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        this.starterManager.loadConfig();
    }
}