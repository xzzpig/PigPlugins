package com.github.xzzpig.lord.player.zhiye;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.github.xzzpig.lord.Vars;
import com.github.xzzpig.lord.player.IPlayerInfo;

public class Zhiye {

	class AttributeInfo implements IPlayerInfo {

		private ConfigurationSection config;

		public AttributeInfo(ConfigurationSection config) {
			this.config = config;
		}

		@Override
		public double getArmor_M() {
			return this.config.getDouble("Armor_m");
		}

		@Override
		public double getArmor_P() {
			return this.config.getDouble("Armor_p");
		}

		@Override
		public Map<String, Integer> getCritical() {
			Map<String, Integer> d = new HashMap<>();
			d.put("prob", this.config.getInt("Critical.prob"));
			d.put("damage_p", this.config.getInt("Critical.damage_p"));
			d.put("damage_m", this.config.getInt("Critical.damage_m"));
			return d;
		}

		@Override
		public double getDamage_M() {
			return this.config.getDouble("Damage_m");
		}

		@Override
		public double getDamage_P() {
			return this.config.getDouble("Damage_p");
		}

		@Override
		public double getEvasion() {
			return this.config.getDouble("Evasion");
		}

		@Override
		public double getHealth() {
			return this.config.getDouble("Health");
		}

		@Override
		public double getMaxMP() {
			return this.config.getDouble("MP");
		}

		@Override
		public int getLevel() {
			return 0;
		}

		@Override
		public double getLifeSteal() {
			return this.config.getDouble("LifeSteal");
		}

		@Override
		public double getMaxHealth() {
			return this.config.getDouble("Health");
		}

		@Override
		public int getSpeed() {
			return this.config.getInt("Speed");
		}

		@Override
		public String getZhiye() {
			return getName();
		}

	}

	public static final Zhiye NONE = new Zhiye(null) {
		@Override
		public AttributeInfo getAttribute() {
			return new AttributeInfo(null) {
				@Override
				public double getArmor_M() {
					return 0;
				}

				@Override
				public double getArmor_P() {
					return 0;
				}

				@Override
				public Map<String, Integer> getCritical() {
					Map<String, Integer> d = new HashMap<>();
					d.put("prob", 0);
					d.put("damage_p", 0);
					d.put("damage_m", 0);
					return d;
				}

				@Override
				public double getDamage_M() {
					return 0;
				}

				@Override
				public double getDamage_P() {
					return 1;
				}

				@Override
				public double getEvasion() {
					return 0;
				}

				@Override
				public double getHealth() {
					return 0;
				}

				@Override
				public double getMaxMP() {
					return 0;
				}

				@Override
				public int getLevel() {
					return 0;
				}

				@Override
				public double getLifeSteal() {
					return 0;
				}

				@Override
				public double getMaxHealth() {
					return 20;
				}

				@Override
				public int getSpeed() {
					return 10;
				}

				@Override
				public String getZhiye() {
					return getName();
				}
			};
		}

		@Override
		public int getChance() {
			return 0;
		}

		@Override
		public int getID() {
			return -10727;
		}

		@Override
		public String getName() {
			return "无";
		}

		@Override
		public List<String> getPermission() {
			return new ArrayList<>();
		}
	};

	public static List<Zhiye> zhiyelist;

	public static Zhiye getBy(int id) {
		for (Zhiye zhiye : zhiyelist) {
			if (zhiye.getID() == id)
				return zhiye;
		}
		return null;
	}

	public static Zhiye getBy(String name) {
		if (name == null || name.equalsIgnoreCase("NONE"))
			return NONE;
		for (Zhiye zhiye : zhiyelist) {
			if (zhiye.getName().equalsIgnoreCase(name))
				return zhiye;
		}
		return null;
	}

	public static Zhiye getRandomDefaultZhiye() {
		Zhiye zhiye = null;
		int allchance = 0;
		for (Zhiye z : zhiyelist) {
			if (!z.isDefault())
				continue;
			allchance += z.getChance();
		}
		int chance = Vars.random.nextInt(allchance);
		for (Zhiye z : zhiyelist) {
			if (!z.isDefault())
				continue;
			chance -= z.getChance();
			if (chance <= 0) {
				zhiye = z;
				break;
			}
		}
		return zhiye;
	}

	public static void loadConfig() {
		ConfigurationSection zhiye = Vars.config.getConfigurationSection("Lord.Zhiye");
		List<String> zylist = zhiye.getStringList("ZhiyeList");
		zhiyelist = new ArrayList<>();
		for (String zy : zylist) {
			Zhiye.zhiyelist.add(new Zhiye(zhiye.getConfigurationSection(zy)));
		}
	}

	private AttributeInfo attributeInfo;

	private ConfigurationSection config;

	public Zhiye(ConfigurationSection section) {
		config = section;
	}

	public AttributeInfo getAttribute() {
		if (attributeInfo == null)
			attributeInfo = new AttributeInfo(config.getConfigurationSection("attribute"));
		return attributeInfo;
	}

	public int getChance() {
		return config.getInt("chance", 0);
	}

	public ConfigurationSection getConfig() {
		return config;
	}

	public int getID() {
		return config.getInt("id", 0);
	}

	public String getName() {
		return config.getString("name");
	}

	public List<String> getPermission() {
		return config.getStringList("permission");
	}

	public boolean isDefault() {
		return config.getBoolean("default", true);
	}
}
