package me.gamemode.net.utils;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.gamemode.net.Core;
import me.gamemode.net.commands.PlayerCommand;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Common {

	public static void sendTitle(Player pl, String title, String subtitle) {
		pl.sendTitle(colorize(title), colorize(subtitle), 20, 3 * 20, 10);
	}

	public static void sendBar(Player pl, String title) {
		try {
			pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colorize(title)));

		} catch (final Throwable t) {
			tell(pl, title);
		}
	}

	public static void log(String... messages) {
		for (final String message : messages)
			log(message);
	}

	public static void log(String messages) {
		tell(Bukkit.getConsoleSender(), "&f&l[&9&l" + Core.getInstance().getName() + "&f&l] " + messages);
	}

	public static void tell(CommandSender toWhom, String... messages) {
		for (String message : messages)
			tell(toWhom, message);
	}

	public static void tell(CommandSender toWhom, String message) {
		toWhom.sendMessage(colorize(message));
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void registerCommand(PlayerCommand command) {
		try {
			final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
			commandMap.register(command.getLabel(), command);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void tellList(CommandSender toWhom, List<String> stringList) {
		for (String message : stringList)
			tell(toWhom, message);

	}

	public static List<String> colorize(List<String> stringList) {
		return null;
	}

}
