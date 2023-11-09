package com.vaistra.config.spring_batch.demoCSV;

import com.vaistra.entities.DemoCSV;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ExportExcelWriter implements ItemWriter<DemoCSV> {
//    private static XSSFWorkbook workbook;
    private static SXSSFWorkbook workbook;
    private SXSSFSheet sheet;
    private SXSSFSheet sheet1;

    public ExportExcelWriter(String path) {
        workbook = new SXSSFWorkbook();
        sheet = workbook.createSheet("Date-Time Data");
        sheet1 = workbook.createSheet("Date-Time Data - 2");
        writeHeaderLine(sheet);
        writeHeaderLine(sheet1);
    }

    private void writeHeaderLine(SXSSFSheet sheet) {
        System.out.println("Creating Header");

        Row row = sheet.createRow(1);

        CellStyle style = workbook.createCellStyle();

        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setFontHeight(18);
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);

        // Create a CellRangeAddress for merging cells (e.g., from A1 to D1)
        CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, 1);

        // Set the title in the merged cells
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Country_list"); // Your title text
        titleCell.setCellStyle(style);

        // Merge the cells for the title
        sheet.addMergedRegion(mergedRegion);

        createCell(row, 0, "Date",style);
        createCell(row, 1, "Time",style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
//        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    int rowCount = 2;
    int rowCount1 = 2;
    @Override
    public void write(Chunk<? extends DemoCSV> chunk) throws Exception {
        try {
            System.out.println("In Data Write Method");
//            List<DemoCSV> dataToWrite = new ArrayList<>();

            CellStyle style = workbook.createCellStyle();
            XSSFFont font = (XSSFFont) workbook.createFont();
            font.setFontHeight(14);
            style.setFont(font);
            style.setWrapText(true);

            for (DemoCSV demoCSV : chunk) {
//                dataToWrite.add(demoCSV);
                if(!chunk.isEnd()) {
                    Row row ;
                    if(rowCount > 1048575) {
                        row = sheet1.createRow(rowCount1++);
                        createCell(row, 0, demoCSV.getDate().toString(), style);
                        createCell(row, 1, demoCSV.getTime().toString(), style);
                        System.out.println(" If " +demoCSV.getDate() + "\t" + demoCSV.getTime() + "\t" + rowCount1);
                    }
                    else {
                        row = sheet.createRow(rowCount++);
                        createCell(row, 0, demoCSV.getDate().toString(), style);
                        createCell(row, 1, demoCSV.getTime().toString(), style);
                        System.out.println(" Else  " +demoCSV.getDate() + "\t" + demoCSV.getTime() + "\t" + rowCount);
                    }
                }
                else
                    break;
            }
//            writeDataToExcel(dataToWrite,style);
            System.out.println("Out Of Loop From write Method");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void writeDataToExcel(List<DemoCSV> data, CellStyle style) {
//        for (DemoCSV demoCSV : data) {
//            Row row = sheet.createRow(rowCount++);
//            createCell(row, 0, demoCSV.getDate().toString(), style);
//            createCell(row, 1, demoCSV.getTime().toString(), style);
//            System.out.println(demoCSV.getDate() + "\t" + demoCSV.getTime() + "\t" + rowCount);
//        }
        System.out.println("Out Of Loop From writeDataToExcel Method");
    }

    public static void downloadFle(HttpServletResponse response, String filePath){
        System.out.println("Inside Download Method");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExcelExport.xlsx");

        // Stream the file to the client.
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             OutputStream out = response.getOutputStream()) {
            workbook.write(out);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                System.out.println(Arrays.toString(buffer));
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}