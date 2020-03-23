package fr.keke142.ivernyavotes.utils;

public final class NumbersUtil {
    private NumbersUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static int roundUp(int number, int multipleOf) {
        int a = (number / multipleOf) * multipleOf;
        int b = a + multipleOf;
        return (number - a > b - number) ? b : a;
    }
}
