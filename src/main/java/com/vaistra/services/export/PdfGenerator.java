package com.vaistra.services.export;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.vaistra.controllers.DemoController;
import com.vaistra.entities.DemoCSV;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.batch.item.Chunk;

import javax.print.Doc;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.awt.Color.BLACK;


public class PdfGenerator {


    private PdfGenerator generator;
    private PdfWriter writer;
    public static Document document;


    public PdfGenerator() {
        document = new Document(PageSize.A4);
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(DemoController.tempFile.getAbsolutePath()));
            writer.setPageEvent(new PageNumberEvent());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generate(Chunk<? extends DemoCSV> chunk){
        try {

            document.open();

            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Font dateTimeFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            dateTimeFont.setSize(10);
            dateTimeFont.setColor(BLACK);

            Paragraph dateTimeParagraph = new Paragraph(dateFormat.format(now), dateTimeFont);
            dateTimeParagraph.setAlignment(Element.ALIGN_RIGHT);

            document.add(dateTimeParagraph);

            Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            fontTitle.setSize(20);

            Paragraph paragraph1 = new Paragraph("Date and Time Data", fontTitle);
            paragraph1.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(paragraph1);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{1, 1});
            table.setSpacingBefore(10);

            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(CMYKColor.lightGray);
            cell.setPadding(5);
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            font.setColor(CMYKColor.WHITE);
            cell.setPhrase(new Phrase("Date", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Time", font));
            table.addCell(cell);

            for (DemoCSV d : chunk) {
                table.addCell(d.getDate().toString());
                table.addCell(d.getTime().toString());
            }

            document.add(table);
        } catch (Exception e) {
            System.out.println(e);
        }
//        finally {
//            document.close();
//        }
    }

    public static void downloadFle(HttpServletResponse response,String filePath){
        document.close();
        System.out.println("Inside Download Method");
        // Set the response headers for file download.
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename= exportPDF.pdf");

        // Stream the file to the client.
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                System.out.println(Arrays.toString(buffer));
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            document.close();
        }
    }


    class PageNumberEvent extends PdfPageEventHelper {
        public void onEndPage(PdfWriter writer, Document document) {
            // Add page number on each page
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();

            String text = "Page " + writer.getPageNumber();
            BaseFont bf = null;
            try {
                bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            float fontSize = 12;
            float margin = 10;
            float width = document.right() - document.left();

            cb.beginText();
            cb.setFontAndSize(bf, fontSize);
            cb.setTextMatrix(document.right() - width, margin);
            cb.showText(text);
            cb.endText();

            cb.restoreState();

        }
    }
}