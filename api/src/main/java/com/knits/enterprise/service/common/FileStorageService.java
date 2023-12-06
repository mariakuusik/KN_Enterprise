package com.knits.enterprise.service.common;

import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.dto.search.ContractSearchDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.EmployeeMapper;
import com.knits.enterprise.model.common.BinaryData;
import com.knits.enterprise.model.company.Contract;
import com.knits.enterprise.repository.common.BinaryDataRepository;
import com.knits.enterprise.repository.common.ContractRepository;
import com.knits.enterprise.service.company.EmployeeService;
import com.knits.enterprise.service.security.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        //downloading multiple contracts as .zip file conflicts with recurring contract names
        verifyContractNameIsUnique(file);

        EmployeeDto employeeDto = employeeService.findEmployeeById(employeeId);
        String fileName = FilenameUtils.getName(file.getOriginalFilename());

        BinaryData binaryData = saveUploadedFile(file, fileName);
        deactivatePreviousEmployeeContracts(employeeId);
        Contract contract = createAndSaveNewContract(binaryData, employeeDto);

        return contractRepository.findById(contract.getId()).orElseThrow(() -> new UserException("Contract was not found"));
    }

    @Transactional
    public BinaryData downloadEmploymentContract(Long employeeId) {
        Contract activeEmploymentContract = contractRepository.findByEmployee_IdAndActive(employeeId, true);
        if (activeEmploymentContract != null) {
            return activeEmploymentContract.getBinaryData();
        } else throw new UserException("Contract for employee " + employeeId + " was not found");
    }

    public byte[] filterContracts(ContractSearchDto contractSearchDto) throws IOException {
        Specification<Contract> specification = contractSearchDto.getSpecification();
        List<Contract> contracts = contractRepository.findAll(specification);
        return generateZipFile(contracts);
    }

    private void verifyContractNameIsUnique(MultipartFile file) {
        boolean contractNameExists = contractRepository.existsByBinaryData_Title(file.getOriginalFilename());
        if (contractNameExists){
            throw new UserException("Contract for this employee with the name " + file.getOriginalFilename() + " already exists," +
                    "please choose a different file name");
        }
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

    private byte[] generateZipFile(List<Contract> contracts) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(baos);

        for (Contract contract : contracts) {
            BinaryData binaryData = contract.getBinaryData();
            byte[] bytes = binaryData.getBytes();
            String fileName = binaryData.getTitle();

            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(bytes);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
        zipOutputStream.flush();

        return baos.toByteArray();
    }
}

