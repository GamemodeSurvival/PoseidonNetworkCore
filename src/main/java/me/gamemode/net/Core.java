package me.gamemode.net;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.gamemode.net.config.SimpleConfig;
import me.gamemode.net.premium.Ranks;
import me.gamemode.net.sql.SQLConnection;
import me.gamemode.net.utils.Common;

public class Core extends JavaPlugin implements Listener {

	public static Core instance;

	public static boolean placeholdersFound = false;
	public static SQLConnection db;
	public static boolean connection = false;

	public static SQLConnection getDB() {
		return db;
	}

	@Override
	public void onEnable() {
		getLogger().info("&bLobbyCore &fhas been enabled!");

		instance = this;

		initialize();

		Ranks.registerRanks();

		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {

			Bukkit.getPluginManager().registerEvents(this, this);

		} else {
			Common.log("&cPlaceholderAPI was not found. Placeholders may not be used.");
			placeholdersFound = true;
		}

	}

	private void initialize() {
		final SimpleConfig config = new SimpleConfig("config.yml");
		if (!config.getBoolean("SQL")) {
			db = new SQLConnection(config.getString("MySQL.hostname"), config.getInt("MySQL.port"),
					config.getString("MySQL.database"), config.getString("MySQL.username"),
					config.getString("MySQL.password"));
			Common.log("&9Connecting to MySQL...");
			try {
				getDB().openConnection();
				connection = true;
			} catch (Exception e) {
				Common.log("&fCould not connect to MySQL: &c" + e);
			}
		}
	}

	public Core() {
		instance = this;
	}

	public static Core getInstance() {
		return instance;
	}
}
