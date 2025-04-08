package cn.JvavRE.playerTopList.data.topList;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import cn.JvavRE.playerTopList.ui.UI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;

public abstract class AbstractTopList {
    protected final String name;
    protected final List<PlayerData> dataList;

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

    public abstract void updatePlayerData();

    protected void initDataList() {
        dataList.clear();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            dataList.add(new PlayerData(player));
        }
        sortDataList();
    }

    public void updateDataList() {
        List<OfflinePlayer> players = Arrays.stream(Bukkit.getOfflinePlayers()).toList();

        // 检查是否存在新玩家, 不需要每次都刷新dataList
        if (dataList.size() != players.size()) {
            List<OfflinePlayer> oldPlayers = dataList.stream().map(PlayerData::getPlayer).toList();

            List<OfflinePlayer> newPlayers = players.stream().filter(player -> !oldPlayers.contains(player)).toList();
            for (OfflinePlayer player : newPlayers) {
                dataList.add(new PlayerData(player));
            }
        }

        updatePlayerData();
        sortDataList();

        // 更新时间
        lastUpdate = LocalDateTime.now();

        // 更新ui
        ui.update();
    }

    protected void sortDataList() {
        dataList.sort(Comparator.comparingDouble(PlayerData::getCount).reversed());
    }

    public void showUI(Player player, int page) {
        ui.show(player, page);
    }

    public PlayerData getDataByPlayer(OfflinePlayer player) {
        return dataList.stream().filter(playerData -> Objects.equals(playerData.getPlayer().getName(), player.getName())).findFirst().orElse(null);
    }

    public int getPlayerRank(OfflinePlayer player) {
        PlayerData playerData = getDataByPlayer(player);

        if (playerData == null) return -1;
        else return dataList.indexOf(getDataByPlayer(player)) + 1;
    }

    public String getFormattedData(PlayerData playerData) {
        try {
            return formatter.formatted(getExpressionResult(playerData));
        } catch (IllegalFormatConversionException e) {
            formatter = "%.0f";
            PlayerTopList.getInstance().getLogger().warning("格式化字符串出现错误, 已重置为%.0f");
            return formatter.formatted(getExpressionResult(playerData));
        }
    }

    private Double getExpressionResult(PlayerData playerData) {
        try {
            return expression == null ?
                    playerData.getCount() :
                    expression.setVariable("count", playerData.getCount()).evaluate();

        } catch (Exception e) {
            expression = null;
            PlayerTopList.getInstance().getLogger().warning("表达式出现错误, 已禁用表达式");
            return playerData.getCount();
        }
    }

    public List<PlayerData> getDataList() {
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
