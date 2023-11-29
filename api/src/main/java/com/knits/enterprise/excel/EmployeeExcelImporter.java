package com.knits.enterprise.excel;

import com.knits.enterprise.config.Constants;
import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.EmployeeMapper;
import com.knits.enterprise.model.common.Organization;
import com.knits.enterprise.model.company.*;
import com.knits.enterprise.model.enums.Gender;
import com.knits.enterprise.model.location.Location;
import com.knits.enterprise.repository.company.EmployeeRepository;
import com.knits.enterprise.service.company.EmployeeService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@Data
@Slf4j
public class EmployeeExcelImporter {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    public EmployeeExcelImporter(EmployeeService employeeService, EmployeeMapper employeeMapper, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
    }

    public List <EmployeeDto> importEmployees(MultipartFile excelFile) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DD_MM_YYYY);
        List<EmployeeDto> employeeDtos = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(excelFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            List<String> columnHeaders = getColumnHeaders(sheet.getRow(0));
            validateColumnHeaders(columnHeaders);

            for (int rowNumber = 1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
                Row row = sheet.getRow(rowNumber);
                
                Employee employee = new Employee();

                employee.setFirstName(row.getCell(0).getStringCellValue());
                employee.setLastName(row.getCell(1).getStringCellValue());
                employee.setEmail(row.getCell(2).getStringCellValue());

                setBirthDate(row, employee, formatter);
               
                employee.setGender(getAndSetGender(row.getCell(4)));
              
                setStartDateOrNull(row, employee);
                setEndDateOrNull(row, employee);

                setCompanyPhoneNumberOrNull(row, employee);
                setCompanyMobileNumberOrNull(row, employee);
                setForeignKeyValues(row, employee);
                
                employeeRepository.saveAndFlush(employee);
                log.info("Employee saved");
                employeeDtos.add(employeeMapper.toDto(employee));
            }
        }
        return employeeDtos;
    }

    private List<String> getColumnHeaders(Row headerRow) {
        List<String> columnHeaders = new ArrayList<>();
        for (int i = 0; i <= headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                columnHeaders.add(cell.getStringCellValue());
            }
        }
        return columnHeaders;
    }

    private void validateColumnHeaders(List<String> columnHeaders) {
        if (!checkIfColumnHeadersMatchEntity(columnHeaders)) {
            throw new UserException("Column headers don't match with Entity names");
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

    private static void setBirthDate(Row row, Employee employee, DateTimeFormatter formatter) {
        Cell birthDateCell = row.getCell(3);
        if (birthDateCell != null) {
            if (birthDateCell.getCellType() == CellType.NUMERIC) {
                employee.setBirthDate(birthDateCell.getLocalDateTimeCellValue().toLocalDate());
            } else if (birthDateCell.getCellType() == CellType.STRING) {
                employee.setBirthDate(LocalDate.parse(birthDateCell.getStringCellValue(), formatter));
            }
        }
    }

    private Gender getAndSetGender(Cell genderCell) {
        if (genderCell != null) {
            String gender = genderCell.getStringCellValue();
            if (gender != null && !gender.isEmpty()) {
                try {
                    return Gender.valueOf(gender.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private static void setStartDateOrNull(Row row, Employee employee) {
        Cell startDateCell = row.getCell(5);
        if (startDateCell != null) {
            if (startDateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(startDateCell)) {
                // If cell is date in Excel format
                employee.setStartDate(startDateCell.getLocalDateTimeCellValue().toLocalDate());
            } else if (startDateCell.getCellType() == CellType.STRING) {
                // If cell is a string
                employee.setStartDate(LocalDate.parse(startDateCell.getStringCellValue()));
            }
        }
    }

    private static void setEndDateOrNull(Row row, Employee employee) {
        Cell endDateCell = row.getCell(6);
        if (endDateCell != null && endDateCell.getCellType() == CellType.STRING && !endDateCell.getStringCellValue().isEmpty()) {
            LocalDate endDate = LocalDate.parse(endDateCell.getStringCellValue());
            employee.setEndDate(endDate);
        }
    }

    private static void setCompanyPhoneNumberOrNull(Row row, Employee employee) {
        Cell companyPhoneNumberCell = row.getCell(7);
        if (companyPhoneNumberCell != null && companyPhoneNumberCell.getCellType() == CellType.STRING && !companyPhoneNumberCell.getStringCellValue().isEmpty()) {
            String companyPhoneNumber = companyPhoneNumberCell.getStringCellValue();
            employee.setCompanyPhone(companyPhoneNumber);
        } else {
            employee.setCompanyPhone("123456");
        }
    }

    private static void setCompanyMobileNumberOrNull(Row row, Employee employee) {
        Cell companyMobileNumberCell = row.getCell(8);
        if (companyMobileNumberCell != null && companyMobileNumberCell.getCellType() == CellType.STRING && !companyMobileNumberCell.getStringCellValue().isEmpty()) {
            String companyMobileNumber = companyMobileNumberCell.getStringCellValue();
            employee.setCompanyMobileNumber(companyMobileNumber);
        } else {
            employee.setCompanyMobileNumber("123456");
        }
    }

    //for Foreign Key values / index no 9 -16
    private static void setCellValueOrNull(Row row, Employee employee, int columnIndex, Function<Cell, String> valueExtractor, Consumer<String> setter) {
        Cell cell = row.getCell(columnIndex);
        if (cell != null) {
            String cellValue = valueExtractor.apply(cell);
            setter.accept(cellValue);
        }
    }

    private static void setForeignKeyValues(Row row, Employee employee) {
        setCellValueOrNull(row, employee, 9, Cell::getStringCellValue, value -> employee.setBusinessUnit(new BusinessUnit()));
        setCellValueOrNull(row, employee, 10, Cell::getStringCellValue, value -> employee.setOrganization(new Organization()));
        setCellValueOrNull(row, employee, 11, Cell::getStringCellValue, value -> employee.setOffice (new Location()));
        setCellValueOrNull(row, employee, 12, Cell::getStringCellValue, value -> employee.setJobTitle (new JobTitle()));
        setCellValueOrNull(row, employee, 13, Cell::getStringCellValue, value -> employee.setDepartment (new Department()));
        setCellValueOrNull(row, employee, 14, Cell::getStringCellValue, value -> employee.setDivision (new Division()));
        setCellValueOrNull(row, employee, 15, Cell::getStringCellValue, value -> employee.setSolidLineManager (new Employee()));
    }

}
