package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.ConcurrentHashMap;

public class UIConfig {
    private static final ConcurrentHashMap<UIComponent, Component> components = new ConcurrentHashMap<>();

    public static Component get(UIComponent component) {
        return components.get(component);
    }

    protected static void loadConfig(ConfigurationSection config) {
        ConcurrentHashMap<UIComponent, Component> newMap = new ConcurrentHashMap<>();

        if (config == null) {
            PlayerTopList.getInstance().getLogger().warning("未找到UI配置, 使用默认配置");
            for (UIComponent component : UIComponent.values()) {
                newMap.put(component, component.getComponent());
            }

        } else {
            for (UIComponent component : UIComponent.values()) {
                String componentStr = config.getString(component.getName());

                // todo)) : 这里deserialize是否会报错?
                if (componentStr != null) newMap.put(component, MiniMessage.miniMessage().deserialize(componentStr));
                else newMap.put(component, component.getComponent());
            }
        }

        components.clear();
        components.putAll(newMap);
        generateUI();

        PlayerTopList.getInstance().getLogger().info("加载UI配置完成");
    }

    private static void generateUI() {
        // 直接缓存UI(不知道有没有性能提升)
        TextComponent.Builder builder = Component.text();

        builder.append(get(UIComponent.HEADER)).appendNewline();
        builder.append(Component.text("{items}"));
        builder.append(get(UIComponent.FOOTER));

        components.put(UIComponent.MAIN_UI, builder.build());
    }
}
