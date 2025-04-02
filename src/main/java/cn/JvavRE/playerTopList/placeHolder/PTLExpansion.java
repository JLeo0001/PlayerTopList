package cn.JvavRE.playerTopList.placeHolder;

import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.data.playerData.AbstractPlayerData;
import cn.JvavRE.playerTopList.data.topList.AbstractTopList;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PTLExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "ptl";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Jvav_Runtime_Environment";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        // ptl_player_rank_<listName>
        if (params.startsWith("player_rank_")) {
            String listName = params.substring(12);

            AbstractTopList topList = ListsMgr.getListByName(listName);
            if (topList == null) return null;

            return String.valueOf(topList.getPlayerRank(player));
        }

        // ptl_player_count_<listName>
        if (params.startsWith("player_count_")) {
            String listName = params.substring(13);

            AbstractTopList topList = ListsMgr.getListByName(listName);
            if (topList == null) return null;

            AbstractPlayerData playerData = topList.getDataByPlayer(player);
            if (playerData == null) return null;
            else return String.valueOf(playerData.getCount());
        }

        return null;
    }
}
