package com.testgen.demo.core.engine;

// FIXED: Clean iText 8 package structures
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
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.testgen.demo.core.config.FileHandler;
import org.springframework.stereotype.Service;

import java.io.*;

import static com.testgen.demo.core.config.FileHandler.getPdfFile;

@Service
public class PDF {

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

    public static void createPdfWithHTML(String processedHtml) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            DefaultFontProvider defaultFont = new DefaultFontProvider(false, true, false);

            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setFontProvider(defaultFont);

            HtmlConverter.convertToPdf(processedHtml, pdfWriter, converterProperties);

            FileOutputStream fileOutputStream = new FileOutputStream("");
            byteArrayOutputStream.write(fileOutputStream);
            byteArrayOutputStream.close();

            byteArrayOutputStream.flush();
            fileOutputStream.close();

            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}