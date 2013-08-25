package io.github.winneonsword.mm.API;

import org.bukkit.ChatColor;

public class ChatAPI {
	
	public String replaceAmpersand(String message){
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}
	
}
