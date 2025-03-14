package cn.JvavRE.playerTopList.tasks;

import cn.JvavRE.playerTopList.ui.UI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopList {
    private final String name;
    private final Statistic type;
    private final List<?> subArgs;
    private final ArrayList<PlayerData> playerDataList;
    private final UI ui;

    public TopList(String name, Statistic type, List<?> subArgs) {
        this.name = name;
        this.type = type;
        this.subArgs = subArgs;
        this.playerDataList = new ArrayList<>();
        this.ui = new UI(this);

        updateTopList();
    }

    protected void updateTopList() {
        playerDataList.clear();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            playerDataList.add(PlayerData.of(player, type, subArgs));
        }
        sort();

        ui.update();
    }

    private void sort() {
        playerDataList.sort(Comparator.comparingInt(PlayerData::count).reversed());
    }

    public void show(Player player, int page) {
        ui.show(player, page);
    }


    public ArrayList<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    public Statistic getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
