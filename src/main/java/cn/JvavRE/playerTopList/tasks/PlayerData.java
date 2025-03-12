package cn.JvavRE.playerTopList.tasks;

import cn.JvavRE.playerTopList.config.UIComponent;
import cn.JvavRE.playerTopList.config.UIConfig;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

public record PlayerData(OfflinePlayer player, int count) {

    public static PlayerData of(OfflinePlayer player, Statistic type) {
        return new PlayerData(player, player.getStatistic(type));
    }

    //TODO: 添加自定义格式
    public String toMiniMessage() {
        String item = UIConfig.get(UIComponent.ITEM)
                .replace("{playerName}", player.getName() != null ? player.getName() : "null")
                .replace("{count}", String.valueOf(count));

        int itemLength = item.length() - "{spacer}".length();

        if (itemLength < 35) {
            item = item.replace("{space}", UIConfig.get(UIComponent.SPACER).repeat(35 - itemLength));
        }

        return item;
    }
}
