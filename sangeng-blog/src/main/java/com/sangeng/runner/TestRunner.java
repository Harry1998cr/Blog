package com.sangeng.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sangeng.domain.entity.Article;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        System.out.println("程序初始化");
    }
}
