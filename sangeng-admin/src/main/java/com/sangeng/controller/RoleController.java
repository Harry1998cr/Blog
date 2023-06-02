package com.sangeng.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddRoleDto;
import com.sangeng.domain.dto.RoleStatusDto;
import com.sangeng.domain.dto.UpdateRoleDto;
import com.sangeng.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("@ps.hasPermission('system:role:list')")
    @GetMapping("/list")
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status){
        return roleService.listRole(pageNum,pageSize,roleName,status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto){
        return roleService.changeStatus(roleStatusDto);
    }
    @PreAuthorize("@ps.hasPermission('system:role:add')")
    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult listRoleById(@PathVariable("id") Long id){
        return roleService.listRoleById(id);
    }

    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    @PutMapping
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }

    @PreAuthorize("@ps.hasPermission('system:role:remove')")
    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") Long id){
        return roleService.deleteRole(id);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
