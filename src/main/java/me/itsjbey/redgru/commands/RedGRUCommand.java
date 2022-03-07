package me.itsjbey.redgru.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.gamemode.GameMode;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.itsjbey.redgru.RedGRU;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class RedGRUCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof ConsoleCommandSender) {
			sender.sendMessage(RedGRU.PREFIX + "§c§lFehler: §7Dieser Befehl kann nur von Spielern ausgeführt werden!");
			return true;
		}
		if(!sender.hasPermission("rwm.redgru")) {
			sender.sendMessage(RedGRU.PREFIX + "§c§lFehler: §7Du hast keine Rechte für diesen Befehl!");
			return true;
		}
		sender.sendMessage(RedGRU.PREFIX + "§f§7Positions-Information:");
		Player p = (Player) sender;
		World bukkitWorld = BukkitAdapter.adapt(p.getWorld());
		BukkitPlayer bukkitPlayer = BukkitAdapter.adapt(p);
		RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(bukkitWorld);
		if(manager == null) {
			sender.sendMessage(RedGRU.PREFIX + "§c§lFehler: §7Die Welt wird nicht durch WorldGuard verwaltet!");
			return true;
		}

		LocalPlayer wgPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
		ApplicableRegionSet set = manager.getApplicableRegions(bukkitPlayer.getBlockLocation().toVector().toBlockPoint());
		sender.sendMessage(RedGRU.PREFIX + "§7- Du stehst in §f§l" + set.getRegions().size() + " §7Region(en).");

		ProtectedRegion finalRegion = null;
		GameMode finalGameMode = null;
		int highestPriority = 0;
		for(ProtectedRegion region : set.getRegions()) {
			GameMode flagGameMode;
			if ((region.getPriority() > highestPriority || finalRegion == null)
					&& (flagGameMode = region.getFlag(Flags.GAME_MODE)) != null) {
				highestPriority = region.getPriority();
				finalRegion = region;
				finalGameMode = flagGameMode;
			}
		}
		if(finalRegion == null) {
			sender.sendMessage(RedGRU.PREFIX + "§c§lFehler: §7Keine Regionen mit einem GameMode-Flag gefunden!");
			return true;
		}
		p.sendMessage(RedGRU.PREFIX + "§7- Region mit höchster Priorität (" + highestPriority + "): §f§l" + finalRegion.getId());
		p.sendMessage(RedGRU.PREFIX + "§7- GameMode in Region: §f§l" + finalGameMode.getName());
		return true;
	}
}
