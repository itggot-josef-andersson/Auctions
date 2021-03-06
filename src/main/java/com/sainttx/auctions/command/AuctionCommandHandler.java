/*
 * Copyright (C) SainttX <http://sainttx.com>
 * Copyright (C) contributors
 *
 * This file is part of Auctions.
 *
 * Auctions is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Auctions is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Auctions.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sainttx.auctions.command;

import com.sainttx.auctions.AuctionPlugin;
import com.sainttx.auctions.command.subcommand.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles command distribution for the auction plugin
 */
public class AuctionCommandHandler implements CommandExecutor, Listener {

    private AuctionPlugin plugin;
    private Set<AuctionSubCommand> commands = new HashSet<AuctionSubCommand>();

    /**
     * Constructor. Initializes all subcommands.
     */
    public AuctionCommandHandler(AuctionPlugin plugin) {
        this.plugin = plugin;
        this.addCommand(new BidCommand(plugin));
        this.addCommand(new CancelCommand(plugin));
        this.addCommand(new EndCommand(plugin));
        this.addCommand(new IgnoreCommand(plugin));
        this.addCommand(new ImpoundCommand(plugin));
        this.addCommand(new InfoCommand(plugin));
        this.addCommand(new ReloadCommand(plugin));
        this.addCommand(new SpamCommand(plugin));
        this.addCommand(new StartCommand(plugin));
        this.addCommand(new ToggleCommand(plugin));
        this.addCommand(new QueueCommand(plugin));
    }

    /**
     * Registers a sub command
     *
     * @param command the command
     */
    public void addCommand(AuctionSubCommand command) {
        commands.add(command);
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (plugin.equals(event.getPlugin())) {
            commands.clear();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && !command.getName().equalsIgnoreCase("bid")) {
            sendMenu(sender);
        } else {
            String sub;

            if (command.getName().equalsIgnoreCase("bid")) {
                sub = "bid";
                args = concat(new String[]{ "auction" }, args);
            } else {
                sub = args[0];
            }

            for (AuctionSubCommand cmd : commands) {
                if (cmd.canTrigger(sub)) {
                    if (!sender.hasPermission("auctions.command.*") && !sender.hasPermission(cmd.getPermission())) {
                        plugin.getMessageHandler().sendMessage(
                                sender, plugin.getMessage("messages.error.insufficientPermissions"));
                    } else {
                        cmd.onCommand(sender, command, label, args);
                    }
                    return true;
                }
            }

            sendMenu(sender);
        }
        return true;
    }

    /*
     * A helper method to concatenate 2 arrays
     */
    private <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * Sends the auction menu to a sender
     *
     * @param sender The sender to send the menu too
     */
    public void sendMenu(CommandSender sender) {
        for (String message : plugin.getConfig().getStringList("messages.helpMenu")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
