
package com.yottabyte.bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.entity.Player;

import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * Some various chat stuff.
 * @author yottabyte
 */
public class KiwiChatPlayerListener extends PlayerListener {
	Map<Player,Long> playerTimes = new HashMap<Player,Long>();
    private final KiwiChat plugin;
    public KiwiChatPlayerListener(KiwiChat instance) {
        plugin = instance;
    }
    
    public static String combineSplit(int startIndex, String[] string, String seperator) {
        StringBuilder builder = new StringBuilder();

        for (int i = startIndex; i < string.length; i++) {
            builder.append(string[i]);
            builder.append(seperator);
        }

        builder.deleteCharAt(builder.length() - seperator.length()); // remove
        return builder.toString();
    }
    
    private double getDistance(org.bukkit.entity.Player a, org.bukkit.entity.Player b) {
	    	Location loca = a.getLocation();
	    	Location locb = b.getLocation();
	    				
	    	double xPart = Math.pow(loca.getX() - locb.getX(), 2.0D);
	    	double yPart = Math.pow(loca.getY() - locb.getY(), 2.0D);
	    	double zPart = Math.pow(loca.getZ() - locb.getZ(), 2.0D);
	    	return Math.sqrt(xPart + yPart + zPart);
	    	}
	    	
	private boolean isLocal(org.bukkit.entity.Player a, org.bukkit.entity.Player b) {
		 	return getDistance(a, b) <= 50;
    }
    
	    	
    @Override
    public void onPlayerCommand(PlayerChatEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        
        if(event.getMessage().startsWith("/me")){
        	event.setCancelled(true);
        }
/*
        if(event.getMessage().startsWith("/do")){
        	String msg = event.getMessage().substring(4);
        	for(Player p : plugin.getServer().getOnlinePlayers()) p.sendMessage("*§e" + msg + " §6(" + player.getName() + ")");      	
        }
  */
        if(event.getMessage().startsWith("/kick")){
        	if (Permissions.Security.permission(event.getPlayer(), "kiwichat.kick")) {
        		String fullMsg[] = event.getMessage().split(" ");
        		if (fullMsg.length > 1) {
	        		String p = fullMsg[1];
	        		Player victim = plugin.getServer().getPlayer(p);
	        		if(victim != null){
	        			if(fullMsg.length < 3){
	        				victim.kickPlayer("You have been kicked by " + player.getName() + ".");
	        			}else{
	        				String reason = combineSplit(2, fullMsg, " ");
	        				victim.kickPlayer("You have been kicked by " + player.getName() + ". Reason: " + reason);
	        			}
	        		}else{
	        			player.sendMessage("§cKick failed: " + p + " isn't online.");
	        		}
        		}else{
        			player.sendMessage("§eUsage: /kick [player] (reason)");
        		}
        	}
        }
        if(event.getMessage().startsWith("/bc")){
        	if (Permissions.Security.permission(event.getPlayer(), "kiwichat.bc")) {
	        	String msg = event.getMessage().substring(4);
	        	
	        	// CHECK IF THE PLAYER HAS USED /bc BEFORE
	        	Long outPlayerTime = playerTimes.get(player);
	        	long diff;
	        	if(outPlayerTime != null){
	        		diff = System.currentTimeMillis() - outPlayerTime;
	        	}else{
	        		diff = 1*60*1000+1;
	        	}
		        	
		        if(diff > 1*60*1000){
		        	plugin.getServer().broadcastMessage("§7[[§e"+player.getName()+ "§7]] §f" + msg); 
		        	playerTimes.put(player, System.currentTimeMillis()); // update the time variable for the player
		        }else{
            		long timeLeft = (1*60*1000)-diff;
            		String formatTime = String.format("%d seconds",TimeUnit.MILLISECONDS.toSeconds(timeLeft));
		        	player.sendMessage("§eYou have to wait " + formatTime + " before you can broadcast again!");
	        	}
	        //player doesn't have privilege to use /bc
        	}else{
        	player.sendMessage("§cYou can't use the /bc command!");
        	}
        	
        }
    }
    @Override
    public void onPlayerChat(PlayerChatEvent event){
    final Logger log = Logger.getLogger("Minecraft");
    org.bukkit.entity.Player player = event.getPlayer();
    String group = Permissions.Security.getGroup(player.getName());
    String prefix = Permissions.Security.getGroupPrefix(group);
    if(prefix == null)
    {
    	prefix="";
    }
   
	log.info("L:" + player.getName() +": "+ event.getMessage());
        for (org.bukkit.entity.Player p : plugin.getServer().getOnlinePlayers()) {
        	if (isLocal(player, p)) {
        	p.sendMessage(prefix + "<" + player.getName()+ "> " + event.getMessage());
        	}else{
        		event.setCancelled(true);
        	}
       }
        event.setCancelled(true);
    	
    }
}
