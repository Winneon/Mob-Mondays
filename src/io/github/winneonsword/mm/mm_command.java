package io.github.winneonsword.mm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class mm_command implements CommandExecutor{
		
	mm plugin;
	
	public mm_command(mm instance){
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
				
				if (args.length == 0){
					// Name & version number.
					sender.sendMessage(introMessage + " §7Mob Mondays, v0.3-a");
					return true;
				}
				if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")){
					// The main help menu.
					sender.sendMessage(introMessage + " §7Mob Mondays Help Menu");
					sender.sendMessage("  §c/mm ? §7- Help Menu.");
					sender.sendMessage("  §c/mm join §7- Join the queue for the next game.");
					sender.sendMessage("  §c/mm leave §7- Leave the queue for the next game.");
					sender.sendMessage("  §c/mm list §7- Show the joined player list.");
					sender.sendMessage("  §c/mm start §7- Start an MM game. §8// §cStaff command only!");
					sender.sendMessage("  §c/mm reload §7- Reloads the config file. §8// §cStaff command only!");
					sender.sendMessage("§7Created by WS, original idea / VT plugin by Hugs.");
					return true;
				}
				if (args[0].equalsIgnoreCase("join")){
					// Join the queue list for any upcoming games.
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					
					if (isPlaying == true){
						sender.sendMessage(introMessage + " §cYou are already selected as a class! §7To change classes, type §c/mm select <class>§7.");
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", true);
					
					sender.sendMessage(introMessage + " §7Choose a class below:");
					sender.sendMessage("  §cmedic §7- Give extra hearts or regen for a limited time.");
					sender.sendMessage("  §cspirit §7- Revieve fly mode for limited time.");
					sender.sendMessage("  §cwarrior §7- Create a knockback blast that stuns opponents..");
					sender.sendMessage("  §cinferno §7- Forge a fire burst and scorch opponents.");
					sender.sendMessage("  §croadrunner §7- Recieve speedy boots for a limted time.");
					sender.sendMessage("  §csniper §7- Headshot opponents quickly and swiftly.");
					sender.sendMessage("§7Type §c/mm select <class> §7to select a class.");
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args.length == 1){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					sender.sendMessage(introMessage + " §7Type §c/mm join §7to join a class!");
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("medic") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "medic"){
						sender.sendMessage(introMessage + " §cYou are already selected as the medic class!");
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "medic");
						
						sender.sendMessage(introMessage + " §7Do you wish to change classes? Type §c/mm confirm §7to change classes to §cmedic§7. Otherwise, type §c/mm cancel §7to cancel this operation.");
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(introMessage + " §cYou are not currently selecting a class! §7Type §c/mm join §7to select a class.");
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "medic");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "§7, §7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(introMessage + " §7You have selected the §cmedic §7class! You are added to the join queue!");
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("spirit") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "spirit"){
						sender.sendMessage(introMessage + " §cYou are already selected as the spirit class!");
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "spirit");
						plugin.saveConfig();
						
						sender.sendMessage(introMessage + " §7Do you wish to change classes? Type §c/mm confirm §7to change classes to §cspirit§7. Otherwise, type §c/mm cancel §7to cancel this operation.");
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(introMessage + " §cYou are not currently selecting a class! §7Type §c/mm join §7to select a class.");
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "spirit");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "§7, §7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(introMessage + " §7You have selected the §cspirit §7class! You are added to the join queue!");
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("warrior") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "warrior"){
						sender.sendMessage(introMessage + " §cYou are already selected as the warrior class!");
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "warrior");
						plugin.saveConfig();
						
						sender.sendMessage(introMessage + " §7Do you wish to change classes? Type §c/mm confirm §7to change classes to §cwarrior§7. Otherwise, type §c/mm cancel §7to cancel this operation.");
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(introMessage + " §cYou are not currently selecting a class! §7Type §c/mm join §7to select a class.");
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "warrior");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "§7, §7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(introMessage + " §7You have selected the §cwarrior §7class! You are added to the join queue!");
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("inferno") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "inferno"){
						sender.sendMessage(introMessage + " §cYou are already selected as the inferno class!");
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "inferno");
						plugin.saveConfig();
						
						sender.sendMessage(introMessage + " §7Do you wish to change classes? Type §c/mm confirm §7to change classes to §cinferno§7. Otherwise, type §c/mm cancel §7to cancel this operation.");
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(introMessage + " §cYou are not currently selecting a class! §7Type §c/mm join §7to select a class.");
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "inferno");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "§7, §7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(introMessage + " §7You have selected the §cinferno §7class! You are added to the join queue!");
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("roadrunner") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "roadrunner"){
						sender.sendMessage(introMessage + " §cYou are already selected as the roadrunner class!");
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "roadrunner");
						plugin.saveConfig();
						
						sender.sendMessage(introMessage + " §7Do you wish to change classes? Type §c/mm confirm §7to change classes to §croadrunner§7. Otherwise, type §c/mm cancel §7to cancel this operation.");
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(introMessage + " §cYou are not currently selecting a class! §7Type §c/mm join §7to select a class.");
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "roadrunner");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "§7, §7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(introMessage + " §7You have selected the §croadrunner §7class! You are added to the join queue!");
					return true;
				}
				if (args[0].equalsIgnoreCase("select") && args[1].equalsIgnoreCase("sniper") && args.length == 2){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					boolean isPlaying = plugin.getConfig().getBoolean("Users." + sender.getName() + ".isPlaying");
					boolean choosingClass = plugin.getConfig().getBoolean("Users." + sender.getName() + ".choosingClass");
					String currentClass = plugin.getConfig().getString("Users." + sender.getName() + ".class");
					
					if (currentClass == "sniper"){
						sender.sendMessage(introMessage + " §cYou are already selected as the sniper class!");
						return true;
					}
					if (isPlaying == true){
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", "sniper");
						plugin.saveConfig();
						
						sender.sendMessage(introMessage + " §7Do you wish to change classes? Type §c/mm confirm §7to change classes to §csniper§7. Otherwise, type §c/mm cancel §7to cancel this operation.");
						return true;
					}
					if (choosingClass != true){
						sender.sendMessage(introMessage + " §cYou are not currently selecting a class! §7Type §c/mm join §7to select a class.");
						return true;
					}
					plugin.getConfig().set("Users." + sender.getName() + ".class", "sniper");
					plugin.getConfig().set("Users." + sender.getName() + ".isPlaying", true);
					plugin.getConfig().set("Users." + sender.getName() + ".choosingClass", null);
					plugin.getConfig().set("Users." + sender.getName() + ".displayName", player.getDisplayName());
					List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
					mmPlayers.add(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					plugin.getConfig().set("listDisplay", listDisplay + "§7, §7" + player.getDisplayName());
					plugin.saveConfig();
					sender.sendMessage(introMessage + " §7You have selected the §csniper §7class! You are added to the join queue!");
					return true;
				}
				if (args[0].equalsIgnoreCase("confirm")){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					String changingClass = plugin.getConfig().getString("Users." + sender.getName() + ".changingClass");
					
					if (changingClass == "healer"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "healer");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(introMessage + " §7You have changed to the §chealer §7class!");
						return true;
					}
					if (changingClass == "spirit"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "spirit");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(introMessage + " §7You have changed to the §cspirit §7class!");
						return true;
					}
					if (changingClass == "warrior"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "warrior");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(introMessage + " §7You have changed to the §cwarrior §7class!");
						return true;
					}
					if (changingClass == "inferno"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "inferno");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(introMessage + " §7You have changed to the §cinferno §7class!");
						return true;
					}
					if (changingClass == "roadrunner"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "roadrunner");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(introMessage + " §7You have changed to the §croadrunner §7class!");
						return true;
					}
					if (changingClass == "sniper"){
						plugin.getConfig().set("Users." + sender.getName() + ".class", "sniper");
						plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
						plugin.saveConfig();
						sender.sendMessage(introMessage + " §7You have changed to the §csniper §7class!");
						return true;
					}
					sender.sendMessage(introMessage + " §cYou are not currently changing a class!");
					return true;
				}
				if (args[0].equalsIgnoreCase("cancel")){
					plugin.getConfig().set("Users." + sender.getName() + ".changingClass", null);
					sender.sendMessage(introMessage + " §7You have canceled the operation involving changing classes!");
					return true;
				}
				if (args[0].equalsIgnoreCase("leave")){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
					mmPlayers.remove(sender.getName());
					plugin.getConfig().set("MM.players", mmPlayers);
					String listDisplay = plugin.getConfig().getString("listDisplay");
					String displayName = plugin.getConfig().getString("Users." + sender.getName() + ".displayName");
					plugin.getConfig().set("listDisplay", listDisplay.replace("§7, §7" + displayName, ""));
					plugin.getConfig().set("Users." + sender.getName(), null);
					plugin.saveConfig();
					sender.sendMessage(introMessage + " §7You have been removed from the queue list!");
					return true;
				}
				if (args[0].equalsIgnoreCase("reload")){
					plugin.reloadConfig();
					sender.sendMessage(introMessage + " §7Successfully reloaded the config file.");
					return true;
				}
				if (args[0].equalsIgnoreCase("set")){
					plugin.getConfig().set("arenaWorld" + ".world", player.getLocation().getWorld().getName());
					plugin.getConfig().set("arenaWorld" + ".x", player.getLocation().getBlockX());
					plugin.getConfig().set("arenaWorld" + ".y", player.getLocation().getBlockY());
					plugin.getConfig().set("arenaWorld" + ".z", player.getLocation().getBlockZ());
					plugin.getConfig().set("arenaSet", true);
					plugin.saveConfig();
					sender.sendMessage(introMessage + " §7Successfully set the center of the arena.");
					return true;
				}
				if (args[0].equalsIgnoreCase("commands")){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					
					return true;
				}
				if (args[0].equalsIgnoreCase("butcher")){
					for (Entity entities : player.getWorld().getEntities()){
						if (entities instanceof Monster){
							((Monster) entities).remove();
						}
					}
					sender.sendMessage(introMessage + " §7Successfully killed all hostile mobs.");
					return true;
				}
				if (args[0].equalsIgnoreCase("list")){
					String listDisplay = plugin.getConfig().getString("listDisplay");
					
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					if (listDisplay.equals("")){
						sender.sendMessage(introMessage + " §cThere are no players joined!");
						return true;
					}
					plugin.getConfig().set("listDisplay", listDisplay.replaceFirst("§7,", ""));
					String listDisplay2 = plugin.getConfig().getString("listDisplay");
					sender.sendMessage(introMessage + " §bPlayers Joined:" + listDisplay2);
					plugin.getConfig().set("listDisplay", listDisplay);
					plugin.saveConfig();
					return true;
				}
				if (args[0].equalsIgnoreCase("start")){
					if (useDayTimer == true){
						if (calendar.get(Calendar.DAY_OF_WEEK) != 2){
							sender.sendMessage(introMessage + " §cIt is not Monday! All MM commands are disabled until it is Monday!");
							return true;
						}
					}
					if (player.hasPermission("mm.start")){
						boolean gameStarted = plugin.getConfig().getBoolean("gameStarted");
						
						if (gameStarted == false){
							for (final World world : Bukkit.getWorlds()){
								boolean arenaSet = plugin.getConfig().getBoolean("arenaSet");
								
								if (arenaSet != true){
									sender.sendMessage(introMessage + " §cYou have not set an arena! §7Go to the center of your arena and type §c/mm set§7!");
									return true;
								}
								
								final List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
								List<String> welcomeMessages = plugin.getConfig().getStringList("welcomeMessages");
								final List<String> countdownMessages = plugin.getConfig().getStringList("countdownMessages");
								
								int list1 = welcomeMessages.size();
								final int ticks = list1 * 100;
								
								int list2 = countdownMessages.size();
								final int ticks2 = list2 * 40;
								
								// This is set to 1 currently. At the release it will be set at 5.
								if (mmPlayers.size() < 1){
									sender.sendMessage(introMessage + " §cThere is not 5 or more people currently in the player queue!");
									return true;
								}
								plugin.getConfig().set("gameStarted", true);
								plugin.saveConfig();
								
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
														
														Bukkit.broadcastMessage(introMessage + " §bMob Mondays has begun! For details on your stats, type §c/mm stats§b.");
														
														world.setFullTime(18000);
														world.setGameRuleValue("doDaylightCycle", "false");
															
														for (int i = 0; i < 20; i++){
															world.spawnEntity(new Location(arenaWorld, x, y, z), org.bukkit.entity.EntityType.ZOMBIE);
														}
														plugin.getConfig().set("roundNumber", 1);
														plugin.saveConfig();
													}}, ticks2);
										}}, ticks);
								return true;
								
							}
						}
						if (gameStarted == true){
							sender.sendMessage(introMessage + " §cThere is already a game started!");
							// Everything after this is for testing purposes only, and will be removed for the final release.
							plugin.getConfig().set("gameStarted", false);
							plugin.getConfig().set("zombiesKilled", null);
							plugin.getConfig().set("skeliesKilled", null);
							plugin.getConfig().set("spidersmKilled", null);
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
							return true;
						}
					}
				}
				sender.sendMessage(introMessage + " §cUnknown argument. §7Type §c/help §7for command usage.");
				return true;
			}
		} else {
			sender.sendMessage("You must be a player to execute this command!");
			return false;
		}
		return false;
	}
}