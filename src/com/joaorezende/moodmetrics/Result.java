package com.joaorezende.moodmetrics;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class Result {
    private static String generateMetrics() {
        String metrics = String.format("AHF: %.2f\n", MoodMetrics.AHF());
        metrics += String.format("MHF: %.2f\n", MoodMetrics.MHF());
        metrics += String.format("AIF: %.2f\n", MoodMetrics.AIF());
        metrics += String.format("MIF: %.2f\n", MoodMetrics.MIF());
        metrics += String.format("PF: %.2f\n", MoodMetrics.PF());
        metrics += String.format("CF: %.2f\n", MoodMetrics.CF());

        return metrics;
    }

    private static String generateTotal() {
        String total = "Total defined attributes: " + MoodMetrics.getTotalDefinedAttributes() + "\n";
        total += "Total hidden attributes: " + MoodMetrics.getTotalHiddenAttributes() + "\n";
        total += "Total hidden method: " + MoodMetrics.getTotalHiddenMethods() + "\n";
        total += "Total inherited attributes: " + MoodMetrics.getTotalInheritedAttributes() + "\n";
        total += "Total inherited methods: " + MoodMetrics.getTotalInheritedMethods() + "\n";
        total += "Total override methods: " + MoodMetrics.getTotalOverrideMethods() + "\n";
        total += "Total override possibilities: " + MoodMetrics.getTotalOverridePossibilities() + "\n";
        total += "Total connections: " + MoodMetrics.getTotalConnections() + "\n";
        total += "Total available connections: " + MoodMetrics.getTotalAvailableConnections();

        return total;
    }

    public static void pdf() {
        // Cria um novo arquivo PDF
        File file = new File("results/Result_Mood_Metrics.pdf");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cria um novo documento PDF
        PdfDocument pdfDoc = null;
        try {
            pdfDoc = new PdfDocument(new PdfWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cria um novo documento iText
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Mood Metrics"));
        doc.add(new Paragraph(generateMetrics()));
        doc.add(new Paragraph(generateTotal()));

        // Fecha o documento
        doc.close();
    }

    public static void terminal() {
        System.out.println("Mood Metrics");
        System.out.println();
        System.out.println(generateMetrics());
        System.out.println(generateTotal());
    }
}
