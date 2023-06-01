package com.sangeng.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sangeng.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuVo {
    private Long id;

    private String icon;

    private String menuName;

    private String menuType;

    private Integer orderNum;

    private Long parentId;

    private String path;

    private String remark;

    private String status;

    private String visible;
}
