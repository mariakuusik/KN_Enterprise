package com.knits.enterprise.excel;

import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.EmployeeMapper;
import com.knits.enterprise.model.company.Employee;
import com.knits.enterprise.service.company.EmployeeService;
import lombok.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class EmployeeExcelImporter {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public void importEmployees(MultipartFile excelFile) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(excelFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            List<String> columnHeaders = new ArrayList<>();

            if (!checkIfColumnHeadersMatchEntity(columnHeaders)) {
                throw new UserException("Column headers don't match with Entity names");
            }

            for (int rowNumber = 1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
                Row row = sheet.getRow(rowNumber);

                //Not all values are set to Employee.
                //Each Foreign Key value should have a separate method to get ID?

                Employee employee = new Employee();
                employee.setFirstName(row.getCell(0).getStringCellValue());
                employee.setLastName(row.getCell(1).getStringCellValue());
                employee.setEmail(row.getCell(2).getStringCellValue());
                employee.setBirthDate(LocalDate.parse(row.getCell(3).getLocalDateTimeCellValue().toString()));
                employee.setStartDate(LocalDate.parse(row.getCell(5).getStringCellValue()));
                employee.setEndDate(LocalDate.parse(row.getCell(6).getStringCellValue()));
                employee.setCompanyPhone(row.getCell(7).getStringCellValue());
                employee.setCompanyPhone(row.getCell(8).getStringCellValue());
                EmployeeDto dto = employeeMapper.toDto(employee);
                employeeService.saveNewEmployee(dto);
            }
        }
    }

    protected boolean checkIfColumnHeadersMatchEntity(List<String> columnHeaders) {
        List<String> entityNames = employeeService.getEntityNamesForEmployee();
        for (int i = 0; i < columnHeaders.size(); i++) {
            if (!columnHeaders.get(i).equals(entityNames.get(i))) {
                return false;
            }
        }
        return true;
    }
}
