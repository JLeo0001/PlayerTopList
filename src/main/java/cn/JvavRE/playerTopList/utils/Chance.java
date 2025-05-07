package cn.JvavRE.playerTopList.utils;

public class Chance {
    public static boolean success(double chance) {
        return Math.random() < chance;
    }
}
