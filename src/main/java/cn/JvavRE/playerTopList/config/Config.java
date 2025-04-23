package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Config {
    private static final Pattern UIReplacePattern = Pattern.compile("\\{\\w+}");
    private static final Pattern listNamePattern = Pattern.compile("^[^_]+$");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final boolean isPapiEnabled = PlayerTopList.getInstance().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    private static final PlayerTopList plugin = PlayerTopList.getInstance();

    private static int pageSize;
    private static int updateInterval;
    private static boolean debugOutput;

    public static void init() {
        loadConfig();
    }

    private static void loadConfig() {
        plugin.saveDefaultConfig();

        pageSize = plugin.getConfig().getInt("page-size", 10);
        updateInterval = plugin.getConfig().getInt("update-interval", 60);
        debugOutput = plugin.getConfig().getBoolean("debug-output", false);

        UIConfig.loadConfig(plugin.getConfig().getConfigurationSection("ui"));
        TopListLoader.loadTopLists(plugin.getConfig().getConfigurationSection("lists"));

        pageSize = Math.max(pageSize, 1);
        updateInterval = Math.max(updateInterval, 30);

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

    public static Pattern getUIReplacePattern() {
        return UIReplacePattern;
    }

    public static Pattern getListNamePattern() {
        return listNamePattern;
    }

    public static DateTimeFormatter getTimeFormatter() {
        return timeFormatter;
    }

    public static boolean isPapiEnabled() {
        return isPapiEnabled;
    }

    public static boolean isDebugOutput() {
        return debugOutput;
    }
}
