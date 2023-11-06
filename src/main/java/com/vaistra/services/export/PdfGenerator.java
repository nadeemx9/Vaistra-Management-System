package com.vaistra.services.export;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.vaistra.controllers.DemoController;
import com.vaistra.entities.DemoCSV;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.batch.item.Chunk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static java.awt.Color.BLACK;


public class PdfGenerator {


    private final String currentDateTime; // Add this field


    public PdfGenerator(String listofUsers, String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }


    public PdfGenerator(String currentDateTime) {

        this.currentDateTime = currentDateTime;
    }


    public void generate(Chunk<? extends DemoCSV> user) throws DocumentException, IOException {


        String filePath = DemoController.tempFile.getAbsolutePath();

        Document document = new Document(PageSize.A4);

        try {

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            writer.setPageEvent(new PageNumberEvent());

            document.open();


            Date now = new Date(); // Get the current date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define the format

            String formattedDateTime = dateFormat.format(now); // Format the date and time

            Font dateTimeFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            dateTimeFont.setSize(10);
            dateTimeFont.setColor(BLACK);

            Paragraph dateTimeParagraph = new Paragraph(formattedDateTime, dateTimeFont);
            dateTimeParagraph.setAlignment(Element.ALIGN_RIGHT);

            document.add(dateTimeParagraph);

            Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            fontTiltle.setSize(20);

            Paragraph paragraph1 = new Paragraph("Date and Time Data", fontTiltle);
            paragraph1.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(paragraph1);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.setWidths(new int[]{8, 8});
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

            for (DemoCSV d : user) {
//                System.out.println(d.getDate() + "\t" + d.getTime());
                table.addCell(d.getDate().toString());
                table.addCell(d.getTime().toString());
            }
            document.add(table);

        } finally {
            document.close();
        }

    }

    public static void downloadFle(HttpServletResponse response,String filePath){
        System.out.println("Inside Download Method");
        // Set the response headers for file download.
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename= export_PDF.pdf");

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
    }


    public static class PageNumberEvent extends PdfPageEventHelper {
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