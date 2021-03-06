package br.net.fabiozumbi12.PixelVip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class PVConfig {	
	
	private PixelVip plugin;
	private YamlConfiguration vipsFile;
	private int delay = 0;
	
	public PVConfig(PixelVip plugin, String defDir, File defConfig){
		this.plugin = plugin;
		
		//load vips.yml file
		reloadVips();
		
		/*----------------------------------------------------------------------------------*/
		
		plugin.getConfig().options().header("=============== PixelVip Configuration Options ================\n"
				+ "\n"
				+ "This is the default configuration and some information about some configurations.\n"
				+ "\n"
				+ "In \"groups\" on \"commands\" and \"cmdChances\"(Lists) you can use this placeholders:\n"
				+ "- {p} = Players Name\n"
				+ "- {vip} = Vip Group\n"
				+ "- {playergroup} = Player Group before Vip activation\n"
				+ "- {days} = Days of activated Vip\n"
				+ "\n"
				+ "In \"groups\" > \"cmdChances\"(List) you can add commands to run based on a % chance. \n"
				+ "Use numbers below 0-100 like the example on \"vip1\".\n"
				+ "\n"
				+ "In \"configs\" > \"cmdOnRemoveVip\"(String) you can use this placeholders:\n"
				+ "- {p} = Player Name\n"
				+ "- {vip} = Name of Vip Removed\n"
				+ "\n"
				+ "In \"configs\" > \"commandsToRunOnChangeVip\"(List) you can use this placeholders:\n"
				+ "- {p} = Player Name\n"
				+ "- {newvip} = Name of Vip the player is changing to\n"
				+ "- {oldvip} = Name of Vip the player is changing from\n"
				+ "\n"
				+ "In \"configs\" > \"commandsToRunOnVipFinish\"(List) you can use this placeholders:\n"
				+ "- {p} = Player Name\n"
				+ "- {vip} = Name of Vip\n"
				+ "- {playergroup} = Player Group before Vip activation\n"
				+ "\n"
				+ "");
				
        if (!plugin.getConfig().contains("groups")){
        	plugin.getConfig().set("groups.vip1.essentials-kit", "vip1");
        	plugin.getConfig().set("groups.vip1.commands", Arrays.asList("broadcast &aThe player &6{p} &ahas acquired your &6{vip} &afor &6{days} &adays","give {p} minecraft:diamond 10", "eco give {p} 10000"));
        	plugin.getConfig().set("groups.vip1.cmdChances.50", Arrays.asList("give {p} minecraft:diamond_block 5"));
        	plugin.getConfig().set("groups.vip1.cmdChances.30", Arrays.asList("give {p} minecraft:mob_spawner 1"));        	
        } 
                
        plugin.getConfig().set("configs.key-size", getObj(10,"configs.key-size"));
        plugin.getConfig().set("configs.useVault-toChangePlayerGroup", getObj(true ,"configs.useVault-toChangePlayerGroup"));
        plugin.getConfig().set("configs.cmdToReloadPermPlugin", getObj("pex reload","configs.cmdToReloadPermPlugin"));
        plugin.getConfig().set("configs.cmdOnRemoveVip", getObj("","configs.cmdOnRemoveVip"));        
        plugin.getConfig().set("configs.commandsToRunOnVipFinish", getObj(new ArrayList<String>(), "configs.commandsToRunOnVipFinish"));
        plugin.getConfig().set("configs.commandsToRunOnChangeVip", getObj(new ArrayList<String>(),"configs.commandsToRunOnChangeVip"));
        plugin.getConfig().set("configs.queueCmdsForOfflinePlayers", getObj(false,"configs.queueCmdsForOfflinePlayers"));
        plugin.getConfig().set("bungee.enableSync", getObj(false,"bungee.enableSync"));
        plugin.getConfig().set("bungee.serverID", getObj("server1","bungee.serverID"));
                
        if (!plugin.getConfig().contains("keys")){
        	plugin.getConfig().set("keys", new ArrayList<String>());
        }        
        if (!plugin.getConfig().contains("itemKeys")){
        	plugin.getConfig().set("itemKeys", new ArrayList<String>());
        }        	
        
        if (getListString("configs.commandsToRunOnVipFinish").size() == 0){	        	
        	plugin.getConfig().set("configs.commandsToRunOnVipFinish", 
        			Arrays.asList("nick {p} off"));
        }   
        
        //strings
        plugin.getConfig().set("strings._pluginTag", getObj("&7[&6PixelVip&7] ","strings._pluginTag"));	
        plugin.getConfig().set("strings.noPlayersByName", getObj("&cTheres no players with this name!","strings.noPlayersByName"));	
        plugin.getConfig().set("strings.onlyPlayers", getObj("&cOnly players ca use this command!","strings.onlyPlayers"));	
        plugin.getConfig().set("strings.noKeys", getObj("&aTheres no available keys! Use &6/newkey &aor &6/newikey &ato generate one.","strings.noKeys"));
        plugin.getConfig().set("strings.listKeys", getObj("&aList of Keys:","strings.listKeys"));
        plugin.getConfig().set("strings.listItemKeys", getObj("&aList of Item Keys:","strings.listItemKeys"));
        plugin.getConfig().set("strings.vipInfoFor", getObj("&aVip info for ","strings.vipInfoFor"));
        plugin.getConfig().set("strings.playerNotVip", getObj("&cThis player(or you) is not VIP!","strings.playerNotVip"));
        plugin.getConfig().set("strings.moreThanZero", getObj("&cThis number need to be more than 0","strings.moreThanZero"));
        plugin.getConfig().set("strings.noGroups", getObj("&cTheres no groups with name ","strings.noGroups"));
        plugin.getConfig().set("strings.keyGenerated", getObj("&aGenerated a key with the following:","strings.keyGenerated"));
        plugin.getConfig().set("strings.invalidKey", getObj("&cThis key is invalid or not exists!","strings.invalidKey"));
        plugin.getConfig().set("strings.vipActivated", getObj("&aVip activated with success:","strings.vipActivated"));
        plugin.getConfig().set("strings.usesLeftActivation", getObj("&b This key can be used for more: &6{uses} &btimes.","strings.usesLeftActivation"));
        plugin.getConfig().set("strings.activeVip", getObj("&b- Vip: &6{vip}","strings.activeVip"));
        plugin.getConfig().set("strings.activeDays", getObj("&b- Days: &6{days} &bdays","strings.activeDays"));	
        plugin.getConfig().set("strings.timeLeft", getObj("&b- Time left: &6","strings.timeLeft"));	        
        plugin.getConfig().set("strings.totalTime", getObj("&b- Days: &6","strings.totalTime"));
        plugin.getConfig().set("strings.timeKey", getObj("&b- Key: &6","strings.timeKey"));	        	        
        plugin.getConfig().set("strings.timeGroup", getObj("&b- Vip: &6","strings.timeGroup"));
        plugin.getConfig().set("strings.timeActive", getObj("&b- In Use: &6","strings.timeActive"));
        plugin.getConfig().set("strings.infoUses", getObj("&b- Uses left: &6","strings.infoUses"));
        plugin.getConfig().set("strings.activeVipSetTo", getObj("&aYour active VIP is ","strings.activeVipSetTo"));
        plugin.getConfig().set("strings.noGroups", getObj("&cNo groups with name &6","strings.noGroups"));
        plugin.getConfig().set("strings.days", getObj(" &bdays","strings.days"));
        plugin.getConfig().set("strings.hours", getObj(" &bhours","strings.hours"));
        plugin.getConfig().set("strings.minutes", getObj(" &bminutes","strings.minutes"));
        plugin.getConfig().set("strings.and", getObj(" &band","strings.and"));
        plugin.getConfig().set("strings.vipEnded", getObj(" &bYour vip &6{vip} &bhas ended. &eWe hope you enjoyed your Vip time &a:D","strings.vipEnded"));
        plugin.getConfig().set("strings.lessThan", getObj("&6Less than one minute to end your vip...","strings.lessThan"));
        plugin.getConfig().set("strings.vipsRemoved", getObj("&aVip(s) of player removed with success!","strings.vipsRemoved"));
        plugin.getConfig().set("strings.vipSet", getObj("&aVip set with success for this player!","strings.vipSet"));
        plugin.getConfig().set("strings.sync-groups", getObj("&aGroup configs send to all servers!","strings.sync-groups"));
        plugin.getConfig().set("strings.list-of-vips", getObj("&aList of active VIPs: ","strings.list-of-vips"));
        plugin.getConfig().set("strings.vipAdded", getObj("&aVip added with success for this player!","strings.vipAdded"));
        plugin.getConfig().set("strings.item", getObj("&a-- Item: &b","strings.item"));
        plugin.getConfig().set("strings.itemsGiven", getObj("&aGiven {items} item(s) using a key.","strings.itemsGiven"));
        plugin.getConfig().set("strings.itemsAdded", getObj("&aItem(s) added to key:","strings.itemsAdded"));
        plugin.getConfig().set("strings.keyRemoved", getObj("&aKey removed with success: &b","strings.keyRemoved"));
        plugin.getConfig().set("strings.noKeyRemoved", getObj("&cTheres no keys to remove!","strings.noKeyRemoved"));
        
        /*---------------------------------------------------------*/
        //move vips to new file if is in config.yml
        
        if (plugin.getConfig().contains("activeVips")){
        	plugin.logger.warning("Active Vips moved to file 'vips.yml'");
        	for (String key:plugin.getConfig().getKeys(true)){
        		if (key.startsWith("activeVips.")){
        			vipsFile.set(key, plugin.getConfig().get(key));
        		}
        	}
        	plugin.getConfig().set("activeVips", null);
        	saveVips();
        }
        
        /*---------------------------------------------------------*/
        
        plugin.saveConfig();
	}
	
	public YamlConfiguration getVips(){
		return vipsFile;
	}
	
	public void reloadVips() {
		File fileVips = new File(plugin.getDataFolder()+File.separator+"vips.yml");
		if (fileVips.exists()){
			try {
				fileVips.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		vipsFile = YamlConfiguration.loadConfiguration(fileVips);
		saveVips();
	}	

	public void saveVips(){
		File fileVips = new File(plugin.getDataFolder()+File.separator+"vips.yml");
		try {
			vipsFile.save(fileVips);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean bungeeSyncEnabled(){
		return getBoolean(false, "bungee.enableSync");
	}
	
	public boolean queueCmds(){
		return getBoolean(false, "configs.queueCmdsForOfflinePlayers");
	}
	
	public List<String> getQueueCmds(String uuid){
		List<String> cmds = new ArrayList<String>();
		if (plugin.getConfig().contains("joinCmds."+uuid+".cmds")){
			cmds.addAll(plugin.getConfig().getStringList("joinCmds."+uuid+".cmds"));
		}
		if (plugin.getConfig().contains("joinCmds."+uuid+".chanceCmds")){
			cmds.addAll(plugin.getConfig().getStringList("joinCmds."+uuid+".chanceCmds"));
		}
		plugin.getConfig().set("joinCmds."+uuid, null);
		plugin.saveConfig();
		return cmds;
	}
	
	private void setJoinCmds(String uuid, List<String> cmds, List<String> chanceCmds) {
		plugin.getConfig().set("joinCmds."+uuid+".cmds", cmds);
		plugin.getConfig().set("joinCmds."+uuid+".chanceCmds", chanceCmds);
		plugin.saveConfig();
	}
	
	public void addKey(String key, String group, long millis, int uses){
		plugin.getConfig().set("keys."+key+".group", group);
		plugin.getConfig().set("keys."+key+".duration", millis);
		plugin.getConfig().set("keys."+key+".uses", uses);
		saveConfigAll();
	}
	
	public void addItemKey(String key, List<String> cmds){
		cmds.addAll(plugin.getConfig().getStringList("itemKeys."+key+".cmds"));
		plugin.getConfig().set("itemKeys."+key+".cmds", cmds);
		saveConfigAll();
	}
	
	private void saveConfigAll(){
		saveVips();
		plugin.saveConfig();
		plugin.getPVBungee().sendBungeeSync();
	}
	
	public boolean delItemKey(String key){	
		if (plugin.getConfig().contains("itemKeys."+key)){
			plugin.getConfig().set("itemKeys."+key, null);
			saveConfigAll();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean delKey(String key, int uses){	
		if (plugin.getConfig().contains("keys."+key)){
			if (uses <= 1){
				plugin.getConfig().set("keys."+key, null);
			} else {
				plugin.getConfig().set("keys."+key+".uses", String.valueOf(uses-1));
			}
			saveConfigAll();
			return true;
		} else {
			return false;
		}
	}
	
	public String[] getKeyInfo(String key){	
		if (plugin.getConfig().getString("keys."+key+".group") != null){
			return new String[]{getString("","keys."+key+".group"),getString("","keys."+key+".duration"),getString("","keys."+key+".uses")};
		}
		return new String[0];
	}
		
	public boolean activateVip(OfflinePlayer p, String key, String group, long days, String pname) {
		boolean hasItemkey = key != null && plugin.getConfig().contains("itemKeys."+key);
		if (hasItemkey){		
			StringBuilder cmdsBuilder = new StringBuilder();
			List<String> cmds = plugin.getConfig().getStringList("itemKeys."+key+".cmds");
			for (String cmd:cmds){
				cmdsBuilder.append(cmd+", ");
				plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
					@Override
					public void run() {
						String cmdf = cmd.replace("{p}", p.getName());
						if (p.isOnline()){
							plugin.serv.dispatchCommand(plugin.serv.getConsoleSender(), cmdf);							
						} 					
					}
				}, delay*2);
				delay++;
			}		
			plugin.getConfig().set("itemKeys."+key, null);
			saveConfigAll();
			
			p.getPlayer().sendMessage(plugin.getUtil().toColor(getLang("_pluginTag","itemsGiven").replace("{items}", new String(cmds.size()+""))));		
			
			String cmdBuilded = cmdsBuilder.toString();
			plugin.addLog("ItemKey | "+p.getName()+" | "+key+" | Cmds: "+cmdBuilded.substring(0, cmdBuilded.length()-2));
		}
		if (getKeyInfo(key).length == 3){
			String[] keyinfo = getKeyInfo(key);
			int uses = Integer.parseInt(keyinfo[2]);
			
			delKey(key, uses);
			
			p.getPlayer().sendMessage(plugin.getUtil().toColor("&b---------------------------------------------"));
			if (uses-1 > 0){				
				p.getPlayer().sendMessage(plugin.getUtil().toColor(getLang("_pluginTag","usesLeftActivation").replace("{uses}", ""+(uses-1))));
			}			
			enableVip(p, keyinfo[0], new Long(keyinfo[1]), pname);				
			return true;
		} else if (!group.equals("")){			
			enableVip(p, group, plugin.getUtil().dayToMillis(days), pname);
			return true;
		} else {
			if (!hasItemkey){
				p.getPlayer().sendMessage(plugin.getUtil().toColor(getLang("_pluginTag","invalidKey")));	
				return false;
			}
			return true;
		}		
	}
	
	//public for bungee
	public void enableVip(OfflinePlayer p, String group, long durMillis, String pname){		
		int count = 0;
		long durf = durMillis;	
		for (String[] k:getVipInfo(p.getUniqueId().toString())){
			if (k[1].equals(group)){	
				durMillis += new Long(k[0]);
				count++;
				break;
			}
		}			
		
		if (count == 0){
			durMillis += plugin.getUtil().getNowMillis();
		}
		
		String pGroup = plugin.getPerms().getGroup(p);
		String pdGroup = pGroup;
		List<String[]> vips = getVipInfo(p.getUniqueId().toString());
		if (!vips.isEmpty()){
			pGroup = vips.get(0)[2];
		}
		
		
		List<String> normCmds = new ArrayList<String>();
		List<String> chanceCmds = new ArrayList<String>();
		
		//run command from vip
		getListString("groups."+group+".commands").forEach((cmd)->{
			plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
				@Override
				public void run() {
					String cmdf = cmd.replace("{p}", p.getName())
							.replace("{vip}", group)
							.replace("{playergroup}", pdGroup)
							.replace("{days}", String.valueOf(plugin.getUtil().millisToDay(durf)));
					if (p.isOnline()){
						plugin.serv.dispatchCommand(plugin.serv.getConsoleSender(), cmdf);
					} else {
						normCmds.add(cmdf);
					}						
				}
			}, delay*2);			
			delay++;
		});		
		
		//run command chances from vip		
		getCmdChances(group).forEach((chanceString)->{
			
			int chance = Integer.parseInt(chanceString);
			double rand = Math.random() * 100;
			
			//test chance
			if (rand <= chance){
				getListString("groups."+group+".cmdChances."+chanceString).forEach((cmd)->{
					plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
						@Override
						public void run() {
							String cmdf = cmd.replace("{p}", p.getName())
									.replace("{vip}", group)
									.replace("{playergroup}", pdGroup)
									.replace("{days}", String.valueOf(plugin.getUtil().millisToDay(durf)));
							if (p.isOnline()){
								plugin.serv.dispatchCommand(plugin.serv.getConsoleSender(), cmdf);
							} else {
								chanceCmds.add(cmdf);
							}
						}
					}, delay*2);			
					delay++;
				});
			}						
		});
		
		if (queueCmds() && (normCmds.size() > 0 || chanceCmds.size() > 0)){	
			plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
				@Override
				public void run() {
					plugin.getLogger().info("Queued cmds for player "+p.getName()+" to run on join.");
					setJoinCmds(p.getUniqueId().toString(), normCmds, chanceCmds);
				}
			}, delay*2);			
		}		
		
		delay = 0;
		
		vipsFile.set("activeVips."+group+"."+p.getUniqueId().toString()+".playerGroup", pGroup);
		vipsFile.set("activeVips."+group+"."+p.getUniqueId().toString()+".duration", durMillis);
		vipsFile.set("activeVips."+group+"."+p.getUniqueId().toString()+".nick", pname);
		vipsFile.set("activeVips."+group+"."+p.getUniqueId().toString()+".expires-on-exact", plugin.getUtil().expiresOn(durMillis));
		
		setActive(p,group,pdGroup);
		
		if (p.isOnline()){
			p.getPlayer().sendMessage(plugin.getUtil().toColor(getLang("_pluginTag","vipActivated")));
			p.getPlayer().sendMessage(plugin.getUtil().toColor(getLang("activeVip").replace("{vip}", group)));
			p.getPlayer().sendMessage(plugin.getUtil().toColor(getLang("activeDays").replace("{days}", String.valueOf(plugin.getUtil().millisToDay(durf)))));
			p.getPlayer().sendMessage(plugin.getUtil().toColor("&b---------------------------------------------"));
		}		
		plugin.addLog("EnableVip | "+p.getName()+" | "+group+" | Expires on: "+plugin.getUtil().expiresOn(durMillis));
	}
	
	public void setVip(OfflinePlayer p, String group, long durMillis, String pname){
		int count = 0;
		for (String[] k:getVipInfo(p.getUniqueId().toString())){
			if (k[1].equals(group)){	
				durMillis += new Long(k[0]);
				count++;
				break;
			}
		}		
		
		if (count == 0){
			durMillis += plugin.getUtil().getNowMillis();
		}
		
		String pGroup = plugin.getPerms().getGroup(p);
		List<String[]> vips = getVipInfo(p.getUniqueId().toString());
		if (!vips.isEmpty()){
			pGroup = vips.get(0)[2];
		}					
		vipsFile.set("activeVips."+group+"."+p.getUniqueId().toString()+".playerGroup", pGroup);	
		vipsFile.set("activeVips."+group+"."+p.getUniqueId().toString()+".duration", durMillis);	
		vipsFile.set("activeVips."+group+"."+p.getUniqueId().toString()+".nick", pname);
		vipsFile.set("activeVips."+group+"."+p.getUniqueId().toString()+".expires-on-exact", plugin.getUtil().expiresOn(durMillis));
		setActive(p,group,pGroup);
		
		plugin.addLog("SetVip | "+p.getName()+" | "+group+" | Expires on: "+plugin.getUtil().expiresOn(durMillis));
	}
	
	public void setActive(OfflinePlayer p, String group, String pgroup){
		String uuid = p.getUniqueId().toString();
		String newVip = group;
		String oldVip = pgroup;		
		for (Object key:getGroupList()){
			if (vipsFile.isConfigurationSection("activeVips."+key+"."+uuid)){
				if (key.toString().equals(group)){						
					if (!getVipBoolean(true, "activeVips."+key.toString()+"."+uuid+".active")){
						newVip = key.toString();
						long total = getVipLong(0,"activeVips."+key.toString()+"."+uuid+".duration")+plugin.getUtil().getNowMillis();
						vipsFile.set("activeVips."+key+"."+uuid+".duration", total);
					}
					vipsFile.set("activeVips."+key+"."+uuid+".active", true);						
				} else {	
					if (getVipBoolean(false, "activeVips."+key.toString()+"."+uuid+".active")){
						oldVip = key.toString();
						long total = getVipLong(0,"activeVips."+key.toString()+"."+uuid+".duration")-plugin.getUtil().getNowMillis();
						vipsFile.set("activeVips."+key+"."+uuid+".duration", total);
					}
					vipsFile.set("activeVips."+key+"."+uuid+".active", false);
				}
			}
		}			
		runChangeVipCmds(p, newVip, oldVip);
		//change kits
		changeVipKit(p, oldVip, newVip);
		saveConfigAll();
	}
		
	public void runChangeVipCmds(OfflinePlayer p, String newVip, String oldVip){
		for (String cmd:getListString("configs.commandsToRunOnChangeVip")){
			if (p.getName() == null){break;}
			
			String cmdf = cmd.replace("{p}", p.getName());
			if (!oldVip.equals("") && cmdf.contains("{oldvip}")){
				plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
					@Override
					public void run() {
						plugin.serv.dispatchCommand(plugin.serv.getConsoleSender(),  cmdf.replace("{oldvip}", oldVip));
					}
				}, delay*5);
				delay++;
			} else
			if (!newVip.equals("") && cmdf.contains("{newvip}")){
				plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
					@Override
					public void run() {
						plugin.serv.dispatchCommand(plugin.serv.getConsoleSender(),  cmdf.replace("{newvip}", newVip));
					}
				}, delay*5);
				delay++;
			} else {
				plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
					@Override
					public void run() {
						plugin.serv.dispatchCommand(plugin.serv.getConsoleSender(),  cmdf);
					}
				}, delay*5);
				delay++;
			}
		}
		if (plugin.getConfig().getBoolean("configs.useVault-toChangePlayerGroup")){
			if (oldVip != null && !oldVip.isEmpty()){
				plugin.getPerms().removeGroup(p, oldVip);
			}			
			plugin.getPerms().addGroup(p, newVip);
		} else {
			reloadPerms();
		}
	}
	
	private void changeVipKit(OfflinePlayer p, String oldVip, String newVip){
		if (plugin.ess != null && p.isOnline()){
			
			String oldKit = this.getString("", "groups."+oldVip+".essentials-kit");			
			if (!oldKit.isEmpty()){
				long oldTime = plugin.ess.getUser(p.getPlayer()).getKitTimestamp(oldKit.toLowerCase());
				vipsFile.set("activeVips."+oldVip+"."+p.getUniqueId().toString()+".kit-cooldown", oldTime);
			}
			
			String newKit = this.getString("", "groups."+newVip+".essentials-kit");			
			if (!newKit.isEmpty()){
				long newTime = vipsFile.getLong("activeVips."+newVip+"."+p.getUniqueId().toString()+".kit-cooldown", 0);
				if (newTime > 0){
					plugin.ess.getUser(p.getPlayer()).setKitTimestamp(newKit.toLowerCase(), newTime);
				}
			}
		}
	}
	
	void removeVip(OfflinePlayer p, String group){
		plugin.addLog("RemoveVip | "+p.getName()+" | "+group);
		
		vipsFile.set("activeVips."+group+"."+p.getUniqueId().toString(), null);
		plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
			@Override
			public void run() {
				if (p.getName() != null){
					plugin.serv.dispatchCommand(plugin.serv.getConsoleSender(), getString("","configs.cmdOnRemoveVip").replace("{p}", Optional.<String>ofNullable(p.getName()).get()).replace("{vip}", group));
				}							
		    }
		},delay*5);
		delay++;
		
		if (plugin.getConfig().getBoolean("configs.useVault-toChangePlayerGroup")){
			plugin.getPerms().removeGroup(p, group);
		} 
	}
	
	public void removeVip(OfflinePlayer p, Optional<String> optg){				
		String uuid = p.getUniqueId().toString();
		List<String[]> vipInfo = getVipInfo(uuid);
		boolean id = false;
		String oldGroup = "";
		if (vipInfo.size() > 0){			
			for (String[] key:vipInfo){
				String group = key[1];
				oldGroup = key[2];
				if (vipInfo.size() > 1 ){
					if (optg.isPresent()){
						if (optg.get().equals(group)){
							removeVip(p, group);			    							
						} else if (!id){
							setActive(p, group, "");
							id = true;
						}			    						
	    			} else {	    				
    					removeVip(p, group);
	    			}
				} else {
					removeVip(p, group);
				}		
			}			    			
		}
		if (getVipInfo(uuid).size() == 0){			
			for (String cmd:getListString("configs.commandsToRunOnVipFinish")){
				if (cmd.contains("{vip}") || p.getName() == null){continue;}
				String cmdf = cmd.replace("{p}", p.getName()).replace("{playergroup}", oldGroup);
				plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
					@Override
					public void run() {
						plugin.serv.dispatchCommand(plugin.serv.getConsoleSender(), cmdf);
					}
				},1+delay*5);		
				delay++;
			}
		}
		
		if (plugin.getConfig().getBoolean("configs.useVault-toChangePlayerGroup")){
			plugin.getPerms().setGroup(p, oldGroup);
		} else {
			reloadPerms();
		}
		saveConfigAll();
	}
	
	public void reloadPerms(){
		plugin.serv.getScheduler().runTaskLater(plugin, new Runnable(){
			@Override
			public void run() {
				plugin.serv.dispatchCommand(plugin.serv.getConsoleSender(), getString("","configs.cmdToReloadPermPlugin"));	
			}
			
		}, (1+delay)*10);	
		delay=0;
	}
	
	public long getVipLong(int def, String node){
		return vipsFile.getLong(node, def);
	}
	
	public int getVipInt(int def, String node){
		return vipsFile.getInt(node, def);
	}
	
	public String getVipString(String def, String node){
		return vipsFile.getString(node, def);
	}
	
	public boolean getVipBoolean(boolean def, String node){
		return vipsFile.getBoolean(node, def);
	}
	
	public long getLong(int def, String node){
		return plugin.getConfig().getLong(node, def);
	}
	
	public int getInt(int def, String node){
		return plugin.getConfig().getInt(node, def);
	}
	
	public String getString(String def, String node){
		return plugin.getConfig().getString(node, def);
	}
	
	public boolean getBoolean(boolean def, String node){
		return plugin.getConfig().getBoolean(node, def);
	}
	
	public Object getObj(Object def, String node){
		return plugin.getConfig().get(node, def);
	}
	
	public List<String> getListString(String node){
		List<String> keyList = new ArrayList<String>();
		keyList.addAll(plugin.getConfig().getStringList(node));
		return keyList;
	}
	
	public String getLang(String... nodes){
		StringBuilder msg = new StringBuilder();
		for (String node:nodes){
			msg.append(getString("No strings with "+node, "strings."+node));
		}
		return msg.toString();
	}

	public boolean groupExists(String group) {
		return plugin.getConfig().contains("groups."+group);
	}

	public Set<String> getCmdChances(String vip) {
		if (plugin.getConfig().getConfigurationSection("groups."+vip+".cmdChances") != null){
			return plugin.getConfig().getConfigurationSection("groups."+vip+".cmdChances").getKeys(false);
		}
		return new HashSet<String>();
	}
	
	public Set<String> getListKeys() {
		if (plugin.getConfig().getConfigurationSection("keys") != null){
			return plugin.getConfig().getConfigurationSection("keys").getKeys(false);
		}
		return new HashSet<String>();
	}
	
	public Set<String> getItemListKeys() {
		if (plugin.getConfig().getConfigurationSection("itemKeys") != null){
			return plugin.getConfig().getConfigurationSection("itemKeys").getKeys(false);
		}
		return new HashSet<String>();
	}
	
	public Set<String> getGroupList(){
		if (plugin.getConfig().getConfigurationSection("groups") != null){
			return plugin.getConfig().getConfigurationSection("groups").getKeys(false);
		}
		return new HashSet<String>();
	}
	
	public HashMap<String,List<String[]>> getVipList(){
		HashMap<String,List<String[]>> vips = new HashMap<String,List<String[]>>();		
		getGroupList().stream().filter(k->vipsFile.contains("activeVips."+k)).forEach(groupobj -> {
			vipsFile.getConfigurationSection("activeVips."+groupobj).getKeys(false).forEach(uuidobj -> {
				String uuid = uuidobj.toString();				
				List<String[]> vipInfo = getVipInfo(uuid);
				List<String[]> activeVips = new ArrayList<String[]>();
				vipInfo.stream().filter(v->v[3].equals("true")).forEach(active -> {
					activeVips.add(active);					
				});				
				if (activeVips.size() > 0){
					vips.put(uuid, activeVips);
				}
			});			
		});
		return vips;
	}
	
	/**Return player's vip info.<p>
	 * [0] = Duration, [1] = Vip Group, [2] = Player Group, [3] = Is Active, [4] = Player Nick
	 * @param puuid Player UUID as string.
	 * @return {@code List<String[5]>}
	 */
	public List<String[]> getVipInfo(String puuid){
		List<String[]> vips = new ArrayList<String[]>();
		getGroupList().stream().filter(k->vipsFile.contains("activeVips."+k.toString()+"."+puuid)).forEach(key ->{
			vips.add(new String[]{
					getVipString("","activeVips."+key.toString()+"."+puuid+".duration"),
					key.toString(),
					getVipString("","activeVips."+key.toString()+"."+puuid+".playerGroup"),
					getVipString("","activeVips."+key.toString()+"."+puuid+".active"),
					getVipString("","activeVips."+key.toString()+"."+puuid+".nick")});
		});				
		return vips;
	}
	
	
	/**Return player's vip info.<p>
	 * [0] = Duration, [1] = Vip Group, [2] = Player Group, [3] = Is Active, [4] = Player Nick
	 * @param puuid Player UUID as string or nickname.
	 * @return {@code String[5]}
	 */
	@SuppressWarnings("deprecation")
	public String[] getActiveVipInfo(String playName){
		OfflinePlayer offp = null;	
		try{
			UUID puuid = UUID.fromString(playName);
			offp = Bukkit.getOfflinePlayer(puuid);	
		} catch (IllegalArgumentException ex){
			offp = Bukkit.getOfflinePlayer(playName);				
		}
		for (String[] vips:getVipInfo(offp.getUniqueId().toString())){
			if (vips[3].equals("true")){
				return vips;
			}
		}
		return new String[5];
	}
}
