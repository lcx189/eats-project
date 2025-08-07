package com.eats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //アノテーション方式のトランザクション管理を有効にする
@Slf4j
@EnableCaching//キャッシュアノテーション機能を有効にする
@EnableScheduling //タスクスケジューリングを有効にす�?
public class EatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EatsApplication.class, args);
        log.info("server started");
    }
}
