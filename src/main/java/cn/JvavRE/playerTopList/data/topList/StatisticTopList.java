package cn.JvavRE.playerTopList.data.topList;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.data.playerData.AbstractPlayerData;
import cn.JvavRE.playerTopList.data.playerData.StatisticPlayerData;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.IllegalFormatConversionException;
import java.util.List;

public class StatisticTopList extends AbstractTopList {
    private final Statistic type;
    private final List<?> subArgs;

    public StatisticTopList(String name, TextColor nameColor, Statistic type, List<?> subArgs,
                            Expression expression, String formatter) {

        super(name, nameColor, expression, formatter);

        this.type = type;
        this.subArgs = subArgs;

        initDataList();
        updateDataList();
    }

    public void updateDataList() {
        List<OfflinePlayer> players = Arrays.stream(Bukkit.getOfflinePlayers()).toList();

        // 检查是否存在新玩家, 不需要每次都刷新dataList
        if (dataList.size() != players.size()) {
            List<OfflinePlayer> oldPlayers = dataList.stream().map(AbstractPlayerData::getPlayer).toList();

            List<OfflinePlayer> newPlayers = players.stream().filter(player -> !oldPlayers.contains(player)).toList();
            for (OfflinePlayer player : newPlayers) {
                dataList.add(StatisticPlayerData.of(player));
            }
        }

        dataList.forEach(playerData -> playerData.updateCount(type, subArgs));
        sortDataList();

        // 更新时间
        lastUpdate = LocalDateTime.now();

        // 更新ui
        ui.update();
    }

    public String getFormattedData(AbstractPlayerData playerData) {
        try {
            return formatter.formatted(calc(playerData));
        } catch (IllegalFormatConversionException e) {
            formatter = "%.0f";
            PlayerTopList.getInstance().getLogger().warning("格式化字符串出现错误, 已重置为%.0f");
            return formatter.formatted(calc(playerData));
        }
    }

    private Double calc(AbstractPlayerData playerData) {
        try {
            return expression == null ?
                    playerData.getCount() :
                    expression.setVariable("count", playerData.getCount()).evaluate();

        } catch (Exception e) {
            expression = null;
            PlayerTopList.getInstance().getLogger().warning("表达式出现错误, 已禁用表达式");
            return (double) playerData.getCount();
        }
    }
}
