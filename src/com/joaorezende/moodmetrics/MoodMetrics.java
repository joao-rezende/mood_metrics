package com.joaorezende.moodmetrics;

public final class MoodMetrics {
    private static int totalHiddenAttributes = 0;
    private static int totalHiddenMethods = 0;
    private static int totalInheritedAttributes = 0;
    private static int totalInheritedMethods = 0;

    public static void sumHiddenAttributes(int number) {
        totalHiddenAttributes += number;
    }

    public static void sumHiddenMethods(int number) {
        totalHiddenMethods += number;
    }

    public static void sumInheritedAttributes(int number) {
        totalInheritedAttributes += number;
    }

    public static void sumInheritedMethods(int number) {
        totalInheritedMethods += number;
    }

    public static int getTotalHiddenAttributes() {
        return totalHiddenAttributes;
    }

    public static int getTotalHiddenMethods() {
        return totalHiddenMethods;
    }

    public static int getTotalInheritedAttributes() {
        return totalInheritedAttributes;
    }

    public static int getTotalInheritedMethods() {
        return totalInheritedMethods;
    }

    public static float AHF(int numberHiddenAttributes) {
        if (totalHiddenAttributes == 0)
            return 0;

        return (float) numberHiddenAttributes / (float) totalHiddenAttributes;
    }

    public static float MHF(int numberHiddenMethods) {
        if (totalHiddenMethods == 0)
            return 0;

        return (float) numberHiddenMethods / (float) totalHiddenMethods;
    }

    public static float AIF(int inheritedAttributes) {
        if (totalInheritedAttributes == 0)
            return 0;

        return (float) inheritedAttributes / (float) totalInheritedAttributes;
    }

    public static float MIF(int inheritedMethods) {

        return (float) inheritedMethods / (float) totalInheritedMethods;
    }
}