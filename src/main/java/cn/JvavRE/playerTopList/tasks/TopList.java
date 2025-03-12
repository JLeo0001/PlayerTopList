package cn.JvavRE.playerTopList.tasks;

import cn.JvavRE.playerTopList.config.UIComponent;
import cn.JvavRE.playerTopList.config.UIConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.util.ArrayList;
import java.util.Comparator;

public class TopList {
    private final String name;
    private final Statistic type;
    private final ArrayList<PlayerData> playerDataList;

    public TopList(String name, Statistic type) {
        this.name = name;
        this.type = type;
        this.playerDataList = new ArrayList<>();
        updateTopList();
    }

    protected void updateTopList() {
        playerDataList.clear();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            playerDataList.add(PlayerData.of(player, type));
        }
        sort();
    }

    private void sort() {
        playerDataList.sort(Comparator.comparingInt(PlayerData::count).reversed());
    }

    public String getName() {
        return name;
    }

    public ArrayList<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    public String toMiniMessage(int page) {
        StringBuilder builder = new StringBuilder();

        int pageSize = 10;
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, playerDataList.size());

        // 拼接UI头部
        builder.append(UIConfig.get(UIComponent.HEADER)
                .replace("listName", name)
        ).append("<newline>");

        // 添加玩家数据
        for (int i = start; i < end; i++) {
            builder.append(playerDataList.get(i).toMiniMessage()
                    .replace("{num}", String.valueOf(i + 1))
            ).append("<newline>");
        }

        // 添加UI尾部
        builder.append(UIConfig.get(UIComponent.FOOTER)
                .replace("{prevButton}", UIConfig.get(UIComponent.PREV_BUTTON))
                .replace("{nxtButton}", UIConfig.get(UIComponent.NEXT_BUTTON))
                .replace("{idx}", UIConfig.get(UIComponent.INDEX)
                        .replace("{currentIndex}", String.valueOf(page))
                        .replace("{totalIndex}", String.valueOf((playerDataList.size() / pageSize) + 1))
                )
        );

        return builder.toString();
    }

    public Component toComponent(int page) {
        return MiniMessage.miniMessage().deserialize(toMiniMessage(page));
    }
}
