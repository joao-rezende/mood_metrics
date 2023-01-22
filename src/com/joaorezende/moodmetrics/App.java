package com.joaorezende.moodmetrics;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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

        if (!fileArgs.isDirectory() && (!fileArgs.exists() || !App.getExtensionFilename(fileArgs.getAbsolutePath()).equals("java"))) {
            System.out.println("Nenhum arquivo .java foi encontrado");
            System.exit(0);
        }

        List<File> files = new ArrayList<>();

        if (!fileArgs.isDirectory()) {
            files.add(fileArgs);
        } else {
            files = App.listFilesForFolder(fileArgs);
        }

        Stack<ReaderClass> classesWithSuperclass = new Stack<>();
        HashMap<String, ReaderClass> readerClassesHashMap = new HashMap<>();

        for (File file : files) {
            if (!App.getExtensionFilename(file.getAbsolutePath()).equals("java"))
                continue;

            Path pathFile = Paths.get(file.getAbsolutePath());
            ReaderClass readerClass = new ReaderClass(pathFile);

            readerClassesHashMap.put(readerClass.getClassName(), readerClass);

            MoodMetrics.sumHiddenAttributes(readerClass.getNumHiddenAttributes());
            MoodMetrics.sumHiddenMethods(readerClass.getNumHiddenMethods());
            
            if (readerClass.getSuperclassName() != null) {
                classesWithSuperclass.add(readerClass);
            }
        }

        while(!classesWithSuperclass.isEmpty()) {
            ReaderClass readerClass = classesWithSuperclass.pop();

            ReaderClass readerSuperclass = readerClassesHashMap.get(readerClass.getSuperclassName());

            if (readerSuperclass == null) {
                continue;
            }

            String attributesClass = readerClass.getAttributes();
            int numInheritedAttributes = 0;
            for (String inheritableAttribute : readerSuperclass.getInheritableAttributes()) {
                if (!attributesClass.contains(inheritableAttribute)) {
                    MoodMetrics.sumInheritedAttributes(1);
                    numInheritedAttributes++;
                }
            }
            readerClass.setNumInheritedAttributes(numInheritedAttributes);
            
            String methodsClass = readerClass.getMethods();
            int numInheritedMethod = 0;
            for (String inheritableMethod : readerSuperclass.getInheritableMethods()) {
                if (!methodsClass.contains(inheritableMethod)) {
                    MoodMetrics.sumInheritedMethods(1);
                    numInheritedMethod++;
                }
            }
            readerClass.setNumInheritedMethods(numInheritedMethod);
        }

        for (ReaderClass readerClass : readerClassesHashMap.values()) {
            System.out.println(readerClass.getClassName());

            System.out.printf("AHF: %.2f\n", MoodMetrics.AHF(readerClass.getNumHiddenAttributes()));
            System.out.printf("MHF: %.2f\n", MoodMetrics.MHF(readerClass.getNumHiddenMethods()));

            float AIF = 0;
            float MIF = 0;
            if (readerClass.getSuperclassName() != null) {
                AIF = MoodMetrics.AIF(readerClass.getNumInheritedAttributes());
                MIF = MoodMetrics.MIF(readerClass.getNumInheritedMethods());
            }
            System.out.printf("AIF: %.2f\n", AIF);
            System.out.printf("MIF: %.2f\n", MIF);

            System.out.println();
        }

        System.out.println("Total hidden attributes: " + MoodMetrics.getTotalHiddenAttributes());
        System.out.println("Total hidden method: " + MoodMetrics.getTotalHiddenMethods());
        System.out.println("Total inherited attributes: " + MoodMetrics.getTotalInheritedAttributes());
        System.out.println("Total inherited methods: " + MoodMetrics.getTotalInheritedMethods());
    }
}
