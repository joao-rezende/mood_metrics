package com.joaorezende.moodmetrics;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

        List<ReaderClass> readerClasses = new ArrayList<>();

        for (File file : files) {
            Path pathFile = Paths.get(file.getAbsolutePath());
            ReaderClass readerClass = new ReaderClass(pathFile);
            readerClasses.add(readerClass);

            MoodMetrics.sumPrivateVars(readerClass.getNumPrivateVars());
            MoodMetrics.sumPrivateMethods(readerClass.getNumPrivateMethods());
        }

        for (ReaderClass readerClass : readerClasses) {
            System.out.println(readerClass.getClassName());
            System.out.printf("AHF: %.2f\n", MoodMetrics.AHF(readerClass.getNumPrivateVars()));
            System.out.printf("MHF: %.2f\n", MoodMetrics.MHF(readerClass.getNumPrivateMethods()));
        }
    }
}
