package com.knits.enterprise.controller.common;

import com.knits.enterprise.dto.search.ContractSearchDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.model.common.BinaryData;
import com.knits.enterprise.model.company.Contract;
import com.knits.enterprise.service.common.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileController {
    private final FileStorageService fileStorageService;
    private static final List<String> SUPPORTED_CONTENT_TYPE = Arrays.asList(
            "application/pdf",
            "application/zip",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    @PostMapping(value = "/employees/contract")
    @Operation(summary = "Adds new contract to DB, supports .pdf, .zip, .docx files.")
    public ResponseEntity<String> uploadEmploymentContract(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("employeeId") Long employeeId)
            throws HttpMediaTypeNotSupportedException, IOException {
        if (!isSupportedContentType(file.getContentType())) {
            throw new HttpMediaTypeNotSupportedException
                    ("Unsupported content type: " + file.getContentType() + ". Use .pdf, .zip or .docx");
        }
        Contract uploadedContract = fileStorageService.uploadEmploymentContract(file, employeeId);

        return ResponseEntity
                .ok()
                .body("Successfully added new contract with id " + uploadedContract.getId() +
                        " and deactivated previous contracts with employee with id " + employeeId);
    }

    @GetMapping(value = "employees/contract/active")
    @Operation(summary = "Employee can download their current Employment Contract")
    public ResponseEntity<byte[]> downloadEmploymentContract(@RequestParam Long employeeId) {
        BinaryData binaryData = fileStorageService.downloadEmploymentContract(employeeId);

        if (binaryData != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(binaryData.getTitle())
                    .build());

            return new ResponseEntity<>(binaryData.getBytes(), headers, HttpStatus.OK);
        } else {
            throw new UserException("Contract for employee " + employeeId + " was not found");
        }
    }

    @GetMapping(value = "/employees/contracts")
    @Operation(summary = "HR officer can search for Employment Contracts with filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "No contracts found based on search filters"),
    })
    public ResponseEntity<byte[]> findEmploymentContracts(ContractSearchDto contractSearchDto) throws IOException {
        byte[] bytes = fileStorageService.filterContracts(contractSearchDto);
        if (bytes.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("contracts.zip")
                .build());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType != null && SUPPORTED_CONTENT_TYPE.contains(contentType);
    }


}
