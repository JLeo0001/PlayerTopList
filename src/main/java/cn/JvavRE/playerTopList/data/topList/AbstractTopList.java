package cn.JvavRE.playerTopList.data.topList;

import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.data.playerData.AbstractPlayerData;
import cn.JvavRE.playerTopList.data.playerData.StatisticPlayerData;
import cn.JvavRE.playerTopList.ui.UI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public abstract class AbstractTopList {
    protected final String name;
    protected final List<AbstractPlayerData> dataList;

    protected final UI ui;
    protected LocalDateTime lastUpdate;

    protected Expression expression;
    protected String formatter;

    public AbstractTopList(String name, TextColor nameColor,
                           Expression expression, String formatter) {

        this.name = name;
        this.dataList = new ArrayList<>();

        this.lastUpdate = LocalDateTime.now();
        this.ui = new UI(this, nameColor);

        this.expression = expression;
        this.formatter = formatter;
    }

    public abstract void updateDataList();

    public abstract String getFormattedData(AbstractPlayerData playerData);

    protected void initDataList() {
        dataList.clear();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            dataList.add(StatisticPlayerData.of(player));
        }
        sortDataList();
    }

    protected void sortDataList() {
        dataList.sort(Comparator.comparingInt(AbstractPlayerData::getCount).reversed());
    }

    public void showUI(Player player, int page) {
        ui.show(player, page);
    }

    public AbstractPlayerData getDataByPlayer(OfflinePlayer player) {
        return dataList.stream().filter(playerData -> Objects.equals(playerData.getPlayer().getName(), player.getName())).findFirst().orElse(null);
    }

    public int getPlayerRank(OfflinePlayer player) {
        AbstractPlayerData playerData = getDataByPlayer(player);

        if (playerData == null) return -1;
        else return dataList.indexOf(getDataByPlayer(player)) + 1;
    }

    public List<AbstractPlayerData> getDataList() {
        return dataList;
    }

    public String getName() {
        return name;
    }

    public String getUpdateTime() {
        return lastUpdate.format(Config.getFormatter());
    }

    public Component getColoredName() {
        return ui.getColoredName();
    }
}
