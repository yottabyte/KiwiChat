
package com.yottabyte.bukkit;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * Chat plugin
 *
 * @author yottabyte
 */

public class KiwiChat extends JavaPlugin {
	
    public static String name = "KiwiChat";
    public static String codename = "Troll";
    public static String version = "0.4";
	
	public static final Logger log = Logger.getLogger("Minecraft");
	
	static Permissions CurrentPermissions = null;
	
    private final KiwiChatPlayerListener playerListener = new KiwiChatPlayerListener(this);

    public KiwiChat(PluginLoader pluginLoader, Server instance,
            PluginDescriptionFile desc, File folder, File plugin,
            ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
    }
        // TODO: Place any custom initialisation code here
        // NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled

	public void setupPermissions() {
		Plugin plugin = this.getServer().getPluginManager().getPlugin("Permissions");

		if (KiwiChat.CurrentPermissions == null) {
			// Permission plugin already registered
			return;
		}
		
		if (plugin != null) {
			KiwiChat.CurrentPermissions = (Permissions) plugin;
		} else {
			log.log(Level.CONFIG, "Permissions plugin is required for this plugin to work. Disabling plugin");
			this.getServer().getPluginManager().disablePlugin(this);
		}
	}
    
    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a plugin is disabled

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        System.out.println("Goodbye world! Local chat is going to sleep! :(");
    }

    public void onEnable() {
        // TODO: Place any custom enable code here including the registration of any events

        // Register our events
        PluginManager pm = getServer().getPluginManager();
       // pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
       // pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Monitor, this);
       // pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
       // pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
       // pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
}
