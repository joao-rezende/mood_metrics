package com.joaorezende.moodmetrics;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.joaorezende.moodmetrics.ReaderClass;

public class App {
    public static List<File> listFilesForFolder(final File folder) {
        List<File> files = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                files.addAll(listFilesForFolder(fileEntry));
            } else if (fileEntry.exists()) {
                files.add(fileEntry);
            }
        }

        return files;
    }

    private static String getExtensionFilename(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }

    public static void main(String[] args) throws Exception {
        String path = args[0];

        File fileArgs = new File(path);

        if (!fileArgs.isDirectory()
                && (!fileArgs.exists() || !App.getExtensionFilename(fileArgs.getAbsolutePath()).equals("java"))) {
            System.out.println("Nenhum arquivo .java foi encontrado");
            System.exit(0);
        }

        List<File> files = new ArrayList<>();

        if (!fileArgs.isDirectory()) {
            files.add(fileArgs);
        } else {
            files = App.listFilesForFolder(fileArgs);
        }

        Stack<String> superClasses = new Stack<String>();

        for (File file : files) {
            if (!App.getExtensionFilename(file.getAbsolutePath()).equals("java"))
                continue;

            Path pathFile = Paths.get(file.getAbsolutePath());
            ReaderClass readerClass = new ReaderClass(pathFile);

            MoodMetrics.addClasse(readerClass);

            System.out.println("Class: " + readerClass.getClassName());

            MoodMetrics.sumPrivateVars(readerClass.getNumPrivateVars());
            MoodMetrics.sumPrivateMethods(readerClass.getNumPrivateMethods());

            if (!readerClass.getSuperclassName().equals("null")) {
                System.out.println("Superclass: " + readerClass.getSuperclassName());
                superClasses.push(readerClass.getSuperclassName());
            }
        }

        System.out.println("----------------------------------------------");

        while (!superClasses.isEmpty()) {
            String superClassName = superClasses.pop();
            System.out.print("Superclass: " + superClassName);

            ReaderClass superReaderClass = MoodMetrics.getReaderClass(superClassName);

            if (superReaderClass == null) {
                System.out.println(" - NULL");
                continue;
            }

            System.out.print("\n");

            MoodMetrics.sumInheritedVars(superReaderClass.getNumDefaultVars());
            MoodMetrics.sumInheritedVars(superReaderClass.getNumPublicVars());
            MoodMetrics.sumInheritedVars(superReaderClass.getNumProtectedVars());

            MoodMetrics.sumInheritedMethods(superReaderClass.getNumDefaultMethods());
            MoodMetrics.sumInheritedMethods(superReaderClass.getNumPublicMethods());
            MoodMetrics.sumInheritedMethods(superReaderClass.getNumProtectedMethods());
        }

        for (Map.Entry<String, ReaderClass> readerClassHash : MoodMetrics.getReaderClasses().entrySet()) {
            ReaderClass readerClass = readerClassHash.getValue();

            System.out.println(readerClass.getClassName());

            System.out.printf("AHF: %.2f\n", MoodMetrics.AHF(readerClass.getNumPrivateVars()));
            System.out.printf("MHF: %.2f\n", MoodMetrics.MHF(readerClass.getNumPrivateMethods()));
            System.out.printf("AIF: %.2f\n", MoodMetrics.AIF(readerClass));
            System.out.printf("MIF: %.2f\n", MoodMetrics.MIF(readerClass));
        }
    }
}
