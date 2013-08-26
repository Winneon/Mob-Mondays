package io.github.winneonsword.mm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class mm extends JavaPlugin {
	
	mm plugin;
	
	public mm(){
		
	}
	
	public final listeners Listener = new listeners(this);
	
	@Override
	public void onEnable(){
		getLogger().info("MM has been enabled! Please set up your options in the provided config.yml (if you haven't already).");
		getServer().getPluginManager().registerEvents(this.Listener, this);
		getCommand("mm").setExecutor(new mm_command(this));
		saveDefaultConfig();
	}

	@Override
	public void onDisable(){
		getLogger().info("MM has been disabled!");
	}
	
}
class Task implements Runnable{
	private mm plugin;
	private List<String> list;
	private int count = 0;
	
	public Task(mm plugin, List<String> list){
		this.plugin = plugin;
		this.list = list;
	}
	
	public void run(){
		sendInfoMessage(" " + list.get(count++));
		if (count < list.size()){
			plugin.getServer().getScheduler().runTaskLater(plugin, this, 100);
		}
	}
	
	public void sendInfoMessage(String message){
		String introMessage = plugin.getConfig().getString("introMessage");
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			Player p = Bukkit.getPlayer(mmPlayers.get(i));
			p.sendMessage(introMessage + message);
		}
	}
}
class Task2 implements Runnable{
	private mm plugin;
	private List<String> list;
	private int count = 0;
	
	public Task2(mm plugin, List<String> list){
		this.plugin = plugin;
		this.list = list;
	}
	
	public void run(){
		sendInfoMessage(" " + list.get(count++));
		if (count < list.size()){
			plugin.getServer().getScheduler().runTaskLater(plugin, this, 40);
		}
	}
	
	public void sendInfoMessage(String message){
		String introMessage = plugin.getConfig().getString("introMessage");
		List<String> mmPlayers = plugin.getConfig().getStringList("MM.players");
		for (int i = 0; i < mmPlayers.size(); i++){
			Player p = Bukkit.getPlayer(mmPlayers.get(i));
			p.sendMessage(introMessage + message);
		}
	}
}