package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    // 主键@Tableid
    private Long id;
    // 用户名
    private String userName;
    // 昵称
    private String nickName;
    // 账号状态：0代表正常，1代表停用
    private String status;
    // 邮箱
    private String email;
    // 电话
    private String phonenumber;
    // 性别 0男 1女 2未知
    private String sex;
    // 头像
    private String avatar;
    // 创建时间
    private Date createTime;
    // 更新人的用户id
    private Long updateBy;
    // 更新时间
    private Date updateTime;
}
