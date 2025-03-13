package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.tasks.ListsManager;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;

public class TopListLoader {
    private static boolean isStatistic(String type) {
        try {
            Statistic.valueOf(type);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isName(String name) {
        return name != null && !name.isEmpty();
    }

    private static void addTopListToManager(String name, String type) {
        if (!isName(name)) {
            Bukkit.getLogger().warning("不是有效的列表名称: '" + name + "'");
            return;
        }
        if (!isStatistic(type)) {
            Bukkit.getLogger().warning("不是有效的统计类型: '" + type + "'");
            return;
        }

        ListsManager.addNewList(name, Statistic.valueOf(type));
    }

    protected static void loadTopLists(ConfigurationSection section) {
        if (section == null) return;

        for (String name : section.getKeys(false)) {
            ConfigurationSection listSection = section.getConfigurationSection(name);
            if (listSection == null) continue;

            String path = listSection.getCurrentPath() + "." + name + ".";
            String type = listSection.getString(path + "type");

            addTopListToManager(name, type);
        }
    }
}
