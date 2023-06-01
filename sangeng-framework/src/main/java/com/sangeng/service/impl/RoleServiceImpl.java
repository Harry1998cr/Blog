package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddRoleDto;
import com.sangeng.domain.dto.RoleStatusDto;
import com.sangeng.domain.dto.UpdateRoleDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.RoleMenu;
import com.sangeng.domain.entity.UserRole;
import com.sangeng.domain.vo.AllRolesVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.RoleVo;
import com.sangeng.mapper.RoleMapper;
import com.sangeng.service.RoleMenuService;
import com.sangeng.service.RoleService;
import com.sangeng.service.UserRoleService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {
    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private UserRoleService userRoleService;

    public RoleServiceImpl() {
    }

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id==1){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }

        //否则查询用户所具有的角色信息

        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        queryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Role> roles = page.getRecords();
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roles, RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos,page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(RoleStatusDto roleStatusDto) {
        Long roleId = roleStatusDto.getRoleId();
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId,roleId);
        updateWrapper.set(Role::getStatus,roleStatusDto.getStatus());
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listRoleById(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        //新建一个role
        Role role = BeanCopyUtils.copyBean(addRoleDto,Role.class);
        save(role);
        //在role-menu表中加入role对应的menu id
        List<RoleMenu> roleMenuList = addRoleDto.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        //更新role
        Role role = BeanCopyUtils.copyBean(updateRoleDto,Role.class);
        updateById(role);
        //更新role-menu
        //删除原先的role-menu关联
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRole_id,role.getId());
        roleMenuService.getBaseMapper().delete(queryWrapper);
        //加入新的role-menu关联
        List<Long> menuIds = updateRoleDto.getMenuIds();
        List<RoleMenu> roleMenus = menuIds.stream()
                .map(m -> new RoleMenu(role.getId(), m))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        //不允许删除管理员角色
        if(id.equals(1L)){
            return ResponseResult.errorResult(500,"不能删除管理员!");
        }
        //如果角色已经被分配给用户，不能删除
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRole_id,id);
        Integer count = userRoleService.getBaseMapper().selectCount(wrapper);
        if(count > 0){
            return ResponseResult.errorResult(501,"当前角色已经分配给用户!");
        }
        //根据id删除角色
        getBaseMapper().deleteById(id);
        //删除角色对应的菜单权限
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRole_id,id);
        roleMenuService.getBaseMapper().delete(queryWrapper);
        //返回响应
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        List<Role> roles = list(new LambdaQueryWrapper<>());
        List<AllRolesVo> allRolesVos = BeanCopyUtils.copyBeanList(roles, AllRolesVo.class);
        return ResponseResult.okResult(allRolesVos);
    }

    @Override
    public List<Long> selectRoleIdsByUserId(Long id) {
        return getBaseMapper().selectRoleIdsByUserId(id);
    }
}
