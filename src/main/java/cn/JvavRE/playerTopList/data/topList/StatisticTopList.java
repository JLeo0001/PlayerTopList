package cn.JvavRE.playerTopList.data.topList;

import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import java.util.List;

public class StatisticTopList extends AbstractTopList {
    private final Statistic type;
    private final List<?> subArgs;

    public StatisticTopList(String name, TextColor nameColor, Expression expression, String formatter,
                            Statistic type, List<?> subArgs) {

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
                case BLOCK, ITEM -> {
                    for (Object subArg : subArgs) {
                        total += player.getStatistic(type, (Material) subArg);
                    }
                }
                case ENTITY -> {
                    for (Object subArg : subArgs) {
                        total += player.getStatistic(type, (EntityType) subArg);
                    }
                }
            }

            playerData.setCount(total);
        }
    }
}
