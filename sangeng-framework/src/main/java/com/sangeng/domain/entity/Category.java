package com.sangeng.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_category")
public class Category {
    @TableId
    private Long id;

    private String name;

    private Long pid;

    private String description;

    private String status;

    private Long create_by;

    private Date create_time;

    private Long update_by;

    private Date update_time;

    private Integer del_flag;

}
