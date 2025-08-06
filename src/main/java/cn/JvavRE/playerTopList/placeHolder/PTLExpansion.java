package cn.JvavRE.playerTopList.placeHolder;

import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import cn.JvavRE.playerTopList.data.topList.AbstractTopList;
import cn.JvavRE.playerTopList.formatOutput.jsonOutput;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
        // ptl_player_rank_sum_<listName1>_<listName2>_...
        if (params.startsWith("player_rank_sum_")) {
            String[] listNames = params.substring(16).split("_");

            int sum = 0;
            for (String listName : listNames) {
                AbstractTopList topList = ListsMgr.getListByName(listName);
                if (topList == null) continue;

                sum += topList.getPlayerRank(player);
            }

            return String.valueOf(sum);
        }

        // ptl_player_count_sum_<listName1>_<listName2>_...
        if (params.startsWith("player_count_sum_")) {
            String[] listNames = params.substring(17).split("_");

            double sum = 0;
            for (String listName : listNames) {
                AbstractTopList topList = ListsMgr.getListByName(listName);
                if (topList == null) continue;

                PlayerData playerData = topList.getDataByPlayer(player);
                if (playerData == null) continue;

                sum += playerData.getCount();
            }

            return String.valueOf(sum);
        }

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

            PlayerData playerData = topList.getDataByPlayer(player);
            if (playerData == null) return null;
            else return String.valueOf(playerData.getCount());
        }

        // ptl_get_json_<listName>
        if (params.startsWith("get_json_")) {
            String listName = params.substring(9);

            if (listName.equalsIgnoreCase("all")) {
                return jsonOutput.allLists2Json();
            }

            AbstractTopList topList = ListsMgr.getListByName(listName);
            if (topList == null) return null;

            return jsonOutput.topList2Json(topList);
        }

        // ptl_top_rank_<listName>
        if (params.startsWith("top_rank_")) {
            String listName = params.substring(9);
            AbstractTopList topList = ListsMgr.getListByName(listName);
            if (topList == null) {
                return "排行榜不存在";
            }

            List<PlayerData> dataList = topList.getDataList();
            if (dataList.isEmpty()) {
                return "排行榜暂无数据";
            }

            StringBuilder result = new StringBuilder();
            int limit = Math.min(10, dataList.size());

            for (int i = 0; i < limit; i++) {
                PlayerData playerData = dataList.get(i);
                String playerName = playerData.getPlayer().getName();
                if (playerName == null) {
                    playerName = "未知玩家";
                }
                String formattedCount = topList.getFormattedData(playerData);

                result.append(i + 1)
                        .append(". ")
                        .append(playerName)
                        .append(" - ")
                        .append(formattedCount);

                if (i < limit - 1) {
                    result.append("\n");
                }
            }

            return result.toString();
        }

        return null;
    }
}
