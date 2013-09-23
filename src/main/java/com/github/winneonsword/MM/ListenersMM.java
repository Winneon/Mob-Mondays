package com.github.winneonsword.MM;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.winneonsword.MM.CommandMM;
import static com.github.winneonsword.CMAPI.API.ChatAPI.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ListenersMM implements Listener{
	
	private MainMM plugin;
	private CommandMM mm;
	
	public ListenersMM(MainMM plugin){
		this.plugin = plugin;
	}
	
	ItemStack bow = new ItemStack(Material.BOW, 1);
	ItemStack air = new ItemStack(Material.AIR, 1);
	public HashMap<Integer, String> playerNames = new HashMap<Integer, String>();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(PlayerDeathEvent event){
		return;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		int roundNumber = plugin.getConfig().getInt("roundNumber");
		LivingEntity entity = event.getEntity();
		if (roundNumber == 5){
			ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
			ItemStack chest = new ItemStack(Material.IRON_LEGGINGS, 1);
			ItemStack legs = new ItemStack(Material.IRON_CHESTPLATE, 1);
			ItemStack booties = new ItemStack(Material.IRON_BOOTS, 1);
			ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
			ItemMeta helmetName = helmet.getItemMeta();
			ItemMeta chestName = chest.getItemMeta();
			ItemMeta legsName = legs.getItemMeta();
			ItemMeta bootsName = booties.getItemMeta();
			ItemMeta swordName = sword.getItemMeta();
			helmetName.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
			chestName.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
			legsName.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
			bootsName.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
			swordName.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
			helmet.setItemMeta(helmetName);
			chest.setItemMeta(chestName);
			legs.setItemMeta(legsName);
			booties.setItemMeta(bootsName);
			sword.setItemMeta(swordName);
			setBossHelmet(entity, helmet);
			setBossChest(entity, chest);
			setBossLegs(entity, legs);
			setBossBooties(entity, booties);
			setBossSword(entity, sword);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFallDamage(EntityDamageEvent e){
		if (e.getEntity() instanceof Player){
			if (e.getCause() == DamageCause.FALL){
				boolean noDamage = plugin.getConfig().getBoolean("noDamage");
				if (noDamage != true){
					return;
				}
				e.setCancelled(true);
				plugin.getConfig().set("noDamage", null);
				plugin.saveConfig();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event){
		int roundNumber = plugin.getConfig().getInt("roundNumber");
		boolean gameStarted = plugin.getConfig().getBoolean("gameStarted");
		final Player player = event.getEntity().getKiller();
		final int x = plugin.getConfig().getInt("arenaWorld" + ".x");
		final int y = plugin.getConfig().getInt("arenaWorld" + ".y");
		final int z = plugin.getConfig().getInt("arenaWorld" + ".z");
		try {
			Bukkit.getWorld(plugin.getConfig().getString("arenaWorld" + ".world"));
		} catch (IllegalArgumentException iae){
			return;
		}
		final World arenaWorld = Bukkit.getWorld(plugin.getConfig().getString("arenaWorld" + ".world"));
		Location arena = new Location(arenaWorld, x, y, z);
		if (event.getEntity().getLocation().distance(arena) < 500){
			if (!(event.getEntity().getKiller() instanceof Player)){
				if (gameStarted == true){
					event.getDrops().clear();
					for (World world : Bukkit.getWorlds()){
						world.spawnEntity(new Location(arenaWorld, x, y, z), event.getEntityType());
					}
					return;
				}
				return;
			}
			if (event.getEntityType() == org.bukkit.entity.EntityType.ZOMBIE){
				Random rand = new Random();
				int randomNumber = rand.nextInt(3) + 1;
				int zombiesKilled = plugin.getConfig().getInt("zombiesKilled");
				
				if (gameStarted == true){
					event.getDrops().clear();
					int mobKills = plugin.getConfig().getInt("Users." + player.getName() + ".mobKills");
					plugin.getConfig().set("Users." + player.getName() + ".mobKills", mobKills + 1);
					plugin.saveConfig();
					if (roundNumber == 1){
						plugin.getConfig().set("zombiesKilled", zombiesKilled + 1);
						plugin.saveConfig();
						int totalZombiesKilled = plugin.getConfig().getInt("zombiesKilled");
						if (totalZombiesKilled == 40){
							killMobs();
							sendInfoMessage(" &bRound 1 is now complete! To start the next round, a staff needs to type &c/mm round&b!");
							plugin.getConfig().set("round1Complete", true);
							List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
							for (int i = 0; i < mmPlayers.size(); i++){
								Player p = Bukkit.getPlayer(mmPlayers.get(i));
								p.setHealth(20);
								p.setFoodLevel(20);
								p.setSaturation(200);
							}
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("&d&lElemental Shard");
							shard.setItemMeta(name);
							drops.add(shard);
							event.getDrops().clear();
							event.getDrops().addAll(drops);
							return;
						}
					}
					if (roundNumber == 4){
						plugin.getConfig().set("zombiesKilled", zombiesKilled + 1);
						plugin.saveConfig();
						int totalZombiesKilled = plugin.getConfig().getInt("zombiesKilled");
						if (totalZombiesKilled == 50){
							plugin.getConfig().set("zombiesFullKilled", true);
							plugin.saveConfig();
							boolean skeliesFullKilled = plugin.getConfig().getBoolean("skeliesFullKilled");
							if (skeliesFullKilled == true){
								killMobs();
								sendInfoMessage(" &bRound 4 has completed! To begin the first BOSS round, a staff needs to type &c/mm round&b!");
								plugin.getConfig().set("round4Complete", true);
								List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
								for (int i = 0; i < mmPlayers.size(); i++){
									Player p = Bukkit.getPlayer(mmPlayers.get(i));
									p.setHealth(20);
									p.setFoodLevel(20);
									p.setSaturation(200);
								}
							}
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("&d&lElemental Shard");
							shard.setItemMeta(name);
							drops.add(shard);
							event.getDrops().clear();
							event.getDrops().addAll(drops);
							return;
						}
					}
					if (roundNumber == 5){
						killMobs();
						sendInfoMessage(" &bThe first BOSS has been defeated! Thank you for playing with our first beta test of MM! See ya next week!");
						mm.clearInventory();
						plugin.getConfig().set("gameStarted", false);
						plugin.getConfig().set("zombiesKilled", null);
						plugin.getConfig().set("skeliesKilled", null);
						plugin.getConfig().set("spidersKilled", null);
						plugin.getConfig().set("roundNumber", null);
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
						ItemStack shard = new ItemStack(Material.QUARTZ, 20);
						ItemMeta name = shard.getItemMeta();
						ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
						
						name.setDisplayName("&d&lElemental Shard");
						shard.setItemMeta(name);
						drops.add(shard);
						event.getDrops().clear();
						event.getDrops().addAll(drops);
						return;
					}
				}
			}
			if (event.getEntityType() == org.bukkit.entity.EntityType.SKELETON){
				Random rand = new Random();
				int randomNumber = rand.nextInt(3) + 1;
				int skeliesKilled = plugin.getConfig().getInt("skeliesKilled");
				
				if (gameStarted == true){
					event.getDrops().clear();
					int mobKills = plugin.getConfig().getInt("Users." + player.getName() + ".mobKills");
					plugin.getConfig().set("Users." + player.getName() + ".mobKills", mobKills + 1);
					plugin.saveConfig();
					if (roundNumber == 2){
						plugin.getConfig().set("skeliesKilled", skeliesKilled + 1);
						plugin.saveConfig();
						int totalSkeliesKilled = plugin.getConfig().getInt("skeliesKilled");
						if (totalSkeliesKilled == 50){						
							killMobs();
							sendInfoMessage(" &bRound 2 is now complete! To start the next round, a staff needs to type &c/mm round&b!");
							plugin.getConfig().set("round2Complete", true);
							List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
							for (int i = 0; i < mmPlayers.size(); i++){
								Player p = Bukkit.getPlayer(mmPlayers.get(i));
								p.setHealth(20);
								p.setFoodLevel(20);
								p.setSaturation(200);
							}
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("&d&lElemental Shard");
							shard.setItemMeta(name);
							drops.add(shard);
							event.getDrops().clear();
							event.getDrops().addAll(drops);
							return;
						}
					}
					if (roundNumber == 4){
						plugin.getConfig().set("skeliesKilled", skeliesKilled + 1);
						plugin.saveConfig();
						int totalSkeliesKilled = plugin.getConfig().getInt("skeliesKilled");
						if (totalSkeliesKilled == 50){
							plugin.getConfig().set("skeliesFullKilled", true);
							plugin.saveConfig();
							boolean zombiesFullKilled = plugin.getConfig().getBoolean("zombiesFullKilled");
							if (zombiesFullKilled == true){
								killMobs();
								sendInfoMessage(" &bRound 4 has completed! To begin the first BOSS round, a staff needs to type &c/mm round&b!");
								plugin.getConfig().set("round4Complete", true);
								List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
								for (int i = 0; i < mmPlayers.size(); i++){
									Player p = Bukkit.getPlayer(mmPlayers.get(i));
									p.setHealth(20);
									p.setFoodLevel(20);
									p.setSaturation(200);
								}
							}
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("&d&lElemental Shard");
							shard.setItemMeta(name);
							drops.add(shard);
							event.getDrops().clear();
							event.getDrops().addAll(drops);
							return;
						}
					}
				}
			}
			if (event.getEntityType() == org.bukkit.entity.EntityType.SPIDER){
				Random rand = new Random();
				int randomNumber = rand.nextInt(3) + 1;
				int spidersKilled = plugin.getConfig().getInt("spidersKilled");
				
				if (gameStarted == true){
					event.getDrops().clear();
					int mobKills = plugin.getConfig().getInt("Users." + player.getName() + ".mobKills");
					plugin.getConfig().set("Users." + player.getName() + ".mobKills", mobKills + 1);
					plugin.saveConfig();
					if (roundNumber == 3){
						plugin.getConfig().set("spidersKilled", spidersKilled + 1);
						plugin.saveConfig();
						int totalSpidersKilled = plugin.getConfig().getInt("spidersKilled");
						if (totalSpidersKilled == 50){
							killMobs();
							sendInfoMessage(" &bRound 3 is now complete! To start the next round, a staff needs to type &c/mm round&b!");
							plugin.getConfig().set("round3Complete", true);
							List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
							for (int i = 0; i < mmPlayers.size(); i++){
								Player p = Bukkit.getPlayer(mmPlayers.get(i));
								p.setHealth(20);
								p.setFoodLevel(20);
								p.setSaturation(200);
							}
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("&d&lElemental Shard");
							shard.setItemMeta(name);
							drops.add(shard);
							event.getDrops().clear();
							event.getDrops().addAll(drops);
							return;
						}
					}
				}
			}
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event){
		final Player player = event.getPlayer();
		String introMessage = plugin.getConfig().getString(rA("introMessage"));
		boolean gameStarted = plugin.getConfig().getBoolean("gameStarted");
		
		if (gameStarted == true){
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
				try {
					if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Elemental Shard") && player.getInventory().getItemInHand().getType() == Material.QUARTZ){
						int shardCount = plugin.getConfig().getInt("Users." + player.getName() + ".shards");
						int shardCountInHand = player.getInventory().getItemInHand().getAmount();
						int quantity = shardCountInHand + shardCount;
						
						plugin.getConfig().set("Users." + player.getName() + ".shards", quantity);
						plugin.saveConfig();
						player.sendMessage(rA(introMessage + " &7You have deposited " + shardCountInHand + " shard(s)!"));
						player.setItemInHand(air);
						return;
					}
				} catch (NullPointerException e){
					
				}
				try {
					if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Ability")){
						String currentClass = plugin.getConfig().getString("Users." + player.getName() + ".class");
						if (currentClass == "medic"){
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
								plugin.getConfig().set("usingMedicAbilityA", player.getName());
								medicAlphaAbility();
								return;
							}
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
								plugin.getConfig().set("usingMedicAbilityO", player.getName());
								medicOmegaAbility();
								return;
							}
						}
						if (currentClass == "spirit"){
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
								int shardCount = plugin.getConfig().getInt("Users." + player.getName() + ".shards");
								if (shardCount < 5){
									player.sendMessage(rA(introMessage + " &cYou do not have 5 shards in your account!"));
									return;
								}
								plugin.getConfig().set("usingSpiritAbilityA", player.getName());
								plugin.saveConfig();
								spiritAlphaAbility();
								return;
							}
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
								plugin.getConfig().set("usingSpiritAbilityO", player.getName());
								spiritOmegaAbility();
								return;
							}
						}
						if (currentClass == "warrior"){
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
								plugin.getConfig().set("usingAbility", player.getName());
								
								return;
							}
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
								plugin.getConfig().set("usingAbility", player.getName());
								
								return;
							}
						}
						if (currentClass == "inferno"){
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
								plugin.getConfig().set("usingAbility", player.getName());
								
								return;
							}
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
								plugin.getConfig().set("usingAbility", player.getName());
								
								return;
							}
						}
						if (currentClass == "roadrunner"){
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
								plugin.getConfig().set("usingAbility", player.getName());
								
								return;
							}
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
								plugin.getConfig().set("usingAbility", player.getName());
								
								return;
							}
						}
						if (currentClass == "sniper"){
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
								plugin.getConfig().set("usingAbility", player.getName());
								
								return;
							}
							if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
								plugin.getConfig().set("usingAbility", player.getName());
								
								return;
							}
						}
					}
				} catch (NullPointerException e){
					
				}
				return;
			}
			return;
		}
		return;
	}
	
	public void killMobs(){
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			Player player = Bukkit.getPlayer(mmPlayers.get(i));
			for (Entity entities : player.getNearbyEntities(250, player.getWorld().getMaxHeight() * 2, 250)){
				if (entities instanceof Monster){
				((Monster) entities).remove();
				}
			}
			for (Entity entities : player.getNearbyEntities(250, player.getWorld().getMaxHeight() * 2, 250)){
				if (entities instanceof Slime){
					((Slime) entities).remove();
				}
			}
		}
	}
	
	public void setBossHelmet(LivingEntity e, ItemStack helmet){
		EntityEquipment eq = e.getEquipment();
		eq.setHelmet(helmet);
	}
	
	public void setBossChest(LivingEntity e, ItemStack chest){
		EntityEquipment eq = e.getEquipment();
		eq.setChestplate(chest);
	}
	
	public void setBossLegs(LivingEntity e, ItemStack legs){
		EntityEquipment eq = e.getEquipment();
		eq.setLeggings(legs);
	}
	public void setBossBooties(LivingEntity e, ItemStack booties){
		EntityEquipment eq = e.getEquipment();
		eq.setBoots(booties);
	}
	
	public void setBossSword(LivingEntity e, ItemStack sword){
		EntityEquipment eq = e.getEquipment();
		eq.setItemInHand(sword);
	}
	
	public void sendInfoMessage(String message){
		String introMessage = plugin.getConfig().getString(rA("introMessage"));
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			Player p = Bukkit.getPlayer(mmPlayers.get(i));
			p.sendMessage(rA(introMessage + message));
		}
	}
	
	public void medicAlphaAbility(){
		Player p = Bukkit.getPlayer(plugin.getConfig().getString("usingMedicAbilityA"));
		String introMessage = plugin.getConfig().getString(rA("introMessage"));
		int shardCount = plugin.getConfig().getInt("Users." + p.getName() + ".shards");
		if (shardCount < 5){
			p.sendMessage(rA(introMessage + " &cYou do not have 5 shards in your account!"));
			return;
		}
		p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1200, 2));
		p.setHealth(32);
		p.sendMessage(rA(introMessage + " &7You were given 6 extra hearts for 1 minute!"));
		p.sendMessage(rA(introMessage + " &c(5 shards have been deducted from your account.)"));
		plugin.getConfig().set("Users." + p.getName() + ".shards", shardCount - 5);
		plugin.saveConfig();
	}
	
	public void medicOmegaAbility(){
		Player p = Bukkit.getPlayer(plugin.getConfig().getString("usingMedicAbilityO"));
		String introMessage = plugin.getConfig().getString(rA("introMessage"));
		int shardCount = plugin.getConfig().getInt("Users." + p.getName() + ".shards");
		if (shardCount < 8){
			p.sendMessage(rA(introMessage + " &cYou do not have 8 shards in your account!"));
			return;
		}
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			Player player = Bukkit.getPlayer(mmPlayers.get(i));
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 1));
			if (player == p){
				p.sendMessage(rA(introMessage + " &7You have given the whole group Regeneration II for 1 minute!"));
				p.sendMessage(rA(introMessage + " &c(8 shards have been deducted from your account.)"));
			} else {
				player.sendMessage(rA(introMessage + " &7" + p.getName() + " has given you Regeneration II for 1 minute!"));
			}
		}
		plugin.getConfig().set("Users." + p.getName() + ".shards", shardCount - 8);
		plugin.saveConfig();
	}
	
	public void spiritAlphaAbility(){
		int taskCount = plugin.getConfig().getInt("Tasks.Flying.TotalTasks");
		final Player p = Bukkit.getPlayer(plugin.getConfig().getString("usingSpiritAbilityA"));
		final String introMessage = plugin.getConfig().getString(rA("introMessage"));
		int shardCount = plugin.getConfig().getInt("Users." + p.getName() + ".shards");
		plugin.getConfig().set("Users." + p.getName() + ".shards", shardCount - 5);
		p.setAllowFlight(true);
		p.sendMessage(rA(introMessage + " &7You were given fly mode for 1 minute!"));
		p.sendMessage(rA(introMessage + " &c(5 shards have been deducted from your account.)"));
        taskCount++;
        plugin.getConfig().set("Tasks.Flying.TotalTasks", taskCount);
        plugin.getConfig().set("Tasks.Flying." + taskCount, p.getName());
        plugin.saveConfig();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
				  new Runnable(){ public void run(){
					  int taskCountLater = plugin.getConfig().getInt("Tasks.Flying.Completed");
                      taskCountLater++;
                      plugin.getConfig().set("Tasks.Flying.Completed", taskCountLater);
                      Player pLater = Bukkit.getPlayer(plugin.getConfig().getString("Tasks.Flying." + taskCountLater));
					  pLater.setAllowFlight(false);
					  plugin.getConfig().set("noDamage", true);
					  plugin.saveConfig();
					  pLater.sendMessage(rA(introMessage + " &7Your fly mode has worn off!"));
				  }
		}, 1200);
	}
	
	public void spiritOmegaAbility(){
		Player p = Bukkit.getPlayer(plugin.getConfig().getString("usingSpiritAbilityO"));
		final String introMessage = plugin.getConfig().getString(rA("introMessage"));
		int shardCount = plugin.getConfig().getInt("Users." + p.getName() + ".shards");
		if (shardCount < 8){
			p.sendMessage(rA(introMessage + " &cYou do not have 8 shards in your account!"));
			return;
		}
		plugin.getConfig().set("Users." + p.getName() + ".shards", shardCount - 8);
		plugin.saveConfig();
		final List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			Player player = Bukkit.getPlayer(mmPlayers.get(i));
			player.setAllowFlight(true);
			if (player == p){
				p.sendMessage(rA(introMessage + " &7You have given the whole group fly mode for 1 minute!"));
				p.sendMessage(rA(introMessage + " &c(8 shards have been deducted from your account.)"));
			} else {
				player.sendMessage(rA(introMessage + " &7" + p.getName() + " has given you fly mode for 1 minute!"));
			}
		}
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
				new Runnable(){ public void run(){
					for (int i = 0; i < mmPlayers.size(); i++){
						Player player = Bukkit.getPlayer(mmPlayers.get(i));
						player.setAllowFlight(false);
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 4));
						player.sendMessage(rA(introMessage + " &7Your fly mode has worn off!"));
					}
				}
		}, 1200);
	}
	
}