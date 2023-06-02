package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Menu;
import com.sangeng.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @PreAuthorize("@ps.hasPermission('system:menu:list')")
    @GetMapping("/list")
    public ResponseResult listMenu(String status,String menuName){
        return menuService.listMenu(status,menuName);
    }

    @PreAuthorize("@ps.hasPermission('system:menu:add')")
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("/{id}")
    public ResponseResult queryMenuById(@PathVariable("id") Long id){
        return menuService.queryMenuById(id);
    }

    @PreAuthorize("@ps.hasPermission('system:menu:edit')")
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @PreAuthorize("@ps.hasPermission('system:menu:remove')")
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long menuId){
        return menuService.deleteMenu(menuId);
    }

    @GetMapping("/treeselect")
    public ResponseResult getTreeSelect(){
        return menuService.getTreeSelect();
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getTreeSelectById(@PathVariable("id") Long id){
        return menuService.getTreeSelectById(id);
    }
}
