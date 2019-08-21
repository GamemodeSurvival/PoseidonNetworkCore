package me.gamemode.net.utils;

import java.io.File;
import java.util.List;

import me.gamemode.net.Core;
import me.gamemode.net.config.SimpleConfig;
import me.gamemode.net.config.SimplePlayerFile;

public class RankManager {

	SimpleConfig config = new SimpleConfig("config.yml");

	private String rank;
	SimplePlayerFile dataF;

	public RankManager(String rank) {
		this.rank = rank.toLowerCase();
		dataF = new SimplePlayerFile(rank.toLowerCase() + ".yml");
		reloadData();
	}

	public void setPrefix(String prefix) {
		dataF.getConfig().set("prefix", prefix);
		dataF.saveFile();
	}

	public String getPrefix() {
		return dataF.getConfig().getString("prefix");
	}

	public void addPermission(String permission) {
		List<String> permissions = dataF.getConfig().getStringList("permissions");
		permissions.add(permission.toLowerCase());
		dataF.getConfig().set("permissions", permissions);
		dataF.saveFile();
	}

	public void removePermission(String permission) {
		List<String> permissions = dataF.getConfig().getStringList("permissions");
		permissions.remove(permission.toLowerCase());
		dataF.getConfig().set("permissions", permissions);
		dataF.saveFile();
	}

	public List getPermissions() {
		return dataF.getConfig().getStringList("permissions");
	}

	public void reloadData() {
		File dataFolder = new File(Core.getInstance().getDataFolder() + File.separator + "players" + File.separator);
		dataFolder.mkdirs();
		SimplePlayerFile dataF = new SimplePlayerFile(rank.toLowerCase() + ".yml");

		if (!dataF.getConfig().isSet("language")) {
			dataF.getConfig().set("language", config.getString("default"));
		}

		dataF.saveFile();

	}

}
