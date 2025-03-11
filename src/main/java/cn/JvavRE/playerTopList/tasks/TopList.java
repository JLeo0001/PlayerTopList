package cn.JvavRE.playerTopList.tasks;

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
        playerDataList.sort(Comparator.comparingInt(PlayerData::count));
    }

    public String getName() {
        return name;
    }

    public ArrayList<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    public String toMiniMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(":<newline>");
        for (int i = 0; i < playerDataList.size(); i++) {
            builder.append(i + 1).append(". ").append(playerDataList.get(i).toMiniMessage()).append("<newline>");
        }
        return builder.toString();
    }

    public Component toComponent(){
        return MiniMessage.miniMessage().deserialize(toMiniMessage());
    }
}
