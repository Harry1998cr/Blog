package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllRolesVo {
    private Long id;

    private String remark;

    private String roleKey;

    private String roleName;

    private Integer roleSort;

    private String status;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private String delFlag;
}
