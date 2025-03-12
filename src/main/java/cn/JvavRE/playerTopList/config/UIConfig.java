package cn.JvavRE.playerTopList.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.ConcurrentHashMap;

public class UIConfig {
    private static final ConcurrentHashMap<UIComponent, String> components = new ConcurrentHashMap<>();

    public static String get(UIComponent component) {
        return components.get(component);
    }

    protected void loadConfig(ConfigurationSection config) {
        ConcurrentHashMap<UIComponent, String> newMap = new ConcurrentHashMap<>();

        if (config == null) {
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
    }
}
