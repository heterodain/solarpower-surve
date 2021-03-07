package com.heterodain.solarpower.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * サービスの設定
 */
@Component
@ConfigurationProperties("service")
@Data
public class ServiceConfig {
    /* Ambientの設定(3分値) */
    private Ambient ambientRealtime;
    /* Ambientの設定(日計値) */
    private Ambient ambientDaily;

    /**
     * Ambientの設定情報
     */
    @Data
    public static class Ambient {
        /* チャネルID */
        private Integer channelId;
        /* リードキー */
        private String readKey;
        /* ライトキー */
        private String writeKey;
    }
}