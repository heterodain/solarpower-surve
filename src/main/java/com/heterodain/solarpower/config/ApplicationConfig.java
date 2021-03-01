package com.heterodain.solarpower.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import lombok.var;

/**
 * コンポーネント設定
 */
@Configuration
public class ApplicationConfig {

    /**
     * JSONパーサーをDIコンテナに登録
     * 
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    /**
     * タスクスケジューラーの設定
     * 
     * @return タスクスケジューラー
     */
    @Bean
    public ThreadPoolTaskScheduler getTaskScheduler() {
        var taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3); // 3スレッド同時実行
        return taskScheduler;
    }
}
