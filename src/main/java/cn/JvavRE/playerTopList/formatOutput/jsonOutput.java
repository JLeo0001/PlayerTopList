package cn.JvavRE.playerTopList.formatOutput;

import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import cn.JvavRE.playerTopList.data.topList.AbstractTopList;


public class jsonOutput {
    public static String topList2Json(AbstractTopList toplist) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"name\":").append("\"").append(toplist.getName()).append("\",");
        builder.append("\"color\":").append("\"").append(toplist.getNameColor().asHexString()).append("\", ");
        builder.append("\"data\":").append("{");

        for (PlayerData playerData : toplist.getDataList()) {
            builder.append("\"")
                    .append(playerData.getPlayer().getName())
                    .append("\"")
                    .append(":")
                    .append("\"")
                    .append(toplist.getFormattedData(playerData))
                    .append("\"")
                    .append(",");
        }

        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        builder.append("}");

        return builder.toString();
    }

    public static String allLists2Json() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"lists\":[");

        for (AbstractTopList toplist : ListsMgr.getTopLists()) {
            builder.append("\"")
                    .append(toplist.getName())
                    .append("\"")
                    .append(",");
        }

        builder.deleteCharAt(builder.length() - 1);
        builder.append("]}");

        return builder.toString();
    }
}
