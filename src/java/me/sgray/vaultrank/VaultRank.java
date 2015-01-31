package me.sgray.vaultrank;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultRank extends JavaPlugin {
    Permission perms;

    public void onEnable() {
        hookVaultPerms();
        getCommand("vaultrank").setExecutor(this);
        saveDefaultConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();
        if (cmdName.equals("vaultrank")) {
            if (args.length == 0 && !isConsole(sender)) {
                String rank = perms.getPrimaryGroup((Player) sender);
                sender.sendMessage(msgPrefix() + "Your rank is " + String.valueOf(rank));
            } else if (args.length == 0 && sender.hasPermission("vaultrank.admin")) {
                sender.sendMessage(getDescription().getName() + " " + getDescription().getVersion());
            } else if (args.length == 1) {
                if (args[0].equals("reload") && sender.hasPermission("vaultrank.admin")) {
                    reloadConfig();
                } else if (sender.hasPermission("vaultrank.others") && args[0].length() < 17) {
                    String rank = perms.getPrimaryGroup((String) null, args[0]);
                    if (rank == null) {
                        sender.sendMessage(msgPrefix() + "Could not find a group!");
                    } else {
                        sender.sendMessage(msgPrefix() + "The rank of " + args[0] + " is " + String.valueOf(rank));
                    }
                } else {
                    String others = (sender.hasPermission("vaultrank.others")) ? " [playername]" : "";
                    sender.sendMessage(msgPrefix() + "Please use the syntax " + ChatColor.GREEN + "/getrank" + others);
                }
            } else {
                String others = (sender.hasPermission("vaultrank.others")) ? " [playername]" : "";
                sender.sendMessage(msgPrefix() + "Please use the syntax " + ChatColor.GREEN + "/getrank" + others);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean hookVaultPerms() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private String msgPrefix() {
        return (ChatColor.translateAlternateColorCodes((char) '&', getConfig().getString("prefix", "VaultRank")));
    }

    private boolean isConsole(CommandSender sender) {
        return !(sender instanceof Player) ? true : false;
    }
}
