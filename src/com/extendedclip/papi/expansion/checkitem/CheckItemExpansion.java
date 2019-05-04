package com.extendedclip.papi.expansion.checkitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class CheckItemExpansion extends PlaceholderExpansion {
	
	public boolean canRegister() {
		return true;
	}
	
	public String getAuthor() {
		return "clip";
	}
	
	public String getIdentifier() {
		return "checkitem";
	}
	
	public String getPlugin() {
		return null;
	}
	
	public String getVersion() {
		return "1.1.0";
	}
	
	public class ItemWrapper {
		
		private boolean checkNameContains;
		private boolean checkNameStartsWith;
		private boolean checkNameEquals;
		private boolean checkLoreContains;
		private boolean checkDurability;
		private boolean checkAmount;
		private boolean checkType;
		private String m;
		private short d;
		private int a;
		private String name;
		private String lore;
		
		public ItemWrapper(String material, short data, int amt) {
			this.m = material.toUpperCase();
			this.d = data;
			this.a = amt;
		}
		
		public ItemWrapper() {
		}
		
		public String getType() {
			return this.m;
		}
		
		public void setType(String material) {
			this.m = material.toUpperCase();
		}
		
		public short getDurability() {
			return this.d;
		}
		
		public void setDurability(short durability) {
			this.d = durability;
		}
		
		public int getAmount() {
			return this.a;
		}
		
		public void setAmount(int amount) {
			this.a = amount;
		}
		
		public String getName() {
			return this.name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public void setLore(String lore) {
			this.lore = lore;
		}
		
		public String getLore() {
			return this.lore;
		}
		
		public boolean shouldCheckDurability() {
			return this.checkDurability;
		}
		
		public void setCheckDurability(boolean checkDurability) {
			this.checkDurability = checkDurability;
		}
		
		public boolean shouldCheckAmount() {
			return this.checkAmount;
		}
		
		public void setCheckAmount(boolean checkAmount) {
			this.checkAmount = checkAmount;
		}
		
		public boolean shouldCheckNameContains() {
			return this.checkNameContains;
		}
		
		public void setCheckNameContains(boolean checkNameContains) {
			this.checkNameContains = checkNameContains;
		}
		
		public boolean shouldCheckNameStartsWith() {
			return this.checkNameStartsWith;
		}
		
		public void setCheckNameStartsWith(boolean checkNameStartsWith) {
			this.checkNameStartsWith = checkNameStartsWith;
		}
		
		public boolean shouldCheckNameEquals() {
			return this.checkNameEquals;
		}
		
		public void setCheckNameEquals(boolean checkNameEquals) {
			this.checkNameEquals = checkNameEquals;
		}
		
		public boolean shouldCheckLoreContains() {
			return this.checkLoreContains;
		}
		
		public void setCheckLoreContains(boolean checkLoreContains) {
			this.checkLoreContains = checkLoreContains;
		}
		
		public boolean shouldCheckType() {
			return this.checkType;
		}
		
		public void setCheckType(boolean checkType) {
			this.checkType = checkType;
		}
	}
	
	public String onPlaceholderRequest(Player p, String args) {
		ItemWrapper wrapper = getItem(ChatColor.translateAlternateColorCodes('&', args));
		if (wrapper == null) {
			return null;
		}
		if (wrapper.getType() == "AIR") {
			return p.getInventory().firstEmpty() == -1 ? PlaceholderAPIPlugin.booleanFalse()
					: PlaceholderAPIPlugin.booleanTrue();
		}
		
		ItemStack[] arrayOfItemStack;
		int j = (arrayOfItemStack = p.getInventory().getContents()).length;
		for (int i = 0; i < j; i++) {
			ItemStack toCheck = arrayOfItemStack[i];
			if (toCheck != null) {
				if (wrapper.shouldCheckType() && !(wrapper.getType().equals(toCheck.getType().name()))) {
					continue;
				}
				if (wrapper.shouldCheckDurability() && !(wrapper.getDurability() == toCheck.getDurability())) {
					continue;
				}
				ItemMeta toCheckMeta = toCheck.getItemMeta();
				if (wrapper.shouldCheckLoreContains() && toCheckMeta.hasLore()) {
					boolean loreContains = false;
					for (String line : toCheckMeta.getLore()) {
						if (line.contains(wrapper.getLore())) {
							loreContains = true;
							break;
						}
					}
					if (!loreContains || (wrapper.shouldCheckLoreContains() && !toCheckMeta.hasLore())) {
						continue;
					}
				}
				if (wrapper.shouldCheckNameContains()) {
					if (!(toCheckMeta.hasDisplayName() && toCheckMeta.getDisplayName().contains(wrapper.name))) {
						continue;
					}
				}
				if (wrapper.shouldCheckNameStartsWith()) {
					if (!(toCheckMeta.hasDisplayName() && toCheckMeta.getDisplayName().startsWith(wrapper.name))) {
						continue;
					}
				}
				if (wrapper.shouldCheckNameEquals()) {
					if (!(toCheckMeta.hasDisplayName() && toCheckMeta.getDisplayName().equals(wrapper.name))) {
						continue;
					}
				}
				if (wrapper.shouldCheckAmount() && !(toCheck.getAmount() >= wrapper.getAmount())) {
					continue;
				}
				return PlaceholderAPIPlugin.booleanTrue();
			}
		}
		return PlaceholderAPIPlugin.booleanFalse();
	}
	
	public int getInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException ex) {
		}
		return -1;
	}
	
	public ItemWrapper getItem(String input) {
		ItemWrapper wrapper = new ItemWrapper();
		if (input.indexOf(",") > 0) {
			String[] arrayOfString;
			int j = (arrayOfString = input.split(",")).length;
			for (int i = 0; i < j; i++) {
				String part = arrayOfString[i];
				if (part.startsWith("data:")) {
					part = part.replace("data:", "");
					try {
						wrapper.setDurability(Short.parseShort(part));
						wrapper.setCheckDurability(true);
					} catch (Exception localException1) {
					}
				}
				if (part.startsWith("mat:")) {
					part = part.replace("mat:", "");
					try {
						if (getInt(part) > 0) {
							wrapper.setType(Material.getMaterial(Integer.parseInt(part)).name());
							wrapper.setCheckType(true);
						} else {
							wrapper.setType(part);
							wrapper.setCheckType(true);
						}
					} catch (Exception ex) {
						return null;
					}
				}
				if (part.startsWith("amt:")) {
					part = part.replace("amt:", "");
					try {
						wrapper.setAmount(Integer.parseInt(part));
						wrapper.setCheckAmount(true);
					} catch (Exception localException2) {
					}
				}
				if (part.startsWith("namestartswith:")) {
					part = part.replace("namestartswith:", "");
					wrapper.setName(part);
					wrapper.setCheckNameStartsWith(true);
				}
				if (part.startsWith("namecontains:")) {
					part = part.replace("namecontains:", "");
					wrapper.setName(part);
					wrapper.setCheckNameContains(true);
				}
				if (part.startsWith("nameequals:")) {
					part = part.replace("nameequals:", "");
					wrapper.setName(part);
					wrapper.setCheckNameEquals(true);
				}
				if (part.startsWith("lorecontains:")) {
					part = part.replace("lorecontains:", "");
					wrapper.setLore(part);
					wrapper.setCheckLoreContains(true);
				}
				
			}
			return wrapper;
		}
		if (input.startsWith("data:")) {
			input = input.replace("data:", "");
			try {
				wrapper.setDurability(Short.parseShort(input));
				wrapper.setCheckDurability(true);
			} catch (Exception localException3) {
			}
		}
		if (input.startsWith("mat:")) {
			input = input.replace("mat:", "");
			try {
				if (getInt(input) > 0) {
					wrapper.setType(Material.getMaterial(Integer.parseInt(input)).name());
					wrapper.setCheckType(true);
				} else {
					wrapper.setType(input);
					wrapper.setCheckType(true);
				}
			} catch (Exception ex) {
				return null;
			}
		}
		if (input.startsWith("amt:")) {
			input = input.replace("amt:", "");
			try {
				wrapper.setAmount(Integer.parseInt(input));
				wrapper.setCheckAmount(true);
			} catch (Exception localException4) {
			}
		}
		if (input.startsWith("namestartswith:")) {
			input = input.replace("namestartswith:", "");
			wrapper.setName(input);
			wrapper.setCheckNameStartsWith(true);
		}
		if (input.startsWith("namecontains:")) {
			input = input.replace("namecontains:", "");
			wrapper.setName(input);
			wrapper.setCheckNameContains(true);
		}
		if (input.startsWith("nameequals:")) {
			input = input.replace("nameequals:", "");
			wrapper.setName(input);
			wrapper.setCheckNameEquals(true);
		}
		return wrapper;
	}
}
