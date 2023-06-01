package com.sangeng.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章表(SgArticle)表实体类
 *
 * @author wcr
 * @since 2023.3.28 18:55
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_article")
@Accessors(chain = true)
public class Article implements Serializable{
    @TableId
    private Long id;
    //标题
    private String title;
    //文章内容
    private String content;
    //文章类型:1 文章 2草稿
    private String type;
    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;

    @TableField(exist = false)
    private String categoryName;
    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;
    //评论数
    private Integer commentCount;
    //访问量
    private Long viewCount;
    //是否允许评论 1是，0否
    private String isComment;
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;

    public Article(Long id, long viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }

    public Long getId(){ return this.id; }

    public void setId(Long id){ this.id = id; }

    public String getTitle(){ return this.title; }

    public void setTitle(String title){ this.title = title;}

    public String getContent(){ return this.content; }

    public void setContent(String content){ this.content = content; }

    public String getType(){ return this.type; }

    public void setType(String type){ this.type = type; }

    public String getSummary(){ return this.summary; }

    public void setSummary(String summary){ this.summary = summary; }

    public Long getCategoryId(){ return this.categoryId; }

    public void setCategoryId(Long categoryId){ this.categoryId = categoryId; }

    public String getThumbnail(){ return this.thumbnail; }

    public void setThumbnail(String thumbnail){ this.thumbnail = thumbnail; }

    public String getIsTop(){ return this.isTop; }

    public void setIsTop(String isTop){ this.isTop = isTop; }

    public String getStatus() { return this.status; }

    public void setStatus(String status){ this.status = status; }

    public Integer getCommentCount(){ return this.commentCount; }

    public void setCommentCount(Integer commentCount){ this.commentCount = commentCount; }

    public Long getViewCount() { return this.viewCount; }

    public void setViewCount(Long viewCount){ this.viewCount = viewCount; }

    public String getIsComment(){ return this.isComment; }

    public void setIsComment(String isComment){ this.isComment = isComment; }

    public Long getCreateBy(){ return this.createBy; }

    public void setCreateBy(Long createBy){ this.createBy = createBy; }

    public Date getCreateTime(){ return this.createTime; }

    public void setCreateTime(Date createTime){ this.createTime = createTime; }

    public Long getUpdateBy(){ return this.updateBy; }

    public void setUpdateBy(Long updateBy){ this.updateBy = updateBy; }

    public Date getUpdateTime(){ return this.updateTime; }

    public void setUpdateTime(Date updateTime){ this.updateTime = updateTime; }

    public Integer getDelFlag(){ return this.delFlag; }

    public void setDelFlag(Integer delFlag){ this.delFlag = delFlag; }

}
