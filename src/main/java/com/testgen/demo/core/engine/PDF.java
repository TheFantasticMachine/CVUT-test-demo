package com.testgen.demo.core.engine;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.testgen.demo.core.config.FileHandler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.testgen.demo.core.config.FileHandler.getPdfFile;

@Service
public class PDF {

    // Old manual paragraph/image builder method
    public static void createPDF(String filename, String text) {
        if (text.isEmpty()) {
            text = "Test Generation Sheet Template Workspace";
        }

        String path = getPdfFile(filename);
        File file = new File(path);

        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (PdfWriter pdfWriter = new PdfWriter(file);
             PdfDocument pdfDocument = new PdfDocument(pdfWriter)) {

            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);

            String imgSrc = "src/main/resources/templates/image.jpg";
            File imageFile = new File(imgSrc);
            if (imageFile.exists()) {
                ImageData imageData = ImageDataFactory.create(imgSrc);
                Image image = new Image(imageData);
                document.add(image);
            }

            document.close();
            System.out.println("[PDF ENGINE] Successfully generated modern iText 8 document: " + file.getName());

        } catch (IOException e) {
            System.err.println("Failed to build PDF out to disk destination.");
            throw new RuntimeException(e);
        }
    }

    // FIXED: Takes a target filename string, handles folder creation, and streams HTML direct to disk
    public static void createPdfWithHTML(String filename, String processedHtml) {
        if (processedHtml == null || processedHtml.isEmpty()) {
            System.err.println("[PDF ENGINE] Aborting compile: Processed HTML payload data content string is empty.");
            return;
        }

        // Generate absolute resource save path matching your project schema
        String path = getPdfFile(filename);
        File targetFile = new File(path);

        // Ensure directories exist on your machine before opening file streams
        if (targetFile.getParentFile() != null) {
            targetFile.getParentFile().mkdirs();
        }

        // Try-with-resources handles closing your FileOutputStream automatically!
        try (FileOutputStream fileOutputStream = new FileOutputStream(targetFile)) {

            DefaultFontProvider defaultFont = new DefaultFontProvider(false, true, false);
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setFontProvider(defaultFont);

            // Directly converts the template string into the file on your disk drive
            HtmlConverter.convertToPdf(processedHtml, fileOutputStream, converterProperties);

            System.out.println("[PDF ENGINE] Flawlessly compiled Thymeleaf HTML template directly into PDF: " + targetFile.getName());

        } catch (Exception e) {
            System.err.println("[PDF ENGINE] Failed to compile HTML context stream directly to disk.");
            e.printStackTrace();
        }
    }
}