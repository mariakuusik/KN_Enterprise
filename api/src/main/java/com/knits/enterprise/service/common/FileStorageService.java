package com.knits.enterprise.service.common;

import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.EmployeeMapper;
import com.knits.enterprise.model.common.BinaryData;
import com.knits.enterprise.model.company.Contract;
import com.knits.enterprise.repository.common.BinaryDataRepository;
import com.knits.enterprise.repository.common.ContractRepository;
import com.knits.enterprise.service.company.EmployeeService;
import com.knits.enterprise.service.security.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FileStorageService {
    private final ContractRepository contractRepository;
    private final BinaryDataRepository binaryDataRepository;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final UserService userService;

    @Transactional
    public Contract uploadEmploymentContract(MultipartFile file, Long employeeId) throws IOException {
        EmployeeDto employeeDto = employeeService.findEmployeeById(employeeId);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        BinaryData binaryData = saveUploadedFile(file, fileName);
        deactivatePreviousEmployeeContracts(employeeId);
        Contract contract = createAndSaveNewContract(binaryData, employeeDto);

        return contractRepository.findById(contract.getId()).orElseThrow(() -> new UserException("Contract was not found"));
    }

    private BinaryData saveUploadedFile(MultipartFile file, String fileName) throws IOException {
        BinaryData binaryData = BinaryData.builder()
                .title(fileName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .bytes(file.getBytes())
                .build();
        binaryDataRepository.save(binaryData);
        return binaryData;
    }

    private void deactivatePreviousEmployeeContracts(Long employeeId) {
        List<Contract> contracts = contractRepository.findByEmployee_Id(employeeId);
        contracts.forEach(contract -> contract.setActive(false));
    }

    private Contract createAndSaveNewContract(BinaryData binaryData, EmployeeDto employeeDto) {
        Contract contract = Contract.builder()
                .binaryData(binaryData)
                .createdBy(userService.getCurrentUser())
                .employee(employeeMapper.toEntity(employeeDto))
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
        contractRepository.save(contract);
        return contract;
    }
}

