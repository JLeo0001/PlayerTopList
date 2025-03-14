package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;

public class Config {
    private static PlayerTopList plugin;

    private static int pageSize;
    private static int updateInterval;

    public static void init(PlayerTopList plugin) {
        Config.plugin = plugin;

        UIConfig.setPlugin(plugin);
        TopListLoader.setPlugin(plugin);

        plugin.saveDefaultConfig();
        loadConfig();
    }

    private static void loadConfig() {
        pageSize = plugin.getConfig().getInt("page-size", 10);
        updateInterval = plugin.getConfig().getInt("update-interval", 60);

        UIConfig.loadConfig(plugin.getConfig().getConfigurationSection("ui"));
        TopListLoader.loadTopLists(plugin.getConfig().getConfigurationSection("lists"));
    }

    public static void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

    public static int getPageSize() {
        return pageSize;
    }

    public static int getUpdateInterval() {
        return updateInterval;
    }
}
