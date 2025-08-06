package cn.JvavRE.playerTopList.data.topList;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import net.kyori.adventure.text.format.TextColor;
import net.milkbowl.vault.economy.Economy;
import net.objecthunter.exp4j.Expression;
import org.bukkit.OfflinePlayer;

public class VaultTopList extends AbstractTopList {

    private final Economy economy;

    public VaultTopList(String name, TextColor nameColor,
                        boolean hidden, boolean reversed,
                        Expression expression, String formatter) {
        super(name, nameColor, hidden, reversed, expression, formatter);
        this.economy = PlayerTopList.getEconomy();
    }

    @Override
    public void updatePlayerData() {
        if (economy == null) return;

        for (PlayerData playerData : dataList) {
            OfflinePlayer player = playerData.getPlayer();
            // 确保玩家在经济系统中有账户
            if (economy.hasAccount(player)) {
                playerData.setCount(economy.getBalance(player));
            } else {
                playerData.setCount(0);
            }
        }
    }
}
