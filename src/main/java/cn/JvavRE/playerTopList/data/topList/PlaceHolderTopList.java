package cn.JvavRE.playerTopList.data.topList;

import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import cn.JvavRE.playerTopList.utils.Digit;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;

public class PlaceHolderTopList extends AbstractTopList {
    private final String placeHolder;

    public PlaceHolderTopList(String name, TextColor nameColor, Expression expression, String formatter,
                              String placeHolder) {

        super(name, nameColor, expression, formatter);

        this.placeHolder = placeHolder;
    }

    @Override
    public void updatePlayerData() {
        for (PlayerData playerData : dataList) {
            String placeHolderResult = PlaceholderAPI.setPlaceholders(playerData.getPlayer(), placeHolder);

            playerData.setCount(Digit.isDigit(placeHolderResult) ? Double.parseDouble(placeHolderResult) : 0.0);
        }
    }
}
