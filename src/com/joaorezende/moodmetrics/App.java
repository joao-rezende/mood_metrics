package com.joaorezende.moodmetrics;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
        String path = args.length > 0 ? args[0] : "";
        String formatReturn = args.length > 1 ? args[1] : "";

        Scanner scanner = new Scanner(System.in);

        if (path.isEmpty()) {
            System.out.print("Digite o caminho do arquivo ou diretório: ");
            path = scanner.nextLine();
        }

        if (formatReturn.isEmpty()) {
            while (!formatReturn.equals("1") && !formatReturn.equals("2")) {
                System.out.print("Qual o formato que deseja retornar (1 - PDF / 2 - Terminal): ");
                formatReturn = scanner.nextLine();
            }
        }

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

        Stack<ReaderClass> classesWithSuperclass = new Stack<>();
        HashMap<String, ReaderClass> readerClassesHashMap = new HashMap<>();

        for (File file : files) {
            if (!App.getExtensionFilename(file.getAbsolutePath()).equals("java"))
                continue;

            Path pathFile = Paths.get(file.getAbsolutePath());
            ReaderClass readerClass = new ReaderClass(pathFile);

            readerClassesHashMap.put(readerClass.getClassName(), readerClass);

            MoodMetrics.sumDefinedAttributes(readerClass.getNumAttributes());
            MoodMetrics.sumDefinedMethods(readerClass.getNumMethods());
            MoodMetrics.sumHiddenAttributes(readerClass.getNumHiddenAttributes());
            MoodMetrics.sumHiddenMethods(readerClass.getNumHiddenMethods());

            if (readerClass.getSuperclassName() != null) {
                classesWithSuperclass.add(readerClass);
            }
        }

        for (Map.Entry<String, ReaderClass> readerClassEntry : readerClassesHashMap.entrySet()) {
            ReaderClass readerClass = readerClassEntry.getValue();

            for (String dataType : readerClass.getDataTypes()) {
                if (readerClassesHashMap.get(dataType) != null && !readerClass.connectionExist(dataType)) {
                    readerClass.addConnection(dataType);
                }
            }

            MoodMetrics.sumConnections(readerClass.getConnections().size());
            MoodMetrics.setTotalAvailableConnections(readerClassesHashMap.size());
        }

        while (!classesWithSuperclass.isEmpty()) {
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
                } else {
                    MoodMetrics.sumOverrideMethods(1);
                }
            }
            readerClass.setNumInheritedMethods(numInheritedMethod);
            MoodMetrics.sumOverridePossibilities(readerSuperclass.getInheritableMethods().size());
        }

        if (formatReturn.equals("1")) {
            Result.pdf();
        } else {
            Result.terminal();
        }
    }
}
