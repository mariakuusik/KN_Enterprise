package com.knits.enterprise.service.company;

import com.knits.enterprise.dto.company.JobTitleDto;
import com.knits.enterprise.mapper.company.JobTitleMapper;
import com.knits.enterprise.model.company.JobTitle;
import com.knits.enterprise.model.security.User;
import com.knits.enterprise.repository.company.JobTitleRepository;
import com.knits.enterprise.service.security.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class JobTitleService {
}
