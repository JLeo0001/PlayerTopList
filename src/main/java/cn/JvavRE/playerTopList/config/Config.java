package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;

import java.time.format.DateTimeFormatter;
import java.util.List;
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
    private static Pattern excludedRegex;
    private static List<String> blackList;
    private static double updateOfflineChance;

    public static void init() {
        loadConfig();
    }

    private static void loadConfig() {
        plugin.saveDefaultConfig();

        pageSize = plugin.getConfig().getInt("page-size", 10);
        updateInterval = plugin.getConfig().getInt("update-interval", 60);
        debugOutput = plugin.getConfig().getBoolean("debug-output", false);
        blackList = plugin.getConfig().getStringList("blacklist");
        updateOfflineChance = plugin.getConfig().getDouble("update-offline-chance", 1.0);

        String excludedRegexString = plugin.getConfig().getString("exclude-regex", "-");
        try {
            excludedRegex = Pattern.compile(excludedRegexString);
        } catch (Exception e) {
            excludedRegex = Pattern.compile("-");
            plugin.getLogger().warning("正则表达式出现错误, 已重置为默认值");
        }

        pageSize = Math.max(pageSize, 1);
        updateInterval = Math.max(updateInterval, 30);
        updateOfflineChance = Math.min(Math.max(updateOfflineChance, 0.0), 1.0);

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

    public static List<String> getBlackList() {
        return blackList;
    }

    public static Pattern getExcludedRegex() {
        return excludedRegex;
    }

    public static double getUpdateOfflineChance() {
        return updateOfflineChance;
    }
}
