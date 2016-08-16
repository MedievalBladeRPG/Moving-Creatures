package com.gmail.plundermc.MovingCreatures;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Events implements Listener {

	private JavaPlugin plugin;

	public Events(JavaPlugin j) {
		plugin = j;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onStick(PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand().getType().equals(Material.STICK) && event.getAction().toString().contains("RIGHT")) {
			Player p = event.getPlayer();
			FloatingBlock b = new FloatingBlock(p.getEyeLocation(), new ItemStack(Material.STONE, (short) 2));
			plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
				b.setVelocity(p.getLocation().getDirection().normalize().multiply(0.1));
			}, 0, 2);
		}
	}
}
