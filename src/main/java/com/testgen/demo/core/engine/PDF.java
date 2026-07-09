package com.testgen.demo.core.engine;

// FIXED: Clean iText 8 package structures
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class PDF {

    public static String getPdfFile(String filename) {
        return "src/main/resources/pdf/" + filename + ".pdf";
    }

    public static void createPDF(String filename, String text) {
        if (text.isEmpty()) {
            text = "Test Generation Sheet Template Workspace";
        }

        String path = getPdfFile(filename);
        File file = new File(path);

        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try {
            PdfWriter pdfWriter = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);

            // add paragraph
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);

            // add  image
            String imgSrc = "src/main/resources/templates/image.jpg";
            ImageData imageData = ImageDataFactory.create(imgSrc);
            Image image = new Image(imageData);

            document.add(image);

            document.close();

            System.out.println("[PDF ENGINE] Successfully generated modern iText 8 document: " + file.getName());

        } catch (IOException e) {
            System.err.println("Failed to build PDF out to disk destination.");
            throw new RuntimeException(e);
        }
    }

    public static void createPdfWithHTML() {
        File html = new File(FileHandler.getHtmlFile("test_template"));
        Document doc = Jsoup.prase(html, "UTF-8");
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try { OutputStream os = new FileOutputStream }
    }
}