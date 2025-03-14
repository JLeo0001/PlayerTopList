package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.ConcurrentHashMap;

public class UIConfig {
    private static final ConcurrentHashMap<UIComponent, String> components = new ConcurrentHashMap<>();

    public static String get(UIComponent component) {
        return components.get(component);
    }

    protected static void loadConfig(ConfigurationSection config) {
        ConcurrentHashMap<UIComponent, String> newMap = new ConcurrentHashMap<>();

        if (config == null) {
            PlayerTopList.Logger().warning("未找到UI配置, 使用默认配置");
            for (UIComponent component : UIComponent.values()) {
                newMap.put(component, component.getComponent());
            }

        } else {
            for (UIComponent component : UIComponent.values()) {
                newMap.put(component, config.getString(component.getName(), component.getComponent()));
            }
        }

        components.clear();
        components.putAll(newMap);
        PlayerTopList.Logger().info("加载UI配置完成");
    }
}
