package com.autochrome.dailyquests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;

public class DailyQuestTabCompletion  implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> arguments = new ArrayList<>();
		if(args.length == 1){
			arguments.add("break");
			arguments.add("fish");
			arguments.add("kill");
			arguments.add("level");
			arguments.add("wave");
		}
		
		if(args.length == 2){
			switch(args[0].toLowerCase()){
			case "break":
				for(Material materialType : Material.values()){
					if(args[1].length() < 1){
						arguments.add(materialType.name());	
					}else{
						if(materialType.name().toUpperCase().startsWith(args[1].toUpperCase())){
							arguments.add(materialType.name());	
						}
					}
				}
				break;
			case "place":
				for(Material materialType : Material.values()){
					if(args[1].length() < 1){
						arguments.add(materialType.name());	
					}else{
						if(materialType.name().toUpperCase().startsWith(args[1].toUpperCase())){
							arguments.add(materialType.name());	
						}
					}
				}
				break;
			case "kill":
				for(EntityType entityType : EntityType.values()){
					if(args[1].length() < 1){
						arguments.add(entityType.name());	
					}else{
						if(entityType.name().toUpperCase().startsWith(args[1].toUpperCase())){
							arguments.add(entityType.name());	
						}
					}
				}
				break;
			case "pickup":
				for(Material materialType : Material.values()){
					if(args[1].length() < 1){
						arguments.add(materialType.name());	
					}else{
						if(materialType.name().toUpperCase().startsWith(args[1].toUpperCase())){
							arguments.add(materialType.name());	
						}
					}
				}
				break;
			case "level":
				arguments.add("gain");
				break;
			case "fish":
				arguments.add("RAW_COD");
				arguments.add("RAW_SALMON");
				arguments.add("TROPICAL_FISH");
				arguments.add("PUFFERFISH");
				break;
			case "wave":
				arguments.add("survive");
				break;
			}
		}
		
		if(args.length == 3){
			arguments.add("1");
			arguments.add("16");
			arguments.add("32");
			arguments.add("64");
			return arguments;
		}
		
		if(args.length == 4){
			arguments.add("give");
			arguments.add("xp");
			arguments.add("eco");
			arguments.add("crate");
		}

		if(args.length == 5){
			switch(args[3]){
			case "give":
				for(Material materialType : Material.values()){
					if(args[4].length() < 1){
						arguments.add(materialType.name());	
					}else{
						if(materialType.name().toUpperCase().startsWith(args[4].toUpperCase())){
							arguments.add(materialType.name());	
						}
					}
				}
				break;
			case "xp":
				arguments.add("give");
				break;
			case "eco":
				arguments.add("give");
				break;
			case "crate":
				arguments.add("givekey");
				break;
			}
		}
		
		if(args.length == 6){
			switch(args[3]){
			case "xp":
				arguments.add("1");
				arguments.add("16");
				arguments.add("32");
				arguments.add("64");
				break;
			case "eco":
				arguments.add("1");
				arguments.add("16");
				arguments.add("32");
				arguments.add("64");
				break;
			case "give":
				arguments.add("1");
				arguments.add("16");
				arguments.add("32");
				arguments.add("64");
				break;
			case "crate":
				File dataFolder = new File("plugins/GoldenCrates/keys");
				File[] files = dataFolder.listFiles();
				for (File file : files) {
					if(file.getName().substring(file.getName().length() - 4).matches(".yml")){
						arguments.add(file.getName().substring(0, file.getName().length() - 4));
					}else{
						arguments.add(file.getName());
					}
				}
				break;
			}

		}
		
		return arguments;
	}

}
