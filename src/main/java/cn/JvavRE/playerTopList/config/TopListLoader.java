package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.tasks.ListsManager;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TopListLoader {
    private static PlayerTopList plugin;

    private static boolean isStatistic(String type) {
        try {
            Statistic.valueOf(type);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isName(String name) {
        return name != null && !name.isEmpty();
    }

    private static void addTopListToManager(String name, String type, List<String> materialNames) {
        if (!isName(name)) {
            plugin.getLogger().warning("不是有效的列表名称: '" + name + "'");
            return;
        }

        if (!isStatistic(type)) {
            plugin.getLogger().warning("不是有效的统计类型: '" + type + "'");
            return;
        }

        List<Material> materials = new ArrayList<>();

        if (!materialNames.isEmpty() && materialNames.getFirst().equals("ALL")) {
            for (Material material : Material.values()) {
                if (material.isSolid()) materials.add(material);
            }

        } else {
            for (String materialName : materialNames) {
                Material material = Material.matchMaterial(materialName);
                if (material == null) {
                    plugin.getLogger().warning("不是有效的材料: '" + materialName + "'");
                    continue;
                }
                materials.add(material);
            }
        }

        ListsManager.addNewList(name, Statistic.valueOf(type), materials);
        plugin.getLogger().info("成功添加列表: " + name + " (" + type + ")");
    }

    protected static void loadTopLists(ConfigurationSection section) {
        if (section == null) return;

        for (String name : section.getKeys(false)) {
            ConfigurationSection listSection = section.getConfigurationSection(name);
            if (listSection == null) continue;

            String type = listSection.getString("type");
            List<String> material = listSection.getStringList("material");

            addTopListToManager(name, type, material);
        }
    }

    public static void setPlugin(PlayerTopList plugin) {
        TopListLoader.plugin = plugin;
    }
}
