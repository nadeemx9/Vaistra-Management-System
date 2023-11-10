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
    private static final int MAX_ROWS_PER_SHEET = 1048575; // Maximum number of rows per sheet
    private static SXSSFWorkbook workbook;
    private int currentSheetIndex = 0;
    private SXSSFSheet currentSheet;

    public ExportExcelWriter(String path) {
        workbook = new SXSSFWorkbook();
        createNewSheet();
    }

    private void createNewSheet() {
        currentSheet = workbook.createSheet("Data - " + (currentSheetIndex + 1));
        writeHeaderLine(currentSheet);
        currentSheetIndex++;
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
        titleCell.setCellValue("Date-Time Data"); // Your title text
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

    @Override
    public void write(Chunk<? extends DemoCSV> chunk) throws Exception {
        try {
            System.out.println("In Data Write Method");
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = (XSSFFont) workbook.createFont();
            font.setFontHeight(14);
            style.setFont(font);
            style.setWrapText(true);

            for (DemoCSV demoCSV : chunk) {
                if (currentSheet.getLastRowNum() >= MAX_ROWS_PER_SHEET) {
                    // If the current sheet is full, create a new sheet
                    createNewSheet();
                }

                Row row = currentSheet.createRow(currentSheet.getLastRowNum() + 1);
                createCell(row, 0, demoCSV.getDate().toString(), style);
                createCell(row, 1, demoCSV.getTime().toString(), style);
                System.out.println(demoCSV.getDate() + "\t" + demoCSV.getTime() + "\t" + currentSheetIndex);
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