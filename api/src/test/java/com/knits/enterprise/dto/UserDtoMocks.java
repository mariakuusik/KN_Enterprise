package com.knits.enterprise.dto;

import com.knits.enterprise.dto.security.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDtoMocks {
    public static UserDto testUserDto(Long id) {
        return UserDto.builder()
                .id(id)
                .firstName("User test firstname")
                .lastName("User test lastname")
                .login("User test username")
                .password("User test password")
                .email("testemail@testemail.com")
                .build();
    }

    public static UserDto createTestUserDto(Long id) {
        return testUserDto(id);
    }

}
