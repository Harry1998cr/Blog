package com.sangeng.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    @TableId
    private Long id;

    //根评论id
    private Long rootId;

    //文章id
    private Long articleId;

    //回复目标评论id
    private Long toCommentId;

    //所回复的目标评论的id
    private Long toCommentUserId;

    private String toCommentUserName;
    //评论内容
    private String content;

    //创建用户id
    private Long createBy;

    //创建时间
    private Date createTime;

    private String username;

    private List<CommentVo> children;


}
