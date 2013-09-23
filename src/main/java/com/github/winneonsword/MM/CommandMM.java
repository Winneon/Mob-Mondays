package com.github.winneonsword.MM;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static com.github.winneonsword.CMAPI.API.ChatAPI.*;

public class CommandMM implements CommandExecutor{
		
	MainMM plugin;
	
	public CommandMM(MainMM instance){
	 plugin = instance;
	}
	
	boolean gameStarted = false;
	int roundcounter = 0;
	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
	
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args){
		if (sender instanceof Player){
			final Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("mm")){
				boolean useDayTimer = plugin.getConfig().getBoolean("useDayTimer");
				final String introMessage = plugin.getConfig().getString("introMessage");
				final List<String> countdownMessages = plugin.getConfig().getStringList("countdownMessages");
				int list2 = countdownMessages.size();
				final int ticks2 = list2 * 40;
				final List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
				
				if (args.length == 0){
					// Name & version number.
					sender.sendMessage(rA(introMessage + " &7Mob Mondays, v1.0-a"));
					return true;
				}
				if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")){
					// The main help menu.
					sender.sendMessage(rA(introMessage + " &7Mob Mondays Help Menu"));
					sender.sendMessage(rA("  &c/mm ? &7- Help Menu."));
					sender.sendMessage(rA("  &c/mm join &7- Join the queue for the next game."));
					sender.sendMessage(rA("  &c/mm leave &7- Leave the queue for the next game."));
					sender.sendMessage(rA("  &c/mm list &7- Show the joined player list."));
					sender.sendMessage(rA("  &c/mm start &7- Start an MM game. &8// &cStaff command only!"));
					sender.sendMessage(rA("  &c/mm reload &7- Reloads the config file. &8// &cStaff command only!"));
					sender.sendMessage(rA("&7Created by WS, original idea / VT plugin by Hugs."));
					return true;
				}
				if (args[0].equalsIgnoreCase("join")){
					// Join the queue list for any upcoming games.
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					
					if (isPlaying == true){
						sender.sendMessage(rA(introMessage + " &cYou are already selected as a class! &7To change classes, type &c/mm select <class>&7."));
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", true);
					
					sender.sendMessage(rA(introMessage + " &7Choose a class below:"));
					sender.sendMessage(rA("  &cmedic &7- Get extra hearts or give regen for a limited time."));
					sender.sendMessage(rA("  &cspirit &7- Revieve fly mode for limited time."));
					//sender.sendMessage(rA("  &cwarrior &7- Create a knockback blast that stuns opponents.."));
					//sender.sendMessage(rA("  &cinferno &7- Forge a fire burst and scorch opponents."));
					//sender.sendMessage(rA("  &croadrunner &7- Recieve speedy boots for a limted time."));
					//sender.sendMessage(rA("  &csniper &7- Headshot opponents quickly and swiftly."));
					sender.sendMessage(rA("&7Type &c/mm select <class> &7to select a class. Type &c/mm cancel &7to cancel this operation."));
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args.length == 1){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					sender.sendMessage(rA(introMessage + " &7Type &c/mm join &7to join a class!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("medic") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "medic"){
						sender.sendMessage(rA(introMessage + " &cYou are already selected as the medic class!"));
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "medic");
						
						sender.sendMessage(rA(introMessage + " &7Do you wish to change classes? Type &c/mm confirm &7to change classes to &cmedic&7. Otherwise, type &c/mm cancel &7to cancel this operation."));
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(rA(introMessage + " &cYou are not currently selecting a class! &7Type &c/mm join &7to select a class."));
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "medic");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "&7, &7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(rA(introMessage + " &7You have selected the &cmedic &7class! You are added to the join queue!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("spirit") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "spirit"){
						sender.sendMessage(rA(introMessage + " &cYou are already selected as the spirit class!"));
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "spirit");
						plugin.saveConfig();
						
						sender.sendMessage(rA(introMessage + " &7Do you wish to change classes? Type &c/mm confirm &7to change classes to &cspirit&7. Otherwise, type &c/mm cancel &7to cancel this operation."));
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(rA(introMessage + " &cYou are not currently selecting a class! &7Type &c/mm join &7to select a class."));
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "spirit");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "&7, &7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(rA(introMessage + " &7You have selected the &cspirit &7class! You are added to the join queue!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("warrior") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "warrior"){
						sender.sendMessage(rA(introMessage + " &cYou are already selected as the warrior class!"));
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "warrior");
						plugin.saveConfig();
						
						sender.sendMessage(rA(introMessage + " &7Do you wish to change classes? Type &c/mm confirm &7to change classes to &cwarrior&7. Otherwise, type &c/mm cancel &7to cancel this operation."));
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(rA(introMessage + " &cYou are not currently selecting a class! &7Type &c/mm join &7to select a class."));
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "warrior");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "&7, &7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(rA(introMessage + " &7You have selected the &cwarrior &7class! You are added to the join queue!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("inferno") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "inferno"){
						sender.sendMessage(rA(introMessage + " &cYou are already selected as the inferno class!"));
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "inferno");
						plugin.saveConfig();
						
						sender.sendMessage(rA(introMessage + " &7Do you wish to change classes? Type &c/mm confirm &7to change classes to &cinferno&7. Otherwise, type &c/mm cancel &7to cancel this operation."));
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(rA(introMessage + " &cYou are not currently selecting a class! &7Type &c/mm join &7to select a class."));
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "inferno");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "&7, &7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(rA(introMessage + " &7You have selected the &cinferno &7class! You are added to the join queue!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("roadrunner") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "roadrunner"){
						sender.sendMessage(rA(introMessage + " &cYou are already selected as the roadrunner class!"));
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "roadrunner");
						plugin.saveConfig();
						
						sender.sendMessage(rA(introMessage + " &7Do you wish to change classes? Type &c/mm confirm &7to change classes to &croadrunner&7. Otherwise, type &c/mm cancel &7to cancel this operation."));
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(rA(introMessage + " &cYou are not currently selecting a class! &7Type &c/mm join &7to select a class."));
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "roadrunner");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "&7, &7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(rA(introMessage + " &7You have selected the &croadrunner &7class! You are added to the join queue!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("sniper") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "sniper"){
						sender.sendMessage(rA(introMessage + " &cYou are already selected as the sniper class!"));
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "sniper");
						plugin.saveConfig();
						
						sender.sendMessage(rA(introMessage + " &7Do you wish to change classes? Type &c/mm confirm &7to change classes to &csniper&7. Otherwise, type &c/mm cancel &7to cancel this operation."));
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(rA(introMessage + " &cYou are not currently selecting a class! &7Type &c/mm join &7to select a class."));
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "sniper");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "&7, &7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(rA(introMessage + " &7You have selected the &csniper &7class! You are added to the join queue!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("confirm")){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					String changingClass = plugin.getConfig().getString("Users." + sender.getName() + ".changingClass");
					
					if (changingClass == "healer"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "healer");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(rA(introMessage + " &7You have changed to the &chealer &7class!"));
						return true;
					}
					if (changingClass == "spirit"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "spirit");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(rA(introMessage + " &7You have changed to the &cspirit &7class!"));
						return true;
					}
					if (changingClass == "warrior"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "warrior");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(rA(introMessage + " &7You have changed to the &cwarrior &7class!"));
						return true;
					}
					if (changingClass == "inferno"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "inferno");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(rA(introMessage + " &7You have changed to the &cinferno &7class!"));
						return true;
					}
					if (changingClass == "roadrunner"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "roadrunner");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(rA(introMessage + " &7You have changed to the &croadrunner &7class!"));
						return true;
					}
					if (changingClass == "sniper"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "sniper");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(rA(introMessage + " &7You have changed to the &csniper &7class!"));
						return true;
					}
					sender.sendMessage(rA(introMessage + " &cYou are not currently changing a class!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("cancel")){
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					if (choosingClass == true){
						plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
						sender.sendMessage(rA(introMessage + " &7You have canceled the operation involving joining the queue."));
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
					sender.sendMessage(rA(introMessage + " &7You have canceled the operation involving changing classes!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("leave")){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					mmPlayers.remove(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					String displayName = plugin.getConfig().getString("Users." + sender.getName() + ".displayName");
					plugin.getConfig().set("listDisplay", listDisplay.replace("&7, &7" + displayName, ""));
					plugin.getConfig().set("Users." + sender.getName(), null);
					plugin.saveConfig();
					sender.sendMessage(rA(introMessage + " &7You have been removed from the queue list!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("reload")){
					plugin.reloadConfig();
					sender.sendMessage(rA(introMessage + " &7Successfully reloaded the config file."));
					return true;
				}
				if (args[0].equalsIgnoreCase("set")){
					plugin.getConfig().set("arenaWorld" + ".world", player.getLocation().getWorld().getName());
					plugin.getConfig().set("arenaWorld" + ".x", player.getLocation().getBlockX());
					plugin.getConfig().set("arenaWorld" + ".y", player.getLocation().getBlockY());
					plugin.getConfig().set("arenaWorld" + ".z", player.getLocation().getBlockZ());
					plugin.getConfig().set("arenaSet", true);
					plugin.saveConfig();
					sender.sendMessage(rA(introMessage + " &7Successfully set the center of the arena."));
					return true;
				}
				if (args[0].equalsIgnoreCase("commands")){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					
					return true;
				}
				if (args[0].equalsIgnoreCase("butcher")){
					if (args.length == 2){
						try {
							Double.parseDouble(args[1]);
						} catch (NumberFormatException e){
							sender.sendMessage(rA(introMessage + " &cYou did not enter a number for the radius!"));
							return true;
						}
						double radius = Double.parseDouble(args[1]);
						for (Entity entities : player.getNearbyEntities(radius, player.getWorld().getMaxHeight() * 2, radius)){
							if (entities instanceof Monster){
								((Monster) entities).remove();
								int butcher = plugin.getConfig().getInt("butcher");
								plugin.getConfig().set("butcher", butcher + 1);
							}
							plugin.saveConfig();
						}
						int butcher = plugin.getConfig().getInt("butcher");
						sender.sendMessage(rA(introMessage + " &7Successfully killed &c" + butcher + " &7hostile mobs."));
						plugin.getConfig().set("butcher", null);
						plugin.saveConfig();
						return true;
					}
					for (Entity entities : player.getWorld().getEntities()){
						if (entities instanceof Monster){
							((Monster) entities).remove();
						}
					}
					sender.sendMessage(rA(introMessage + " &7Successfully killed all hostile mobs."));
					return true;
				}
				if (args[0].equalsIgnoreCase("list")){
					String listDisplay = plugin.getConfig().getString("listDisplay");
					
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					if (listDisplay.equals("")){
						sender.sendMessage(rA(introMessage + " &cThere are no players joined!"));
						return true;
					}
					plugin.getConfig().set("listDisplay", listDisplay.replaceFirst("&7,", ""));
					String listDisplay2 = plugin.getConfig().getString("listDisplay");
					sender.sendMessage(rA(introMessage + " &bPlayers Joined:" + listDisplay2 + " &e(&c" + mmPlayers.size() + "&e)"));
					plugin.getConfig().set("listDisplay", listDisplay);
					plugin.saveConfig();
					return true;
				}
				if (args[0].equalsIgnoreCase("stats")){
					boolean gameStarted = plugin.getConfig().getBoolean("gameStarted");
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					if (gameStarted != true){
						sender.sendMessage(rA(introMessage + " &cA game is not occuring at the moment!"));
						return true;
					}
					String Class = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					String currentClass = StringUtils.capitalize(Class);
					int shards = plugin.getConfig().getInt("Users." + sender.getName() + ".shards");
					int mobKills = plugin.getConfig().getInt("Users." + sender.getName() + ".mobKills");
					sender.sendMessage(rA(introMessage + " &7Current Stats:"));
					sender.sendMessage(rA("  &7Class: &c" + currentClass));
					sender.sendMessage(rA("  &7Shards Deposited: &c" + shards));
					sender.sendMessage(rA("  &7Mob Kills: &c" + mobKills));
					return true;
				}
				if (args[0].equalsIgnoreCase("commands")){
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					if (currentClass == "medic"){
						sender.sendMessage(rA(introMessage + " &cThis is still a WIP! Sowwy! :("));
					}
					if (currentClass == "spirit"){
						
					}
					if (currentClass == "warrior"){
						
					}
					if (currentClass == "inferno"){
						
					}
					if (currentClass == "roadrunner"){
						
					}
					if (currentClass == "sniper"){
						
					}
				}
				if (args[0].equalsIgnoreCase("start")){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(rA(introMessage + " &cIt is not Monday! All MM commands are disabled until it is Monday!"));
							return true;
						}
					}
					if (player.hasPermission("mm.start")){
						boolean gameStarted = plugin.getConfig().getBoolean("gameStarted");
						
						if (gameStarted == false){
							for (final World world : Bukkit.getWorlds()){
								boolean arenaSet = plugin.getConfig().getBoolean("arenaSet");
								
								if (arenaSet != true){
									sender.sendMessage(rA(introMessage + " &cYou have not set an arena! &7Go to the center of your arena and type &c/mm set&7!"));
									return true;
								}
								
								List<String> welcomeMessages = plugin.getConfig().getStringList("welcomeMessages");
								
								int list1 = welcomeMessages.size();
								final int ticks = list1 * 100;
								
								int minSize = plugin.getConfig().getInt("minSize");
								if (mmPlayers.size() < minSize){
									sender.sendMessage(rA(introMessage + " &cThere is not " + minSize + " or more players currently in the player queue!"));
									return true;
								}
								plugin.getConfig().set("gameStarted", true);
								plugin.saveConfig();
								clearInventory();
								distributeItems();
								giveDiscs();
								new Task(this.plugin, welcomeMessages).run();
								
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
										new Runnable(){	public void run(){	
											new Task2(plugin, countdownMessages).run();
											Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
													new Runnable(){ public void run(){
														World arenaWorld = Bukkit.getWorld(plugin.getConfig().getString("arenaWorld" + ".world"));
														int x = plugin.getConfig().getInt("arenaWorld" + ".x");
														int y = plugin.getConfig().getInt("arenaWorld" + ".y");
														int z = plugin.getConfig().getInt("arenaWorld" + ".z");
														
														sendInfoMessage(" &bMob Mondays has begun! Good luck!");
														player.setHealth(20);
														player.setFoodLevel(20);
														player.setSaturation(200);
														world.setFullTime(18000);
														world.setGameRuleValue("doDaylightCycle", "false");
														plugin.getConfig().set("roundNumber", 1);
														plugin.saveConfig();
														for (int i = 0; i < 40; i++){
															world.spawnEntity(new Location(arenaWorld, x, y, z), org.bukkit.entity.EntityType.ZOMBIE);
														}
													}}, ticks2);
										}}, ticks);
								return true;
								
							}
						}
						if (gameStarted == true){
							sender.sendMessage(rA(introMessage + " &cThere is already a game started!"));
							// Everything after this is for testing purposes only, and will be removed for the final release.
							getInventory();
							plugin.getConfig().set("gameStarted", false);
							plugin.getConfig().set("zombiesKilled", null);
							plugin.getConfig().set("skeliesKilled", null);
							plugin.getConfig().set("spidersKilled", null);
							plugin.getConfig().set("listDisplay", "");
							plugin.getConfig().set("amount", null);
							plugin.saveConfig();
							for (Entity entities : player.getWorld().getEntities()){
								if (entities instanceof Monster){
									((Monster) entities).remove();
								}
							}
							for (World world : Bukkit.getWorlds()){
								String rule = "doDaylightCycle";
								String value = "true";
								
								world.setFullTime(6000);
								world.setGameRuleValue(rule, value);
							}
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
									new Runnable(){ public void run(){
										plugin.getConfig().set("Users", null);
										plugin.getConfig().set("MM.players", null);
										plugin.saveConfig();
									}}, 60);
							return true;
						}
					}
				}
				if (args[0].equalsIgnoreCase("round")){
					if (!(sender.hasPermission("mm.round"))){
						sender.sendMessage(rA(introMessage + " &cYou do not have permission to advance the round!"));
					}
					boolean gameStarted = plugin.getConfig().getBoolean("gameStarted");
					if (gameStarted != true){
						sender.sendMessage(rA(introMessage + " &cThere is not a game occuring! &7Type &c/mm start &7to start a game."));
						return true;
					}
					boolean round1Complete = plugin.getConfig().getBoolean("round1Complete");
					boolean round2Complete = plugin.getConfig().getBoolean("round2Complete");
					boolean round3Complete = plugin.getConfig().getBoolean("round3Complete");
					boolean round4Complete = plugin.getConfig().getBoolean("round4Complete");
					boolean round5Complete = plugin.getConfig().getBoolean("round5Complete");
					final World arenaWorld = Bukkit.getWorld(plugin.getConfig().getString("arenaWorld" + ".world"));
					final int x = plugin.getConfig().getInt("arenaWorld" + ".x");
					final int y = plugin.getConfig().getInt("arenaWorld" + ".y");
					final int z = plugin.getConfig().getInt("arenaWorld" + ".z");
					if (round1Complete == true){
						sendInfoMessage(" &bThe next round has been initialized! It will be begin in...");
						new Task2(plugin, countdownMessages).run();
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
								new Runnable(){ public void run(){
									sendInfoMessage(" &bRound 2 has begun!");												
									for (int i = 0; i < 50; i++){
										for (World world : Bukkit.getWorlds()){
											world.spawnEntity(new Location(arenaWorld, x, y, z), EntityType.SKELETON);
										}
									}
									plugin.getConfig().set("roundNumber", 2);
									plugin.getConfig().set("round1Complete", null);
									plugin.getConfig().set("zombiesKilled", null);
									plugin.saveConfig();
									return;
								}}, ticks2);
						return true;
					}
					if (round2Complete == true){
						sendInfoMessage(" &bThe next round has been initialized! It will be begin in...");
						new Task2(plugin, countdownMessages).run();
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
								new Runnable(){ public void run(){
									sendInfoMessage(" &bRound 3 has begun!");												
									for (int i = 0; i < 50; i++){
										for (World world : Bukkit.getWorlds()){
											world.spawnEntity(new Location(arenaWorld, x, y, z), EntityType.SPIDER);
										}
									}
									plugin.getConfig().set("roundNumber", 3);
									plugin.getConfig().set("round2Complete", null);
									plugin.getConfig().set("skeliesKilled", null);
									plugin.saveConfig();
									return;
								}}, ticks2);
						return true;
					}
					if (round3Complete == true){
						sendInfoMessage(" &bThe next round has been initialized! It will be begin in...");
						new Task2(plugin, countdownMessages).run();
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
						  new Runnable(){ public void run(){
							  sendInfoMessage(" &bRound 4 has begun!");												
						    for (int i = 0; i < 50; i++){
						      for (World world : Bukkit.getWorlds()){
										world.spawnEntity(new Location(arenaWorld, x, y, z), EntityType.ZOMBIE);
						      }
						    }
						    for (int i = 0; i < 50; i++){
						      for (World world : Bukkit.getWorlds()){
						        world.spawnEntity(new Location(arenaWorld, x, y, z), EntityType.SKELETON);
						      }
						    }
						    plugin.getConfig().set("roundNumber", 4);
						    plugin.getConfig().set("round3Complete", null);
						    plugin.getConfig().set("spidersKilled", null);
						    plugin.saveConfig();
						    return;
						  }}, ticks2);
						return true;
					}
					if (round4Complete == true){
						sendInfoMessage(" &bThe first BOSS round has been initialized! Beginning in...");
						new Task2(plugin, countdownMessages).run();
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
						  new Runnable(){ public void run(){
							  	sendInfoMessage(" &bRound 5, 1st BOSS, has begun!");
								plugin.getConfig().set("roundNumber", 5);
								plugin.saveConfig();
								for (int i = 0; i < 3; i++){
								      for (World world : Bukkit.getWorlds()){
								        world.spawnEntity(new Location(arenaWorld, x, y, z), EntityType.ZOMBIE);
								      }
								}
								plugin.getConfig().set("round4Complete", null);
								plugin.getConfig().set("zombiesKilled", null);
								plugin.getConfig().set("skeliesKilled", null);
								plugin.getConfig().set("zombiesFullKilled", null);
								plugin.getConfig().set("skeliesFullKilled", null);
								plugin.saveConfig();
								return;
						  }}, ticks2);
						return true;
					}
					if (round5Complete == true){
						return true;
					}
					return true;
				}
				sender.sendMessage(rA(introMessage + " &cUnknown argument. &7Type &c/help &7for command usage."));
				return true;
			}
		} else {
			sender.sendMessage(rA("You must be a player to execute this command!"));
			return false;
		}
		return false;
	}
	
	public void sendInfoMessage(String message){
		String introMessage = plugin.getConfig().getString("introMessage");
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			Player p = Bukkit.getPlayer(mmPlayers.get(i));
			p.sendMessage(rA(introMessage + message));
		}
	}
	
	public void clearInventory(){
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			Player p = Bukkit.getPlayer(mmPlayers.get(i));
			PlayerInventory inv = p.getInventory();
			plugin.getConfig().set("Users." + p.getName() + ".inventory", inv.getContents());
			inv.clear();
			ItemStack[] armourContents = { new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR), };
			inv.setArmorContents(armourContents);
		}
	}
	
	public void getInventory(){
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			Player p = Bukkit.getPlayer(mmPlayers.get(i));
			PlayerInventory inv = p.getInventory();
			List<?> list = plugin.getConfig().getList("Users." + p.getName() + ".inventory");
			if (list != null){
				for (int a = 0; a < Math.min(list.size(), inv.getSize()); a++){
					inv.setItem(a, (ItemStack) list.get(a));
				}
			}
		}
	}
	
	public void giveDiscs(){
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		ItemStack green = new ItemStack(Material.GREEN_RECORD, 1);
		ItemStack yellow = new ItemStack(Material.GOLD_RECORD, 1);
		ItemMeta greenName = green.getItemMeta();
		ItemMeta yellowName = yellow.getItemMeta();
		List<String> greenLore = new ArrayList<String>();
		greenLore.add("&r&eRight click me to");
		greenLore.add("&r&eactivate your");
		greenLore.add("&r&ealpha ability.");
		List<String> yellowLore = new ArrayList<String>();
		yellowLore.add("&r&eRight click me to");
		yellowLore.add("&r&eactivate your");
		yellowLore.add("&r&ealpha ability.");
		greenName.setDisplayName("&2&lAlpha Ability");
		greenName.setLore(greenLore);
		yellowName.setDisplayName("&6&lOmega Ability");
		yellowName.setLore(yellowLore);
		green.setItemMeta(greenName);
		yellow.setItemMeta(yellowName);
		for (int i = 0; i < mmPlayers.size(); i++){
			Player p = Bukkit.getPlayer(mmPlayers.get(i));
			PlayerInventory inv = p.getInventory();
			inv.setItem(7, green);
			inv.setItem(8, yellow);
		}
	}
	
	public void distributeItems(){
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			String playerName = mmPlayers.get(i);
			Player p = Bukkit.getPlayer(playerName);
			String currentClass = plugin.getConfig().getString("Users." + p.getName() + ".class");
			plugin.getConfig().set("turn", playerName);
			plugin.saveConfig();
			if (currentClass == "medic"){
				medicKit();
			}
			if (currentClass == "spirit"){
				spiritKit();
			}
			if (currentClass == "warrior"){
				
			}
			if (currentClass == "inferno"){
				
			}
			if (currentClass == "roadrunner"){
				
			}
			if (currentClass == "sniper"){
				
			}
		}
	}
	
	public void medicKit(){
		Player p = Bukkit.getPlayer(plugin.getConfig().getString("turn"));
		PlayerInventory inv = p.getInventory();
		ItemStack helmet = new ItemStack(Material.GOLD_HELMET, 1);
		ItemStack chest = new ItemStack(Material.GOLD_CHESTPLATE, 1);
		ItemStack legs = new ItemStack(Material.GOLD_LEGGINGS, 1);
		ItemStack boots = new ItemStack(Material.GOLD_BOOTS, 1);
		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta helmetName = helmet.getItemMeta();
		ItemMeta chestName = chest.getItemMeta();
		ItemMeta legsName = legs.getItemMeta();
		ItemMeta bootsName = boots.getItemMeta();
		ItemMeta swordName = sword.getItemMeta();
		helmetName.setDisplayName("&eMedic Helmet");
		chestName.setDisplayName("&eMedic Chestplate");
		legsName.setDisplayName("&eMedic Leggings");
		bootsName.setDisplayName("&eMedic Booties");
		swordName.setDisplayName("&eMedic Sword");
		helmetName.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		helmetName.addEnchant(Enchantment.DURABILITY, 10, true);
		chestName.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		chestName.addEnchant(Enchantment.DURABILITY, 10, true);
		legsName.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		legsName.addEnchant(Enchantment.DURABILITY, 10, true);
		bootsName.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		bootsName.addEnchant(Enchantment.DURABILITY, 10, true);
		swordName.addEnchant(Enchantment.DURABILITY, 10, true);
		helmet.setItemMeta(helmetName);
		chest.setItemMeta(chestName);
		legs.setItemMeta(legsName);
		boots.setItemMeta(bootsName);
		sword.setItemMeta(swordName);
		inv.setHelmet(helmet);
		inv.setChestplate(chest);
		inv.setLeggings(legs);
		inv.setBoots(boots);
		inv.setItem(0, sword);
		plugin.getConfig().set("turn", null);
	}
	
	public void spiritKit(){
		Player p = Bukkit.getPlayer(plugin.getConfig().getString("turn"));
		PlayerInventory inv = p.getInventory();
		ItemStack helmet = new ItemStack(Material.GOLD_HELMET, 1);
		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
		ItemStack legs = new ItemStack(Material.IRON_LEGGINGS, 1);
		ItemStack boots = new ItemStack(Material.GOLD_BOOTS, 1);
		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta helmetName = helmet.getItemMeta();
		ItemMeta chestName = chest.getItemMeta();
		ItemMeta legsName = legs.getItemMeta();
		ItemMeta bootsName = boots.getItemMeta();
		ItemMeta swordName = sword.getItemMeta();
		helmetName.setDisplayName("&eSpirit Helmet");
		chestName.setDisplayName("&eSpirit Chestplate");
		legsName.setDisplayName("&eSpirit Leggings");
		bootsName.setDisplayName("&eSpirit Booties");
		swordName.setDisplayName("&eSpirit Sword");
		helmetName.addEnchant(Enchantment.DURABILITY, 10, true);
		chestName.addEnchant(Enchantment.DURABILITY, 10, true);
		legsName.addEnchant(Enchantment.DURABILITY, 10, true);
		bootsName.addEnchant(Enchantment.DURABILITY, 10, true);
		swordName.addEnchant(Enchantment.DURABILITY, 10, true);
		helmet.setItemMeta(helmetName);
		chest.setItemMeta(chestName);
		legs.setItemMeta(legsName);
		boots.setItemMeta(bootsName);
		sword.setItemMeta(swordName);
		inv.setHelmet(helmet);
		inv.setChestplate(chest);
		inv.setLeggings(legs);
		inv.setBoots(boots);
		inv.setItem(0, sword);
		plugin.getConfig().set("turn", null);
	}
	
}