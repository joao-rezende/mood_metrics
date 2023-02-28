package com.joaorezende.moodmetrics;

public final class MoodMetrics {
    private static int totalDefinedAttributes = 0;
    private static int totalDefinedMethods = 0;
    private static int totalOverridePossibilities = 0;
    private static int totalHiddenAttributes = 0;
    private static int totalHiddenMethods = 0;
    private static int totalInheritedAttributes = 0;
    private static int totalInheritedMethods = 0;
    private static int totalOverrideMethods = 0;
    private static int totalConnections = 0;
    private static int totalAvailableConnections = 0;

    public static void setTotalAvailableConnections(int totalAvailableConnections) {
        MoodMetrics.totalAvailableConnections = totalAvailableConnections;
    }

    public static void sumDefinedAttributes(int number) {
        totalDefinedAttributes += number;
    }

    public static void sumDefinedMethods(int number) {
        totalDefinedMethods += number;
    }

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

    public static void sumOverrideMethods(int number) {
        totalOverrideMethods += number;
    }

    public static void sumOverridePossibilities(int number) {
        totalOverridePossibilities += number;
    }

    public static void sumConnections(int number) {
        totalConnections += number;
    }

    public static int getTotalHiddenAttributes() {
        return totalHiddenAttributes;
    }

    public static int getTotalDefinedAttributes() {
        return totalDefinedAttributes;
    }

    public static int getTotalDefinedMethods() {
        return totalDefinedMethods;
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

    public static int getTotalOverrideMethods() {
        return totalOverrideMethods;
    }

    public static int getTotalOverridePossibilities() {
        return totalOverridePossibilities;
    }

    public static int getTotalConnections() {
        return totalConnections;
    }

    public static int getTotalAvailableConnections() {
        return totalAvailableConnections;
    }

    public static float AHF() {
        if (totalDefinedAttributes == 0)
            return 0;

        return (float) totalHiddenAttributes / (float) totalDefinedAttributes;
    }

    public static float MHF() {
        if (totalHiddenMethods == 0)
            return 0;

        return (float) totalHiddenMethods / (float) totalDefinedMethods;
    }

    public static float AIF() {
        if (totalDefinedAttributes == 0)
            return 0;

        return (float) totalInheritedAttributes / (float) totalDefinedAttributes;
    }

    public static float MIF() {
        if (totalHiddenMethods == 0)
            return 0;

        return (float) totalInheritedMethods / (float) totalDefinedMethods;
    }

    public static float PF() {
        if (totalOverridePossibilities == 0)
            return 0;

        return (float) totalOverrideMethods / (float) totalOverridePossibilities;
    }

    public static float CF() {
        if (totalAvailableConnections <= 1)
            return 0;
        
        return (float) totalConnections / ((float) (totalAvailableConnections * totalAvailableConnections) - (float) totalAvailableConnections);
    }
}