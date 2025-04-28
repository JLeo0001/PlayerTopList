package cn.JvavRE.playerTopList.data.topList;

import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

public class CommonStatisticTopList extends AbstractTopList {
    private final Statistic type;

    public CommonStatisticTopList(String name, TextColor nameColor,
                                  boolean hidden, boolean reversed,
                                  Expression expression, String formatter,
                                  Statistic type) {

        super(name, nameColor, hidden, reversed, expression, formatter);

        this.type = type;
    }


    @Override
    public void updatePlayerData() {
        for (PlayerData playerData : dataList) {
            OfflinePlayer player = playerData.getPlayer();
            playerData.setCount(player.getStatistic(type));
        }
    }
}
