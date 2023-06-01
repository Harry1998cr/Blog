package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddUserDto;
import com.sangeng.domain.dto.UpdateUserDto;
import com.sangeng.domain.entity.*;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.UserEchoVo;
import com.sangeng.domain.vo.UserVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.mapper.UserMapper;
import com.sangeng.service.RoleService;
import com.sangeng.service.UserRoleService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户Id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
 }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断, 如果为null或者""
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //对数据进行重复判断，查询是否已经存在
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(passwordExist(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密处理
        String password = passwordEncoder.encode(user.getPassword());
        //存入数据库
        user.setPassword(password);
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<User> userList = page.getRecords();
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(userList, UserVo.class);
        return ResponseResult.okResult(new PageVo(userVos,page.getTotal()));
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        //用户名不能为空
        if(!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        //用户名已经存在
        if(userNameExist(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //手机号已经存在
        if(phoneNumberExist(addUserDto.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        //邮箱已经存在
        if(emailExist(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //密码加密
        String encodedPassWord = passwordEncoder.encode(addUserDto.getPassword());
        //新增用户
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        user.setPassword(encodedPassWord);
        save(user);
        //关联用户与角色
        List<Long> roleIds = addUserDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream()
                .map(ri -> new UserRole(user.getId(), ri))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //不能删除当前用户
        if(userId.equals(id)){
            return ResponseResult.errorResult(500,"不能删除当前用户!");
        }
        //根据id删除用户
        getBaseMapper().deleteById(id);
        //删除用户和角色的关联
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUser_id,id);
        userRoleService.getBaseMapper().delete(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryUserEchoById(Long id) {
        //根据用户id查询关联的角色Id列表
        List<Long> roleIds = roleService.selectRoleIdsByUserId(id);
        //获取所有角色列表
        List<Role> roles = roleService.list(new LambdaQueryWrapper<>());
        //查询用户信息
        User user = getBaseMapper().selectById(id);
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
        return ResponseResult.okResult(new UserEchoVo(roleIds,roles,userVo));
    }

    @Override
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        //更新用户
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        updateById(user);
        //更新用户与关联角色信息
        //先删除原有的关联
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUser_id,user.getId());
        userRoleService.getBaseMapper().delete(queryWrapper);
        //再保存新的关联
        List<UserRole> userRoles = updateUserDto.getRoleIds().stream()
                .map(ri -> new UserRole(user.getId(), ri))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    private boolean phoneNumberExist(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber,phonenumber);

        return count(queryWrapper)>0;
    }

    private boolean passwordExist(String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPassword, password);

        return count(queryWrapper)>0;
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);

        return count(queryWrapper)>0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);

        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);

        return count(queryWrapper)>0;
    }
}
