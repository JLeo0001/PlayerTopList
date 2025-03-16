package cn.JvavRE.playerTopList.data;

import cn.JvavRE.playerTopList.ui.UI;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TopList {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String name;
    private final Statistic type;
    private final List<?> subArgs;
    private final ArrayList<PlayerData> dataList;
    private final UI ui;
    private LocalDateTime lastUpdate;

    public TopList(String name, TextColor nameColor, Statistic type, List<?> subArgs) {
        this.name = name;
        this.type = type;
        this.subArgs = subArgs;
        this.dataList = new ArrayList<>();
        this.lastUpdate = LocalDateTime.now();
        this.ui = new UI(this, nameColor);

        initDataList();
        updateDataList();
    }

    private void initDataList() {
        dataList.clear();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            dataList.add(PlayerData.of(player));
        }
        sortDataList();
    }

    protected void updateDataList() {
        List<OfflinePlayer> players = Arrays.stream(Bukkit.getOfflinePlayers()).toList();

        // 检查是否存在新玩家, 不需要每次都刷新dataList
        if (dataList.size() != players.size()) {
            List<OfflinePlayer> oldPlayers = dataList.stream().map(PlayerData::getPlayer).toList();

            List<OfflinePlayer> newPlayers = players.stream().filter(player -> !oldPlayers.contains(player)).toList();
            for (OfflinePlayer player : newPlayers) {
                dataList.add(PlayerData.of(player));
            }
        }

        dataList.forEach(playerData -> playerData.updateCount(type, subArgs));
        sortDataList();

        // 更新时间
        lastUpdate = LocalDateTime.now();

        // 更新ui
        ui.update();
    }

    private void sortDataList() {
        dataList.sort(Comparator.comparingInt(PlayerData::getCount).reversed());
    }

    public void show(Player player, int page) {
        ui.show(player, page);
    }

    public ArrayList<PlayerData> getDataList() {
        return dataList;
    }

    public String getName() {
        return name;
    }

    public String getUpdateTime() {
        return lastUpdate.format(formatter);
    }

    public UI getUI() {
        return ui;
    }
}
