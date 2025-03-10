package cn.JvavRE.playerTopList.tasks;

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
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()){
            playerDataList.add(PlayerData.fromPlayer(player, type));
        }
        sort();
    }

    private void sort(){
        playerDataList.sort(Comparator.comparingInt(PlayerData::getCount));
    }

    public String getName() {
        return name;
    }

    public ArrayList<PlayerData> getPlayerDataList() {
        return playerDataList;
    }
}
