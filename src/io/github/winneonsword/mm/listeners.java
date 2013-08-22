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

import java.util.ArrayList;
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
		final String introMessage = plugin.getConfig().getString("introMessage");
		final Player player = event.getEntity().getKiller();
		final World arenaWorld = Bukkit.getWorld(plugin.getConfig().getString("arenaWorld" + ".world"));
		final int x = plugin.getConfig().getInt("arenaWorld" + ".x");
		final int y = plugin.getConfig().getInt("arenaWorld" + ".y");
		final int z = plugin.getConfig().getInt("arenaWorld" + ".z");
		
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
				if (roundNumber == 1){
					plugin.getConfig().set("zombiesKilled", zombiesKilled + 1);
					plugin.saveConfig();
					int totalZombiesKilled = plugin.getConfig().getInt("zombiesKilled");
					if (totalZombiesKilled == 20){
						
						for (Entity entities : player.getWorld().getEntities()){
							if (entities instanceof Monster){
								((Monster) entities).remove();
							}
						}
						Bukkit.broadcastMessage(introMessage + " §bRound 1 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
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
							for (Entity entities : player.getWorld().getEntities()){
								if (entities instanceof Monster){
									((Monster) entities).remove();
								}
							}
							Bukkit.broadcastMessage(introMessage + " §bRound 4 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
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
		if (event.getEntityType() == org.bukkit.entity.EntityType.SKELETON){
			Random rand = new Random();
			int randomNumber = rand.nextInt(3) + 1;
			int skeliesKilled = plugin.getConfig().getInt("skeliesKilled");
			
			if (gameStarted == true){
				event.getDrops().clear();
				if (roundNumber == 2){
					plugin.getConfig().set("skeliesKilled", skeliesKilled + 1);
					plugin.saveConfig();
					int totalSkeliesKilled = plugin.getConfig().getInt("skeliesKilled");
					if (totalSkeliesKilled == 30){						
						for (Entity entities : player.getWorld().getEntities()){
							if (entities instanceof Monster){
								((Monster) entities).remove();
							}
						}
						Bukkit.broadcastMessage(introMessage + " §bRound 2 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
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
							for (Entity entities : player.getWorld().getEntities()){
								if (entities instanceof Monster){
									((Monster) entities).remove();
								}
							}
							Bukkit.broadcastMessage(introMessage + " §bRound 4 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
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
		if (event.getEntityType() == org.bukkit.entity.EntityType.SPIDER){
			Random rand = new Random();
			int randomNumber = rand.nextInt(3) + 1;
			int spidersKilled = plugin.getConfig().getInt("spidersKilled");
			
			if (gameStarted == true){
				event.getDrops().clear();
				if (roundNumber == 3){
					plugin.getConfig().set("spidersKilled", spidersKilled + 1);
					plugin.saveConfig();
					int totalSpidersKilled = plugin.getConfig().getInt("spidersKilled");
					if (totalSpidersKilled == 40){
						player.setHealth(20);
						player.setFoodLevel(20);
						player.setSaturation(200);
						for (Entity entities : player.getWorld().getEntities()){
							if (entities instanceof Monster){
								((Monster) entities).remove();
							}
						}
						Bukkit.broadcastMessage(introMessage + " §bRound 3 is now complete! To start the next round, a staff needs to type §c/mm round§b!");
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
			}
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		String introMessage = plugin.getConfig().getString("introMessage");
		boolean gameStarted = plugin.getConfig().getBoolean("gameStarted");
		
		if (gameStarted == true){
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){	
				if (!(player.getInventory().getItemInHand().getTypeId() == 406)){
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
				return;
			}
			return;
		}
		return;
	}
}