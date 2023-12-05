package com.knits.enterprise.controller.common;

import com.knits.enterprise.model.company.Contract;
import com.knits.enterprise.service.common.FileStorageService;
import com.knits.enterprise.service.company.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotSupportedException;
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

    private boolean isSupportedContentType(String contentType) {
        return contentType != null && SUPPORTED_CONTENT_TYPE.contains(contentType);
    }

    @PostMapping(value = "/employee/contract")
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
                        " and deactivated previous contracts with employee");
    }

}
