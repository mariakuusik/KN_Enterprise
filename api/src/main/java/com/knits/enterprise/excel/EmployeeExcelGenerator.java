package com.knits.enterprise.excel;

import com.knits.enterprise.model.company.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Data
@NoArgsConstructor
public class EmployeeExcelGenerator extends GenericExcelGenerator<Employee> {

    @Override
    protected List<String> getColumnHeaders() {

        return Arrays.asList("First Name", "Last Name", "Email", "Birth Date", "Gender", "Start Date",
                "End Date", "Company Phone", "Company Mobile Number", "Business Unit", "Country",
                "Department", "Job Title", "Organization");
    }

    @Override
    protected void fillDataRows(Sheet sheet, List<Employee> employees) {
        int rowNum = 1;
        CreationHelper creationHelper = sheet.getWorkbook().getCreationHelper();
        short dateFormat = creationHelper.createDataFormat().getFormat("yyyy-MM-dd");
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getFirstName());
            row.createCell(1).setCellValue(employee.getLastName());
            row.createCell(2).setCellValue(employee.getEmail());
            row.createCell(3).setCellValue(employee.getBirthDate());
            row.createCell(4).setCellValue(employee.getGender().name());
            row.createCell(5).setCellValue(employee.getStartDate());
            row.createCell(6).setCellValue(employee.getEndDate());
            row.createCell(7).setCellValue(employee.getCompanyPhone());
            if (employee.getBusinessUnit() != null) {
                row.createCell(8).setCellValue(employee.getBusinessUnit().getName());
            }
            if (employee.getOffice() != null) {
                row.createCell(9).setCellValue(employee.getOffice().getName());
            }
            if (employee.getDepartment() != null) {
                row.createCell(10).setCellValue(employee.getDepartment().getName());
            }
            if (employee.getJobTitle() != null) {
                row.createCell(11).setCellValue(employee.getJobTitle().getName());
            }
            if (employee.getOrganization() != null) {
                row.createCell(12).setCellValue(employee.getOrganization().getName());
            }
        }
        setDataTypeForColumn(sheet, 3, dateFormat);
        setDataTypeForColumn(sheet, 5, dateFormat);
        setDataTypeForColumn(sheet, 6, dateFormat);
    }

    private void setDataTypeForColumn(Sheet sheet, int columnIndex, short dateFormat) {
        for (Row row : sheet) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setDataFormat(dateFormat);
                cell.setCellStyle(cellStyle);
            }
        }
    }
}


