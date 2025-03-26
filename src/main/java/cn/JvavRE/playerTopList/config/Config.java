package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Config {
    private static final Pattern pattern = Pattern.compile("\\{\\w+}");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static PlayerTopList plugin;
    private static int pageSize;
    private static int updateInterval;

    public static void init(PlayerTopList plugin) {
        Config.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfig();
    }

    private static void loadConfig() {
        pageSize = plugin.getConfig().getInt("page-size", 10);
        updateInterval = plugin.getConfig().getInt("update-interval", 60);

        UIConfig.loadConfig(plugin.getConfig().getConfigurationSection("ui"));
        TopListLoader.loadTopLists(plugin.getConfig().getConfigurationSection("lists"));

        plugin.getLogger().info("配置加载完成");
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

    public static Pattern getPattern() {
        return pattern;
    }

    public static DateTimeFormatter getFormatter() {
        return formatter;
    }
}
