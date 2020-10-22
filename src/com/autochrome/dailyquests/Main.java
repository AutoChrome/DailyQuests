package com.autochrome.dailyquests;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	private QuestHandler questHandler;
	public void onEnable() {
		this.saveDefaultConfig();
		FileConfiguration config = this.getConfig();
		saveConfig();
		this.questHandler = new QuestHandler(config);
		
		Bukkit.getScheduler().runTaskTimer(this, new Runnable(){
			@Override
			public void run() {
				long diff = 0;
				
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				//Get the difference between current time and 11:59PM. Convert to seconds.
				try {
					diff = (format.parse(java.time.LocalTime.now().toString()).getTime() - format.parse("23:59:00").getTime()) / 1000;
					System.out.println(diff);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//Check if the difference is within the 11:59 minute.
				if(diff > -1){
					questHandler.distributeQuest();
				}
			}
		}, 0L, config.getLong("timer.delay")*20);
		getCommand("addDaily").setExecutor(this);
		getCommand("addDaily").setTabCompleter(new DailyQuestTabCompletion());
		getServer().getPluginManager().registerEvents(this.questHandler, this);
	}
	
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("addDaily")){
			if(sender.hasPermission("dailyquest.manage")){
				if(args[3].equalsIgnoreCase("give")){
					String description = "";
					for(int i = 6; i < args.length; i++){
						description += args[i] + " ";
					}
					Quest quest = new Quest(args[0], args[1], Integer.parseInt(args[2]), args[3] + " %player% " + args[4] + " " + args[5], description.substring(0, description.length() - 1));
					if(questHandler.addQuest(quest)){
						sender.sendMessage(ChatColor.GREEN + "Quest successfully added!");
					}else{
						sender.sendMessage(ChatColor.DARK_RED + "An issue occurred!");
					}
					return true;
				}else if(args[3].equalsIgnoreCase("xp")){
					String description = "";
					for(int i = 6; i < args.length; i++){
						description += args[i] + " ";
					}
					Quest quest = new Quest(args[0], args[1], Integer.parseInt(args[2]), args[3] + " " + args[4] + " %player% " + args[5], description.substring(0, description.length() - 1));
					if(questHandler.addQuest(quest)){
						sender.sendMessage(ChatColor.GREEN + "Quest successfully added!");
					}else{
						sender.sendMessage(ChatColor.DARK_RED + "An issue occurred!");
					}
					return true;
				}else if(args[3].equalsIgnoreCase("eco")){
					String description = "";
					for(int i = 6; i < args.length; i++){
						description += args[i] + " ";
					}
					Quest quest = new Quest(args[0], args[1], Integer.parseInt(args[2]), args[3] + " " + args[4] + " %player% " + args[5], description.substring(0, description.length() - 1));
					if(questHandler.addQuest(quest)){
						sender.sendMessage(ChatColor.GREEN + "Quest successfully added!");
					}else{
						sender.sendMessage(ChatColor.DARK_RED + "An issue occurred!");
					}
					return true;
				}else if(args[3].equalsIgnoreCase("crate")){
					String description = "";
					for(int i = 6; i < args.length; i++){
						description += args[i] + " ";
					}
					Quest quest = new Quest(args[0], args[1], Integer.parseInt(args[2]), args[3] + " " + args[4] + " %player% " + args[5], description.substring(0, description.length() - 1));
					if(questHandler.addQuest(quest)){
						sender.sendMessage(ChatColor.GREEN + "Quest successfully added!");
					}else{
						sender.sendMessage(ChatColor.DARK_RED + "An issue occurred!");
					}
					return true;
				}else{
					sender.sendMessage(ChatColor.RED + "Sorry, you have entered the command incorrectly. Please try again.");
					return false;
				}
			}else{
				sender.sendMessage(ChatColor.DARK_RED + "Sorry! You do not have permission to use that command.");
			}
		}else if(cmd.getName().equalsIgnoreCase(("distributeDaily"))){
			if(sender.hasPermission("dailyquest.manage")){
				questHandler.distributeQuest();
				sender.sendMessage(ChatColor.GREEN + "Quests have been distributed.");
			}else{
				sender.sendMessage(ChatColor.DARK_RED + "Sorry! You do not have permission to use that command.");
			}
		}else if(cmd.getName().equalsIgnoreCase("quest")){
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "You must be a player to do this command!");
				return false;
			}
			Player player = Bukkit.getPlayer(sender.getName());
			questHandler.displayQuest(player);
		}
		return false;
	}
}
