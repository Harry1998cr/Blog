package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddLinkDto;
import com.sangeng.domain.dto.UpdateLinkDto;
import com.sangeng.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @PreAuthorize("@ps.hasPermission('content:link:list')")
    @GetMapping("/list")
    public ResponseResult queryLinkPage(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.queryLinkPage(pageNum,pageSize,name,status);
    }

    @PreAuthorize("@ps.hasPermission('content:link:add')")
    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }

    @PreAuthorize("@ps.hasPermission('content:link:query')")
    @GetMapping("/{id}")
    public ResponseResult queryLinkById(@PathVariable("id") Long id){
        return linkService.queryLinkById(id);
    }

    @PreAuthorize("@ps.hasPermission('content:link:edit')")
    @PutMapping
    public ResponseResult updateLink(@RequestBody UpdateLinkDto updateLinkDto){
        return linkService.updateLink(updateLinkDto);
    }

    @PreAuthorize("@ps.hasPermission('content:link:remove')")
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") Long id){
        return linkService.deleteLink(id);
    }
}
