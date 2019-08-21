package me.gamemode.net.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamemode.net.utils.Common;

public class StaffModeCommand extends PlayerCommand implements Listener {

	public StaffModeCommand() {
		super("staff");
	}

	public static ArrayList<String> modMode = new ArrayList();
	ArrayList<Player> frozen = new ArrayList();
	private static HashMap<String, ItemStack[]> armorContents = new HashMap();
	private static HashMap<String, ItemStack[]> inventoryContents = new HashMap();
	private static HashMap<String, Integer> xplevel = new HashMap();
	private static ArrayList<String> online = new ArrayList();

	public static void saveInventory(Player p) {
		armorContents.put(p.getName(), p.getInventory().getArmorContents());
		inventoryContents.put(p.getName(), p.getInventory().getContents());
		xplevel.put(p.getName(), Integer.valueOf(p.getLevel()));
	}

	public static void loadInventory(Player p) {
		p.getInventory().clear();
		p.getInventory().setContents(inventoryContents.get(p.getName()));
		p.getInventory().setArmorContents(armorContents.get(p.getName()));
		p.setLevel(xplevel.get(p.getName()).intValue());

		inventoryContents.remove(p.getName());
		armorContents.remove(p.getName());
		xplevel.remove(p.getName());
	}

	public static void modItems(Player p) {
		Inventory inv = p.getInventory();
		inv.clear();
		ItemStack modCompass = new ItemStack(Material.COMPASS);
		ItemStack modBook = new ItemStack(Material.BOOK);
		ItemStack modFreeze = new ItemStack(Material.PACKED_ICE);
		ItemStack modTp = new ItemStack(Material.ENDER_EYE);
		ItemStack modVanish = new ItemStack(Material.FEATHER);

		ItemMeta compassMeta = modCompass.getItemMeta();
		ItemMeta bookMeta = modBook.getItemMeta();
		ItemMeta freezeMeta = modFreeze.getItemMeta();
		ItemMeta tpMeta = modTp.getItemMeta();
		ItemMeta vanishMeta = modVanish.getItemMeta();

		compassMeta.setDisplayName(Common.colorize("&eWhoosh"));
		bookMeta.setDisplayName(Common.colorize("&eInspect Player"));
		freezeMeta.setDisplayName(Common.colorize("&eFreeze Player"));
		vanishMeta.setDisplayName(Common.colorize("&eToggle Vanish"));
		tpMeta.setDisplayName(Common.colorize("&eRandom Teleporter"));

		ArrayList<String> modCompassLore = new ArrayList();
		ArrayList<String> modBookLore = new ArrayList();
		ArrayList<String> modFreezeLore = new ArrayList();
		ArrayList<String> modVanishLore = new ArrayList();
		ArrayList<String> modTpLore = new ArrayList();

		modCompassLore.add(Common.colorize("&7Used to teleport to eye location."));
		modBookLore.add(Common.colorize("&7Used to inspect a players inventory."));
		modFreezeLore.add(Common.colorize("&7Used to freeze a player."));
		modVanishLore.add(Common.colorize("&7Used to toggle vanish."));
		modTpLore.add(Common.colorize("&7Used to teleport to a random player."));

		compassMeta.setLore(modCompassLore);
		bookMeta.setLore(modBookLore);
		freezeMeta.setLore(modFreezeLore);
		vanishMeta.setLore(modVanishLore);
		tpMeta.setLore(modTpLore);

		modCompass.setItemMeta(compassMeta);
		modBook.setItemMeta(bookMeta);
		modFreeze.setItemMeta(freezeMeta);
		modTp.setItemMeta(tpMeta);
		modVanish.setItemMeta(vanishMeta);

		inv.setItem(0, modCompass);
		inv.setItem(3, modVanish);
		inv.setItem(4, modBook);
		inv.setItem(5, modFreeze);
		inv.setItem(8, modTp);
	}

	public static void enterMod(Player p) {
		modMode.add(p.getName());
		saveInventory(p);
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.setExp(0.0F);
		modItems(p);
		p.setGameMode(GameMode.CREATIVE);
		p.sendMessage(Common.colorize("&7You have &aenabled &7Moderator Mode!"));
	}

	public static void leaveMod(Player p) {
		modMode.remove(p.getName());
		p.setGameMode(GameMode.SURVIVAL);
		p.getInventory().clear();
		loadInventory(p);
		p.sendMessage("&7You have &cdisabled &7Moderator Mode!");
	}

	@EventHandler
	public void rightClick(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof Player)) {
			return;
		}
		Player player = e.getPlayer();
		Player p = (Player) e.getRightClicked();
		if ((modMode.contains(player.getName())) && ((p instanceof Player)) && ((player instanceof Player))
				&& (player.getItemInHand().getType() == Material.BOOK)) {
			player.openInventory(p.getInventory());
			player.sendMessage(
					ChatColor.GRAY + " §6§ §rNow opening the inventory of §e" + p.getName() + ChatColor.GRAY + "§r.");
		}

		else if ((modMode.contains(player.getName())) && ((p instanceof Player)) && ((player instanceof Player))
				&& (player.getItemInHand().getType() == Material.PACKED_ICE)) {
			player.sendMessage(ChatColor.GRAY + " §6§ §rAttempting to freeze §e" + p.getName() + "§r.");
			player.chat("/ss " + p.getName());
		}
	}

	@EventHandler
	public void onJoinMod(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("rank.staff")) {
			enterMod(player);
		}
	}

	@EventHandler
	public void onRightClick2(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			if ((modMode.contains(player.getName())) && (player.getItemInHand().getType() == Material.FEATHER)) {
				player.chat("/v");
			}
		}
	}

	@EventHandler
	public void onRecord(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((modMode.contains(p.getName())) && (p.getItemInHand().getType() == Material.ENDER_EYE)
				&& (e.getAction().toString().contains("RIGHT"))) {
			int randomNum = ThreadLocalRandom.current().nextInt(1, Bukkit.getServer().getOnlinePlayers().size() + 1);
			for (Player player : Bukkit.getOnlinePlayers()) {
				online.add(player.getName());
			}
			Player random = Bukkit.getPlayer(online.get(randomNum));
			if (Bukkit.getOnlinePlayers().size() == 1)
				p.sendMessage(ChatColor.RED + "§6§ §rOops, seems like there are not enough players to use this.");
			e.setCancelled(true);
			if (Bukkit.getOnlinePlayers().size() > 1) {
				if (p != random) {
					p.teleport(random);
					p.sendMessage(Common.colorize("&7You were teleported randomly to &e" + random.getName() + "&7."));
					e.setCancelled(true);
				}
				if (p == random) {
					p.sendMessage(Common.colorize("&cOops, seems like we accidently found you. Please try again!"));
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onTag(EntityDamageByEntityEvent e) {
		if ((!(e.getEntity() instanceof Player)) || (!(e.getDamager() instanceof Player))) {
			return;
		}
		Player staff = (Player) e.getDamager();
		if (modMode.contains(staff.getName())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		Player staff = e.getPlayer();
		if (modMode.contains(staff.getName())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if ((modMode.contains(p.getName())) && (p.getGameMode().equals(GameMode.CREATIVE))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if ((modMode.contains(player)) && (player.getGameMode().equals(GameMode.CREATIVE))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDropModItems(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (modMode.contains(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (((p.hasPermission("rank.staff"))) && (modMode.contains(p))) {
			leaveMod(p);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Material block = event.getBlock().getType();
		if ((modMode.contains(player.getName())) && (block == Material.LEGACY_CARPET)) {
			event.setCancelled(true);
			player.updateInventory();
		}
	}

	@EventHandler
	public void onPlayerPlacesBlock(BlockCanBuildEvent event) {
		event.setBuildable(true);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (modMode.contains(p.getName())) {
			e.setCancelled(true);
		}
	}

	@Override
	protected void run(Player player, String[] args) {
		Player sender = player;
		if ((!sender.hasPermission("core.staff"))) {
			sender.sendMessage(Common.colorize("&cCommand not found."));
			return;
		}
		if (args.length < 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("");
				return;
			}
			if (modMode.contains(sender.getName())) {
				leaveMod(sender);
				return;
			}
			enterMod(sender);
			return;
		}
		if ((!sender.hasPermission("core.staff"))) {
			sender.sendMessage(Common.colorize("&cCommand not found."));
			return;
		}
		Player t = Bukkit.getServer().getPlayer(args[0]);
		if (t == null) {
			sender.sendMessage(Common.colorize("&7Could not find player &e" + args[0].toString() + "&7."));
			return;
		}
		if (modMode.contains(t.getName())) {
			leaveMod(t);
			sender.sendMessage(Common.colorize("&7Moderator Mode has been &cdisabled &7for &e" + t.getName()));
			return;
		}
		enterMod(t);
		sender.sendMessage(Common.colorize("&7Moderator Mode has been &aenabled &7for &e" + t.getName()));
		return;
	}
}
