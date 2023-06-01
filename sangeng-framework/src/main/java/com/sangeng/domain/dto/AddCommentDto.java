package com.sangeng.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加评论dto")
public class AddCommentDto {
    private Long id;
    //评论类型 0代表文章评论 1代表友链评论
    @ApiModelProperty(notes = "评论类型 0代表文章评论 1代表友链评论")
    private String type;
    //根评论id
    private Long rootId;
    @ApiModelProperty(notes = "文章id")
    //文章id
    private Long articleId;
    //回复目标评论id
    private Long toCommentId;
    //所回复的目标评论的id
    private Long toCommentUserId;
    //评论内容
    private String content;
    //创建用户id
    private Long createBy;
    //创建时间
    private Date createTime;
    //由谁更新
    private Long updateBy;
    //更新时间
    private Date updateTime;
    //逻辑删除标识,0代表未删除，1代表已经删除
    private Integer delFlag;
}
