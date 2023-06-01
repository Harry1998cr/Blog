package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private Long id;

    private String email;

    private String nickName;

    private String sex;

    private String status;

    private String userName;

    private List<Long> roleIds;
}
