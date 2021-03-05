package com.heterodain.solarpower.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 設定情報
 */
@Component
@ConfigurationProperties
@Data
public class ApplicationSetting {
    /** デバイス設定 */
    private Device device;
    /** サービス設定 */
    private Service service;

    @Data
    public static class Device {
        /* チャージコントローラーの設定 */
        private PvController pvController;
    }

    @Data
    public static class Service {
        /* Ambientの設定 */
        private Ambient ambient;
    }

    /**
     * チャージコントローラーの設定情報
     */
    @Data
    public static class PvController {
        /* シリアル通信ポート名 */
        private String comPort;
        /* RS485のユニットID */
        private Integer unitId;
    }

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