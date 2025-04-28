package cn.JvavRE.playerTopList.data.topList;

import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.util.List;

public class MaterialStatisticTopList extends AbstractTopList {
    private final Statistic type;
    private final List<Material> subArgs;

    public MaterialStatisticTopList(String name, TextColor nameColor,
                                    boolean hidden, boolean reversed,
                                    Expression expression, String formatter,
                                    Statistic type, List<Material> subArgs) {

        super(name, nameColor, hidden, reversed, expression, formatter);

        this.type = type;
        this.subArgs = subArgs;
    }


    @Override
    public void updatePlayerData() {
        for (PlayerData playerData : dataList) {
            int total = 0;
            OfflinePlayer player = playerData.getPlayer();

            for (Material subArg : subArgs) {
                total += player.getStatistic(type, subArg);
            }

            playerData.setCount(total);
        }
    }
}

