package me.gamemode.net.premium;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.gamemode.net.Core;
import me.gamemode.net.premium.RankList.ranks;
import me.gamemode.net.sql.SQLConnection;
import me.gamemode.net.sql.SQLUtil;
import me.gamemode.net.utils.Common;
import me.gamemode.net.utils.Permission;

public class Ranks implements Listener {

	private static SQLConnection connection = Core.getDB();

	private static Set<String> Groups = new HashSet<>();

	public static void setRank(Player player, OfflinePlayer target, ranks rank)
			throws SQLException, ClassNotFoundException {

		String group = rank.toString().toUpperCase().replaceAll("PLUS", "+");
		if (Groups.contains(group)) {
			SQLUtil.update(connection, "players", "rank", group,
					new SQLUtil.Where(new SQLUtil.WhereVar("name", target.getName().toUpperCase()).getWhere()));
		} else {
			Common.tell(player, "&cRank not found.");
			return;
		}
	}

	public static void removeRank(Player player) throws SQLException, ClassNotFoundException {
		SQLUtil.update(connection, "players", "rank", "MEMBER",
				new SQLUtil.Where(new SQLUtil.WhereVar("name", player.getName().toUpperCase()).getWhere()));
	}

	public static void registerRanks() {
		Groups.add("MEMBER");
		Groups.add("VIP");
		Groups.add("VIP+");
		Groups.add("MVP");
		Groups.add("MVP+");
		Groups.add("ULTRA");
		Groups.add("BUILDER");
		Groups.add("DEVELOPER");
		Groups.add("TRAINEE");
		Groups.add("MOD");
		Groups.add("MOD+");
		Groups.add("ADMIN");
	}

	public static Set<String> getGroups() {
		return Groups;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws SQLException, ClassNotFoundException {
		if (!e.getPlayer().hasPlayedBefore()) {
			connection.executeSQL("INSERT INTO players(name, rank) VALUES ('" + e.getPlayer().getName().toUpperCase()
					+ "','MEMBER')");
		}
		Permission.setupPermissions(e.getPlayer());
	}

}
