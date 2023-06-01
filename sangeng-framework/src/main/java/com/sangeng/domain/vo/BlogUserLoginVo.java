package com.sangeng.domain.vo;

import com.sangeng.domain.entity.UserInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogUserLoginVo {
    private String token;

    private UserInfoVo userInfo;
}
