package me.itsjbey.redgru;

import me.itsjbey.redgru.commands.RedGRUCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedGRU extends JavaPlugin {

	public static final String PREFIX = "§7[§4Red§rGRU§7] ";

	@Override
	public void onEnable() {
		Bukkit.getPluginCommand("redgru").setExecutor(new RedGRUCommand());
	}

	@Override
	public void onDisable() {
	}
}
