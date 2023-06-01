package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constant.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.entity.RoleMenu;
import com.sangeng.domain.vo.MenuTreeVo;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.domain.vo.RoleMenuVo;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.service.MenuService;
import com.sangeng.service.RoleMenuService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if(id.equals(1L)){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回其所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //否则 当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        List<Menu> menuTree = builderMenuTree(menus);

        return menuTree;
    }

    @Override
    public ResponseResult listMenu(String status, String menuName) {
        //如果状态和菜单名存在，进行对应条件的查询
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menuList = list(queryWrapper);

        return ResponseResult.okResult(menuList);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        //保存当前菜单
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryMenuById(Long id) {
        Menu menu = getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getId, id));
        MenuVo menuVo = BeanCopyUtils.copyBean(menu,MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        //如果把父菜单设置为当前菜单，提示修改失败。
        if(menu.getParentId().equals(menu.getId())){
            return ResponseResult.errorResult(500,"修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        //更新菜单
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        //如果菜单有子菜单，则不允许删除
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,id);
        Integer count = getBaseMapper().selectCount(queryWrapper);
        if(count > 0){
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTreeSelect() {
        MenuMapper menuMapper = getBaseMapper();
        List<MenuTreeVo> menuTreeVos = menuMapper.selectMenuTreeVo();
        return ResponseResult.okResult(builderMenuTreeVo(0L,menuTreeVos));
    }

    @Override
    public ResponseResult getTreeSelectById(Long id) {
        MenuMapper menuMapper = getBaseMapper();
        List<Long> checkedKeys;
        if(id.equals(1L)){
            //如果角色是超级管理员
            //则菜单权限应该是所有权限(状态为正常的菜单)
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = menuMapper.selectList(queryWrapper);
            checkedKeys = menus.stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList());
        } else {
            //获取角色对应菜单权限id list
            List<MenuTreeVo> menuTreeVos = menuMapper.selectMenuTreeVoByRoleId(id);
            checkedKeys = menuTreeVos.stream()
                    .map(MenuTreeVo::getId)
                    .collect(Collectors.toList());
        }
        //获取全部菜单树
        List<MenuTreeVo> wholeMenuTree = menuMapper.selectMenuTreeVo();
        wholeMenuTree = builderMenuTreeVo(0L,wholeMenuTree);
        //封装响应返回
        return ResponseResult.okResult(new RoleMenuVo(wholeMenuTree,checkedKeys));
    }

    private List<MenuTreeVo> builderMenuTreeVo(Long parent_id, List<MenuTreeVo> menuTreeVos) {
        return menuTreeVos.stream()
                .filter(m->m.getParentId().equals(parent_id))
                .peek(m->m.setChildren(builderMenuTreeVo(m.getId(),menuTreeVos)))
                .collect(Collectors.toList());
    }


    private List<Menu> builderMenuTree(List<Menu> menus) {
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> children = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return children;
    }
}
