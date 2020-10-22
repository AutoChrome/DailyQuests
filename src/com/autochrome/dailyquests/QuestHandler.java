package com.autochrome.dailyquests;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import com.garbagemule.MobArena.events.NewWaveEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class QuestHandler implements Listener {
	HashMap<UUID, AssignedQuest> playerQuestList;
	List<Quest> quests;
	FileConfiguration config;
	public QuestHandler(FileConfiguration config)
	{
		playerQuestList = new HashMap<UUID, AssignedQuest>();
		this.config = config;
		quests = new ArrayList<Quest>();
		loadQuests();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent joined)
	{
		if(!playerQuestList.containsKey(joined.getPlayer().getUniqueId())){
			giveQuest(joined.getPlayer());
		}else if(!playerQuestList.get(joined.getPlayer().getUniqueId()).getIsCompleted()){
			giveQuest(joined.getPlayer());
		}
	}
	
	@EventHandler
	public void BlockBreakEvent(BlockBreakEvent block)
	{
		//Check if player exists in the quest list
		if(playerQuestList.containsKey(block.getPlayer().getUniqueId())){
			//Check if the quest is relevant to the event.
			if(playerQuestList.get(block.getPlayer().getUniqueId()).getQuest().getType().toString().matches("BREAK")){
				AssignedQuest assignedQuest = playerQuestList.get(block.getPlayer().getUniqueId());
				//Check if player has already completed daily.
				if(!assignedQuest.getIsCompleted()){
					//Check if the current progress is less than the required amount
					if(assignedQuest.getProgress() < assignedQuest.getQuest().getConditionQuantity()){
						//Check if the event that has occurred meets the condition of the quest.
						if(block.getBlock().getType() == Material.getMaterial(assignedQuest.getQuest().getCondition().toUpperCase())){
							assignedQuest.setProgress(assignedQuest.getProgress() + 1);
							if(assignedQuest.getProgress() == assignedQuest.getQuest().getConditionQuantity()){
								assignedQuest.setIsCompleted(true);
								formatMessage(config.getString("messages.complete"), assignedQuest, block.getPlayer());
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), playerQuestList.get(block.getPlayer().getUniqueId()).getQuest().getReward().replace("%player%", block.getPlayer().getName()));
								block.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "" + ChatColor.BOLD + "Quest complete! Reward given."));
							}else{
								formatMessage(config.getString("messages.progress.messages"), assignedQuest, block.getPlayer());
							}
						}
					}else{
						assignedQuest.setIsCompleted(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void EntityDeathEvent(EntityDeathEvent death)
	{
		if(death.getEntity().getKiller() != null){
			Player player = death.getEntity().getKiller();
			
			if(playerQuestList.containsKey(player.getUniqueId())){
				if(playerQuestList.get(player.getUniqueId()).getQuest().getType().toString().toUpperCase().matches("KILL")){
					AssignedQuest assignedQuest = playerQuestList.get(player.getUniqueId());
					if(!assignedQuest.getIsCompleted()){
						if(death.getEntity().getName().toUpperCase().matches(assignedQuest.getQuest().getCondition().toUpperCase())){
							assignedQuest.setProgress(assignedQuest.getProgress() + 1);
							if(assignedQuest.getProgress() == assignedQuest.getQuest().getConditionQuantity()){
								assignedQuest.setIsCompleted(true);
								formatMessage(config.getString("messages.complete"), assignedQuest, player);
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), assignedQuest.getQuest().getReward().replace("%player%", player.getName()));
							}else{
								formatMessage(config.getString("messages.progress.messages"), assignedQuest, player);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void PlayerLevelChangeEvent(PlayerLevelChangeEvent level)
	{
		if(playerQuestList.containsKey(level.getPlayer().getUniqueId())){
			if(playerQuestList.get(level.getPlayer().getUniqueId()).getQuest().getType().toString().toUpperCase().matches("LEVELUP")){
				AssignedQuest assignedQuest = playerQuestList.get(level.getPlayer().getUniqueId());
				if(!assignedQuest.getIsCompleted()){
					if(assignedQuest.getProgress() < assignedQuest.getQuest().getConditionQuantity()){
						if(level.getNewLevel() > level.getOldLevel()){
							assignedQuest.setProgress(assignedQuest.getProgress() + 1);
							if(assignedQuest.getProgress() == assignedQuest.getQuest().getConditionQuantity()){
								assignedQuest.setIsCompleted(true);
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), assignedQuest.getQuest().getReward().replace("%player%", level.getPlayer().getName()));
								formatMessage(config.getString("messages.complete"), null, level.getPlayer());
							}else{
								formatMessage(config.getString("messages.progress.messages"), assignedQuest, level.getPlayer());
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void BrewEvent(BrewEvent brew)
	{
		
	}
	
	@EventHandler
	public void TameEvent(EntityTameEvent tame)
	{
		
	}
	
	@EventHandler
	public void CraftEvent(CraftItemEvent craft)
	{
		
	}
	
	@EventHandler
	public void FishEvent(PlayerFishEvent fish)
	{
		if(fish.getState().equals(State.CAUGHT_FISH)){
			System.out.println(fish.getCaught().getName());
			if(playerQuestList.containsKey(fish.getPlayer().getUniqueId())){
				if(playerQuestList.get(fish.getPlayer().getUniqueId()).getQuest().getType().toUpperCase().matches("FISH")){
					AssignedQuest assignedQuest = playerQuestList.get(fish.getPlayer().getUniqueId());
					if(!assignedQuest.getIsCompleted()){
						if(fish.getCaught().getName().replace(" ", "_").toUpperCase().matches(assignedQuest.getQuest().getCondition().toUpperCase())){
							assignedQuest.setProgress(assignedQuest.getProgress() + 1);
							if(assignedQuest.getProgress() == assignedQuest.getQuest().getConditionQuantity()){
								assignedQuest.setIsCompleted(true);
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), assignedQuest.getQuest().getReward().replace("%player%", fish.getPlayer().getName()));
								formatMessage(config.getString("messages.complete"), null, fish.getPlayer());
							}else{
								formatMessage(config.getString("messages.progress.messages"), assignedQuest, fish.getPlayer());
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void NewWaveEvent(NewWaveEvent wave)
	{
		for(Player player : wave.getArena().getPlayersInArena()){
			if(playerQuestList.containsKey(player.getUniqueId())){
				if(playerQuestList.get(player.getUniqueId()).getQuest().getType().toUpperCase().matches("WAVE")){
					AssignedQuest assignedQuest = playerQuestList.get(player.getUniqueId());
					System.out.println("Player has quest and finished wave.");
					
					if(!assignedQuest.getIsCompleted()){
						if(assignedQuest.getProgress() == assignedQuest.getQuest().getConditionQuantity()){
							assignedQuest.setIsCompleted(true);
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), assignedQuest.getQuest().getReward().replace("%player%", player.getName()));
							formatMessage(config.getString("messages.complete"), null, player);
						}else{
							formatMessage(config.getString("messages.progress.messages"), assignedQuest, player);
						}
					}
				}
			}
		}
	}
	
	public boolean addQuest(Quest quest)
	{
		quests.add(quest);
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String json = g.toJson(quests);
		File file = new File("plugins/DailyQuests/quests.json");
		try{
			FileWriter fw = new FileWriter(file);
			fw.write(json);
			fw.flush();
			fw.close();
			return true;
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void displayQuest(Player player)
	{
		if(playerQuestList.containsKey(player.getUniqueId())){
			if(!playerQuestList.get(player.getUniqueId()).getIsCompleted()){
				AssignedQuest quest = playerQuestList.get(player.getUniqueId());
				formatMessage(config.getString("messages.progress"), quest, player);
				
			}else{
				formatMessage(config.getString("messages.complete"), null, player);
			}
		}else{
			formatMessage(config.getString("messages.complete"), null, player);
		}
	}
	
	public void distributeQuest()
	{
		loadQuests();
		Bukkit.broadcastMessage(ChatColor.AQUA + "Daily quests have been distributed. There are: " + this.quests.size() + " total quests available.");
		playerQuestList.clear();
		for(Player player : Bukkit.getOnlinePlayers()){
			Random rand = new Random();
			Quest quest = quests.get(rand.nextInt(quests.size()));
			AssignedQuest assigned = new AssignedQuest(quest, false, 0);
			playerQuestList.put(player.getUniqueId(), assigned);
			formatMessage(config.getString("messages.distribute.chat"), assigned, player);
		}
	}
	
	public void giveQuest(Player player)
	{
		Random rand = new Random();
		Quest quest = quests.get(rand.nextInt(quests.size()));
		playerQuestList.put(player.getUniqueId(), new AssignedQuest(quest, false, 0));
	}
	
	public void loadQuests()
	{
		this.quests.clear();
		File file = new File("plugins/DailyQuests/quests.json");
		
		Gson gson = new Gson();
		Type questListType = new TypeToken<ArrayList<Quest>>(){}.getType();
		List<Quest> questList = new ArrayList<Quest>();
		try{
			questList = gson.fromJson(new FileReader(file), questListType);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		this.quests.addAll(questList);
	}
	
	public void formatMessage(String string, AssignedQuest quest, Player player)
	{
		if(quest != null){
			if(string.contains("%questType%"))
			{
				string = string.replace("%questType%", quest.getQuest().getType());
			}
			
			if(string.contains("%progress%"))
			{
				string = string.replace("%progress%", String.valueOf(quest.getProgress()));
			}
			
			if(string.contains("%questConditionQuantity%"))
			{
				string = string.replace("%questConditionQuantity%", String.valueOf(quest.getQuest().getConditionQuantity()));
			}
			
			if(string.contains("%questCondition%"))
			{
				string = string.replace("%questCondition%", quest.getQuest().getCondition());
			}
			
			if(string.contains("%reward%"))
			{
				string = string.replace("%reward%", quest.getQuest().getReward());
			}
			
			if(string.contains("%rewardDescription%"))
			{
				string = string.replace("%rewardDescription%", quest.getQuest().getRewardDescription());
			}
			if(quest.getQuest().getType().toUpperCase().matches("FISH")){
				switch(quest.getQuest().getCondition()){
				case "RAW_COD":
					string = string.replace("RAW_COD", "Cod");
					break;
				case "TROPICAL_FISH":
					string = string.replace("TROPICAL_FISH", "Tropical Fish");
					break;
				case "RAW_SALMON":
					string = string.replace("RAW_SALMON", "Salmon");
					break;
				}
			}
		}
		
		for(String message : string.split("%newline%")){
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
		
	}
}
