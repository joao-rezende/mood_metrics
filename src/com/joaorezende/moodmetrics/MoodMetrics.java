package com.joaorezende.moodmetrics;

import java.io.Reader;
import java.util.HashMap;

public final class MoodMetrics {
    private static int totalPrivateVars = 0;
    private static int totalPrivateMethods = 0;
    private static int totalInheritedVars = 0;
    private static int totalInheritedMethods = 0;
    private static HashMap<String, ReaderClass> readerClasses = new HashMap<String, ReaderClass>();

    public static void sumPrivateVars(int number) {
        totalPrivateVars += number;
    }

    public static void sumPrivateMethods(int number) {
        totalPrivateMethods += number;
    }

    public static void sumInheritedVars(int number) {
        totalInheritedVars += number;
    }

    public static void sumInheritedMethods(int number) {
        totalInheritedMethods += number;
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

    public static float AIF(ReaderClass readerClass) {
        if (readerClass.getSuperclassName().equals("null"))
            return 0;

        int inheritedVars = 0;

        ReaderClass superClass = readerClasses.get(readerClass.getSuperclassName());

        inheritedVars += superClass.getNumDefaultVars();
        inheritedVars += superClass.getNumPublicVars();
        inheritedVars += superClass.getNumProtectedVars();

        inheritedVars += AIF(superClass);

        return (float) inheritedVars / (float) totalInheritedVars;
    }

    public static float MIF(ReaderClass readerClass) {
        if (readerClass.getSuperclassName().equals("null"))
            return 0;

        int inheritedMethods = 0;

        ReaderClass superClass = readerClasses.get(readerClass.getSuperclassName());

        inheritedMethods += superClass.getNumDefaultMethods();
        inheritedMethods += superClass.getNumPublicMethods();
        inheritedMethods += superClass.getNumProtectedMethods();

        inheritedMethods += MIF(superClass);

        return (float) inheritedMethods / (float) totalInheritedMethods;
    }

    public static int getTotalPrivateVars() {
        return totalPrivateVars;
    }

    public static int getTotalPrivateMethods() {
        return totalPrivateMethods;
    }

    public static void addClasse(ReaderClass readerClass) {
        readerClasses.put(readerClass.getClassName(), readerClass);
    }

    public static ReaderClass getReaderClass(String className) {
        return readerClasses.get(className);
    }

    public static HashMap<String, ReaderClass> getReaderClasses() {
        return readerClasses;
    }
}
