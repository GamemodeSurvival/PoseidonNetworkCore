package me.gamemode.net.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.gamemode.net.config.SimpleConfig;
import me.gamemode.net.utils.Common;

public class OpWhitelist implements Listener {

	private static ArrayList<String> banned = new ArrayList<>();

	SimpleConfig ops = new SimpleConfig("opwhitelist.yml");

	public void whitelist(PlayerJoinEvent e) {
		List<String> whitelist = ops.getStringList("whitelist");
		if (e.getPlayer().isOp()) {
			if (!whitelist.contains(e.getPlayer().getName().toLowerCase())) {
				BanList bans = Bukkit.getBanList(BanList.Type.NAME);
				bans.addBan(e.getPlayer().toString(), Common.colorize("&cYou are not authorized to have operator."),
						null, null);
				e.getPlayer().kickPlayer(Common.colorize("&cYou are not authorized to have operator."));
				banned.add(e.getPlayer().getName());
			}
			if (!banned.isEmpty() && whitelist.contains(e.getPlayer().getName().toLowerCase())) {
				for (String player : banned) {
					e.getPlayer().sendMessage(Common.colorize(
							"&7User &e" + player + "&7 has been banned for not being an authorized operator."));
					banned.remove(player);
				}
			}
		}
	}

}
