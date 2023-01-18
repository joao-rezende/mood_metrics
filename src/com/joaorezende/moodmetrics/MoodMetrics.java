package com.joaorezende.moodmetrics;

public final class MoodMetrics {
    private static int totalPrivateVars = 0;
    private static int totalPrivateMethods = 0;

    public static void sumPrivateVars(int number) {
        totalPrivateVars += number;
    }

    public static void sumPrivateMethods(int number) {
        totalPrivateMethods += number;
    }

    public static float AHF(int numberPrivateVars) {
        if (totalPrivateVars == 0)
            return 0;

        return (float) numberPrivateVars / (float) totalPrivateVars;
    }

    public static float MHF(int numberPrivateMethods) {
        if (totalPrivateMethods == 0)
            return 0;

        return (float) numberPrivateMethods / (float) totalPrivateMethods;
    }

    public static int getTotalPrivateVars() {
        return totalPrivateVars;
    }

    public static int getTotalPrivateMethods() {
        return totalPrivateMethods;
    }
}
