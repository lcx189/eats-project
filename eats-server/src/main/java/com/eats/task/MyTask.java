package com.eats.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * カスタムスケジュールタスククラス
 */
@Component
@Slf4j
public class MyTask {

    /**
     * スケジュールタス�?5秒ごとにトリガー
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    public void executeTask(){
        log.info("スケジュールタスクの実行を開始：{}", new Date());
    }

}
