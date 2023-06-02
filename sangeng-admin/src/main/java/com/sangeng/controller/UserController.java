package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddUserDto;
import com.sangeng.domain.dto.UpdateUserDto;
import com.sangeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("@ps.hasPermission('system:user:list')")
    @GetMapping("/list")
    public ResponseResult listAllUser(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.listAllUser(pageNum,pageSize,userName,phonenumber,status);
    }

    @PreAuthorize("@ps.hasPermission('system:user:add')")
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @PreAuthorize("@ps.hasPermission('system:user:remove')")
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public ResponseResult queryUserEchoById(@PathVariable("id") Long id){
        return userService.queryUserEchoById(id);
    }

    @PreAuthorize("@ps.hasPermission('system:user:edit')")
    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
}
