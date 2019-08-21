package me.gamemode.net.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import me.gamemode.net.Core;
import me.gamemode.net.config.SimpleConfig;
import me.gamemode.net.sql.SQLUtil;

public class Permission {

	static SimpleConfig groups = new SimpleConfig("groups.yml");

	public static void setupPermissions(Player p) throws SQLException, ClassNotFoundException {
		ResultSet set = SQLUtil.query(Core.getDB(), "players", "rank",
				new SQLUtil.Where(new SQLUtil.WhereVar("name", p.getName().toUpperCase()).getWhere()));
		set.next();
		String rank = set.getString("rank");

		List<String> permissionList = groups.getStringList(rank.toLowerCase() + ".permissions");

		for (String perm : permissionList) {
			PermissionAttachment attachment = p.addAttachment(Core.getInstance(), perm, true);
		}
	}

}