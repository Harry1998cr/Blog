package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.vo.MenuTreeVo;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<MenuTreeVo> selectMenuTreeVo();

    List<MenuTreeVo> selectMenuTreeVoByRoleId(Long id);
}
