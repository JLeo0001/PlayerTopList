package cn.JvavRE.playerTopList.data;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.ui.UI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;

public class TopList {
    private final String name;
    private final Statistic type;
    private final List<?> subArgs;
    private final ArrayList<PlayerData> dataList;

    private final UI ui;
    private LocalDateTime lastUpdate;

    private Expression expression;
    private String formatter;

    public TopList(String name, TextColor nameColor, Statistic type, List<?> subArgs,
                   Expression expression, String formatter) {

        this.name = name;
        this.type = type;
        this.subArgs = subArgs;
        this.dataList = new ArrayList<>();

        this.lastUpdate = LocalDateTime.now();
        this.ui = new UI(this, nameColor);

        this.expression = expression;
        this.formatter = formatter;

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

    public void showUI(Player player, int page) {
        ui.show(player, page);
    }

    public ArrayList<PlayerData> getDataList() {
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

    public String getFormattedData(PlayerData playerData) {
        try {
            return formatter.formatted(calc(playerData));
        } catch (IllegalFormatConversionException e) {
            formatter = "%.0f";
            PlayerTopList.getInstance().getLogger().warning("格式化字符串出现错误, 已重置为%.0f");
            return formatter.formatted(calc(playerData));
        }
    }

    public Double calc(PlayerData playerData) {
        try {
            return expression != null ?
                    expression.setVariable("count", playerData.getCount()).evaluate() :
                    playerData.getCount();
        } catch (Exception e) {
            expression = null;
            PlayerTopList.getInstance().getLogger().warning("表达式出现错误, 已禁用表达式");
            return (double) playerData.getCount();
        }
    }
}
