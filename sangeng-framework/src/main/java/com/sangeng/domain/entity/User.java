package com.sangeng.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User {
    // 主键@Tableid
    private Long id;
    // 用户名
    private String userName;
    // 昵称
    private String nickName;
    // 密码
    private String password;
    // 用户类型：0代表普通用户，1代表管理员
    private String type;
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
    // 创建人的用户id
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    // 更新人的用户id
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    // 删除标志 0未删除 1已删除
    private Integer delFlag;

}
