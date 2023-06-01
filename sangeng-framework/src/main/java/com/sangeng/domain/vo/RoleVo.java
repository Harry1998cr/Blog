package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo {
    private Long id;

    private String roleKey;

    private String roleName;

    private Integer roleSort;

    private String status;
}
