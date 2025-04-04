package cn.JvavRE.playerTopList.data.topList;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

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


    @Override
    public void updatePlayerData() {
        for (PlayerData playerData : dataList) {
            int total = 0;
            OfflinePlayer player = playerData.getPlayer();

            // 现在只需一次必要的循环
            switch (type.getType()) {
                case UNTYPED -> total = player.getStatistic(type);
                case BLOCK, ITEM -> subArgs.forEach(subArg -> player.getStatistic(type, (Material) subArg));
                case ENTITY -> subArgs.forEach(subArg -> player.getStatistic(type, (EntityType) subArg));
            }

            playerData.setCount(total);
        }
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

    private Double calc(PlayerData playerData) {
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
