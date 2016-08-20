package com.gmail.plundermc.MovingCreatures;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Commands {

	@SuppressWarnings("unused")
	private JavaPlugin plugin;

	public Commands(JavaPlugin j) {
		this.plugin = j;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}
}
