package utils;

import java.text.DecimalFormat;

public class CurrencyUtil {

    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public static String format(double amount) {
        return "P" + formatter.format(amount);
    }
}
