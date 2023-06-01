package com.sangeng.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void testJob(){
        //要执行的代码
        // 把redis中的数据存到Mysql
        System.out.println("我爱你中国");
    }
}
