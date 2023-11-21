package com.knits.enterprise.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public abstract class GenericExcelGenerator<T> {

    protected abstract List<String> getColumnHeaders();

    protected abstract void fillDataRows(Sheet sheet, List<T> data);

    public byte[] generateExcelFile(List<T> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet data");

            createHeaderRow(sheet);
            fillDataRows(sheet, data);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createHeaderRow(Sheet sheet) {
        List<String> columnHeaders = getColumnHeaders();
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders.size(); i++) {
            headerRow.createCell(i).setCellValue(columnHeaders.get(i));
        }
    }
}
