package cn.JvavRE.playerTopList.utils;

public class Digit {
    public static boolean isDigit(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
