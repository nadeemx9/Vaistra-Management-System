//package com.vaistra.services.export;
//
//import com.lowagie.text.pdf.PdfPageEventHelper;
//import com.vaistra.controllers.DemoController;
//import com.vaistra.entities.DemoCSV;
//import com.vaistra.entities.cscv.Country;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.batch.item.Chunk;
//
//import javax.swing.text.Style;
//import java.io.*;
//import java.util.Arrays;
//
//public class ExcelGenerator extends PdfPageEventHelper{
//    private XSSFWorkbook workbook;
//    private XSSFSheet sheet;
//
//    public ExcelGenerator() {
//        workbook = new XSSFWorkbook();
//        sheet = workbook.createSheet("Countries Data");
//        writeHeaderLine();
//    }
//
//    private void writeHeaderLine() {
//        System.out.println("Creating Header");
//
//        Row row = sheet.createRow(1);
//
//        CellStyle style = workbook.createCellStyle();
//
//        XSSFFont font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeight(18);
//        style.setFont(font);
//
//        style.setAlignment(HorizontalAlignment.CENTER);
//
//        // Create a CellRangeAddress for merging cells (e.g., from A1 to D1)
//        CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, 1);
//
//        // Set the title in the merged cells
//        Row titleRow = sheet.createRow(0);
//        Cell titleCell = titleRow.createCell(0);
//        titleCell.setCellValue("Country_list"); // Your title text
//        titleCell.setCellStyle(style);
//
//        // Merge the cells for the title
//        sheet.addMergedRegion(mergedRegion);
//
//        createCell(row, 0, "Date",style);
//        createCell(row, 1, "Time",style);
//
//    }
//    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
//        sheet.autoSizeColumn(columnCount);
//        Cell cell = row.createCell(columnCount);
//        if (value instanceof Integer) {
//            cell.setCellValue((Integer) value);
//        } else if (value instanceof Boolean) {
//            cell.setCellValue((Boolean) value);
//        } else {
//            cell.setCellValue((String) value);
//        }
//        cell.setCellStyle(style);
//    }
//
//    public void writeDataLines(Chunk<? extends DemoCSV> chunk) {
////    public void writeDataLines() {
//        try {
//            System.out.println("In Data Write Method");
//            int rowCount = 2;
//
//            CellStyle style = workbook.createCellStyle();
//            XSSFFont font = workbook.createFont();
//            font.setFontHeight(14);
//            style.setFont(font);
//            style.setWrapText(true);
//
//            for (DemoCSV demoCSV : chunk) {
//                if(!chunk.isEnd()) {
//                    Row row = sheet.createRow(rowCount++);
//                    createCell(row, 0, demoCSV.getDate().toString(), style);
//                    createCell(row, 1, demoCSV.getTime().toString(), style);
////                    System.out.println(demoCSV.getDate() + "\t" + demoCSV.getTime() + "\t" + rowCount);
//                }
//            }
//            System.out.println("Out Of Loop");
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
////    public void export(Chunk<? extends DemoCSV> chunk) throws IOException {
////        System.out.println("Inside Export Method!");
////        writeDataLines(chunk);
////        try (FileOutputStream outputStream = new FileOutputStream(DemoController.tempExcelFile.getAbsolutePath())) {
////            workbook.write(outputStream);
////        }
////    }
//
//    public void downloadFle(HttpServletResponse response,String filePath){
//        System.out.println("Inside Download Method");
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=ExcelExport.xlsx");
//
//        // Stream the file to the client.
//        try (FileInputStream fileInputStream = new FileInputStream(filePath);
//             OutputStream out = response.getOutputStream()) {
//            workbook.write(out);
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                System.out.println(Arrays.toString(buffer));
//                out.write(buffer, 0, bytesRead);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}