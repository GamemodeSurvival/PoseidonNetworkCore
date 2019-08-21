package me.gamemode.net.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import me.gamemode.net.Core;

public class SimplePlayerFile {

	private String name;
	private File configFile = null;
	private YamlConfiguration config = null;
	private Boolean configStatus = true;

	public SimplePlayerFile(String name) {
		this.name = name;
		setupConfig();
	}

	public Boolean getStatus() {
		return configStatus;
	}

	public void saveFile() {
		try {
			configFile = new File(Core.getInstance().getDataFolder() + File.separator + "players" + File.separator,
					name);
			config.save(configFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public YamlConfiguration getConfig() {
		return config;
	}

	public void ReloadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public void setupConfig() {
		if (!Core.getInstance().getDataFolder().exists()) {
			Core.getInstance().getDataFolder().mkdirs();
		}

		configFile = new File(Core.getInstance().getDataFolder() + File.separator + "players" + File.separator, name);
		config = YamlConfiguration.loadConfiguration(configFile);
		return;

	}

}
