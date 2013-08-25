package io.github.winneonsword.mm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class listeners implements Listener{
	
	private mm plugin;
	
	public listeners(mm plugin){
		this.plugin = plugin;
	}
	
	Skeleton skeleton;
	ItemStack bow = new ItemStack(Material.BOW, 1);
	ItemStack air = new ItemStack(Material.AIR, 1);
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(PlayerDeathEvent event){
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		int roundNumber = plugin.getConfig().getInt("roundNumber");
		LivingEntity entity = event.getEntity();
		EntityType entityType = event.getEntityType();
		EntityEquipment eqp = entity.getEquipment();
		
		if (roundNumber == 6){
			if (entityType == EntityType.SKELETON){
				if (eqp.getItemInHand() != bow){
					eqp.setItemInHand(bow);
					return;
				}
				return;
			}
			return;
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event){
		int roundNumber = plugin.getConfig().getInt("roundNumber");
		boolean gameStarted = plugin.getConfig().getBoolean("gameStarted");
		final Player player = event.getEntity().getKiller();
		final World arenaWorld = Bukkit.getWorld(plugin.getConfig().getString("arenaWorld" + ".world"));
		final int x = plugin.getConfig().getInt("arenaWorld" + ".x");
		final int y = plugin.getConfig().getInt("arenaWorld" + ".y");
		final int z = plugin.getConfig().getInt("arenaWorld" + ".z");
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
						if (totalZombiesKilled == 20){
							killMobs();
							sendInfoMessage(" §bRound 1 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
							plugin.getConfig().set("round1Complete", true);
							player.setHealth(20);
							player.setFoodLevel(20);
							player.setSaturation(200);
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("§d§lElemental Shard");
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
						if (totalZombiesKilled == 25){
							plugin.getConfig().set("zombiesFullKilled", true);
							plugin.saveConfig();
							boolean skeliesFullKilled = plugin.getConfig().getBoolean("skeliesFullKilled");
							if (skeliesFullKilled == true){
								killMobs();
								sendInfoMessage(" §bRound 4 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
								plugin.getConfig().set("round4Complete", true);
								player.setHealth(20);
								player.setFoodLevel(20);
								player.setSaturation(200);
								
							}
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("§d§lElemental Shard");
							shard.setItemMeta(name);
							drops.add(shard);
							event.getDrops().clear();
							event.getDrops().addAll(drops);
							return;
						}
					}
					if (roundNumber == 5){
						plugin.getConfig().set("zombiesKilled", zombiesKilled + 1);
						plugin.saveConfig();
						int totalZombiesKilled = plugin.getConfig().getInt("zombiesKilled");
						if (totalZombiesKilled == 30){
							plugin.getConfig().set("zombiesFullKilled", true);
							plugin.saveConfig();
							boolean skeliesFullKilled = plugin.getConfig().getBoolean("skeliesFullKilled");
							boolean spidersFullKilled = plugin.getConfig().getBoolean("spidersFullKilled");
							if (skeliesFullKilled == true){
								killMobs();
								sendInfoMessage(" §bRound 4 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
								plugin.getConfig().set("round4Complete", true);
								player.setHealth(20);
								player.setFoodLevel(20);
								player.setSaturation(200);
								
							}
							if (spidersFullKilled == true){
								killMobs();
								sendInfoMessage(" §bRound 4 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
								plugin.getConfig().set("round4Complete", true);
								player.setHealth(20);
								player.setFoodLevel(20);
								player.setSaturation(200);
								
							}
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("§d§lElemental Shard");
							shard.setItemMeta(name);
							drops.add(shard);
							event.getDrops().clear();
							event.getDrops().addAll(drops);
							return;
						}
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
						if (totalSkeliesKilled == 30){						
							killMobs();
							sendInfoMessage(" §bRound 2 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
							plugin.getConfig().set("round2Complete", true);
							player.setHealth(20);
							player.setFoodLevel(20);
							player.setSaturation(200);
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("§d§lElemental Shard");
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
						if (totalSkeliesKilled == 25){
							plugin.getConfig().set("skeliesFullKilled", true);
							plugin.saveConfig();
							boolean zombiesFullKilled = plugin.getConfig().getBoolean("zombiesFullKilled");
							if (zombiesFullKilled == true){
								killMobs();
								sendInfoMessage(" §bRound 4 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
								plugin.getConfig().set("round4Complete", true);
								player.setHealth(20);
								player.setFoodLevel(20);
								player.setSaturation(200);
							}
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("§d§lElemental Shard");
							shard.setItemMeta(name);
							drops.add(shard);
							event.getDrops().clear();
							event.getDrops().addAll(drops);
							return;
						}
					}
					if (roundNumber == 5){
						
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
						if (totalSpidersKilled == 40){
							killMobs();
							sendInfoMessage(" §bRound 3 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
							plugin.getConfig().set("round3Complete", true);
							player.setHealth(20);
							player.setFoodLevel(20);
							player.setSaturation(200);
						}
						if (randomNumber == 3){
							ItemStack shard = new ItemStack(Material.QUARTZ, 1);
							ItemMeta name = shard.getItemMeta();
							ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
							
							name.setDisplayName("§d§lElemental Shard");
							shard.setItemMeta(name);
							drops.add(shard);
							event.getDrops().clear();
							event.getDrops().addAll(drops);
							return;
						}
					}
					if (roundNumber == 5){
						
					}
				}
			}
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		String introMessage = plugin.getConfig().getString("introMessage");
		boolean gameStarted = plugin.getConfig().getBoolean("gameStarted");
		
		if (gameStarted == true){
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){	
				if (!(player.getInventory().getItemInHand().getTypeId() == 406) || !(player.getInventory().getItemInHand().getTypeId() == 2257) || !(player.getInventory().getItemInHand().getTypeId() == 2256)){
					return;
				}
				if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Elemental Shard")){
					int shardCount = plugin.getConfig().getInt("Users." + player.getName() + ".shards");
					int shardCountInHand = player.getInventory().getItemInHand().getAmount();
					int quantity = shardCountInHand + shardCount;
					
					plugin.getConfig().set("Users." + player.getName() + ".shards", quantity);
					plugin.saveConfig();
					player.sendMessage(introMessage + " §7You have deposited " + shardCountInHand + " shard(s)!");
					player.setItemInHand(air);
					return;
				}
				if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Ability")){
					String currentClass = plugin.getConfig().getString("Users." + player.getName() + ".class");
					if (currentClass == "medic"){
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
							sendInfoMessage("test");
							plugin.getConfig().set("usingAbility", player.getName());
							medicAlphaAbility();
							return;
						}
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
							return;
						}
					}
					if (currentClass == "spirit"){
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
							return;
						}
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
							return;
						}
					}
					if (currentClass == "warrior"){
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
							return;
						}
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
							return;
						}
					}
					if (currentClass == "inferno"){
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
							return;
						}
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
							return;
						}
					}
					if (currentClass == "roadrunner"){
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
							return;
						}
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
							return;
						}
					}
					if (currentClass == "sniper"){
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Alpha")){
							return;
						}
						if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().contains("Omega")){
							return;
						}
					}
				}
				return;
			}
			return;
		}
		return;
	}
	
	public void killMobs(){
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 1; i > mmPlayers.size(); i++){
			Player player = Bukkit.getPlayer(mmPlayers.get(i - 1));
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
	
	public void sendInfoMessage(String message){
		String introMessage = plugin.getConfig().getString("introMessage");
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 1; i > mmPlayers.size(); i++){
			Player p = Bukkit.getPlayer(mmPlayers.get(i - 1));
			p.sendMessage(introMessage + message);
		}
	}
	public void medicAlphaAbility(){
		Player p = Bukkit.getPlayer(plugin.getConfig().getString("usingAbility"));
		String introMessage = plugin.getConfig().getString("introMessage");
		int shardCount = plugin.getConfig().getInt("Users." + p.getName() + ".shards");
		if (shardCount < 5){
			p.sendMessage(introMessage + " §cYou do not have 5 shards in your account!");
			return;
		}
		p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, 5));
		p.sendMessage(introMessage + " §7You were given 5 extra hearts for 1 minute! §c(5 shards have been deducted from your account.)");
		plugin.getConfig().set("Users." + p.getName() + ".shards", shardCount - 5);
		plugin.getConfig().set("usingAbility", null);
		plugin.saveConfig();
	}
	
}