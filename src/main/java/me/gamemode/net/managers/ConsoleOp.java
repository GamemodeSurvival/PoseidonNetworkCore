package me.gamemode.net.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.gamemode.net.utils.Common;

public class ConsoleOp extends Command {

	public ConsoleOp() {
		super("op");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.hasPermission("core.admin")) {
			Common.tell(sender, "&cCommand not found.");
			return false;
		}
		if (sender instanceof Player) {
			Common.tell(sender, "&cThis command can only be ran through console.");
			return false;
		} else {
			OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
			p.setOp(true);
			Common.log(args[0] + " has been promoted to operator.");
		}
		return false;
	}

}
