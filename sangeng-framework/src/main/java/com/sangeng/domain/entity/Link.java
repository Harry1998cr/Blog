package com.sangeng.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_link")
public class Link {
    @TableId
    private Long id;

    private String logo;

    private String name;

    private String address;

    private String description;

    private String status;

    private Long create_by;

    private Date create_time;

    private Long update_by;

    private Date update_time;

    private Integer del_flag;
}
