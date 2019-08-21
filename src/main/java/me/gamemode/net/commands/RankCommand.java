package me.gamemode.net.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.gamemode.net.premium.RankList.ranks;
import me.gamemode.net.premium.Ranks;
import me.gamemode.net.utils.Common;

public class RankCommand extends PlayerCommand {

	public RankCommand() {
		super("rank");
	}

	ranks rank;

	public void getRank(String type) {
		try {
			switch (type.toUpperCase()) {
				case "ADMIN":
					rank = ranks.ADMIN;
					break;
				case "MOD+":
					rank = ranks.MODPLUS;
					break;
				case "MOD":
					rank = ranks.MOD;
					break;
				case "TRAINEE":
					rank = ranks.TRAINEE;
					break;
				case "DEVELOPER":
					rank = ranks.DEVELOPER;
					break;
				case "BUILDER":
					rank = ranks.BUILDER;
					break;
				case "ULTRA":
					rank = ranks.ULTRA;
					break;
				case "MVP+":
					rank = ranks.MVPPLUS;
					break;
				case "MVP":
					rank = ranks.MVP;
					break;
				case "VIP+":
					rank = ranks.VIPPLUS;
					break;
				case "VIP":
					rank = ranks.VIP;
					break;
				case "MEMBER":
					rank = ranks.MEMBER;
					break;
				default:
					rank = ranks.NONE;
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void run(Player player, String[] args) {
		if (player.hasPermission("core.admin")) {
			if (args.length >= 0) {
				Common.tell(player, "&cIncorrect Usage.");
			}
			if (args[0].equalsIgnoreCase("add")) {
				if (args.length >= 1) {
					Common.tell(player, "&cPlease specify a rank.");
					return;
				} else if (args.length >= 2) {
					Common.tell(player, "&cPlease specify a user.");
					return;
				}
				if (!Ranks.getGroups().contains(rank.toString().toUpperCase())) {
					Common.tell(player, "&cRank not found.");
					return;
				}
				getRank(args[1]);
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
				if (rank.toString().toUpperCase() == "NONE") {
					Common.tell(player, "&cRank not found.");
					return;
				}
				if (target == null) {
					Common.tell(player, "&cUser not found.");
					return;
				}
				if (Ranks.getGroups().contains(rank.toString().toUpperCase())) {
					try {
						Ranks.setRank(player, target, rank);
						Common.tell(player,
								"&e" + target.getName() + "&7 has been added to &e"
										+ rank.toString().toLowerCase().substring(0, 1).toUpperCase()
										+ rank.toString().toLowerCase().substring(1));
						return;
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						Common.tell(player, "&cAn error occured.");
						return;
					}
				}
			}
		} else {
			Common.tell(player, "&cCommand not found.");
		}
	}

}
