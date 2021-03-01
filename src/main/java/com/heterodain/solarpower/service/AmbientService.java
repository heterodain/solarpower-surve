package com.heterodain.solarpower.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heterodain.solarpower.config.ApplicationSetting.Ambient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.ToString;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

/**
 * Ambientサービス
 */
@Service
@Slf4j
public class AmbientService {
    private static final ZoneId UTC = ZoneId.of("UTC");

    @Autowired
    private ObjectMapper om;

    // 前回送信した時刻
    private Long beforeSend;

    /**
     * チャネルにデータ送信
     * 
     * @param info  チャネル情報
     * @param ts    タイムスタンプ
     * @param datas 送信データ(最大8個)
     * @throws IOException
     * @throws InterruptedException
     */
    public synchronized void send(Ambient info, ZonedDateTime ts, Double... datas)
            throws IOException, InterruptedException {
        // 送信間隔が5秒以上になるように調整
        if (beforeSend != null) {
            var diff = System.currentTimeMillis() - beforeSend;
            if (diff < 5000) {
                Thread.sleep(5000 - diff);
            }
        }

        // 送信するJSONを構築
        var rootNode = om.createObjectNode();
        rootNode.put("writeKey", info.getWriteKey());

        var dataArrayNode = om.createArrayNode();
        var dataNode = om.createObjectNode();
        var utcTs = ts.withZoneSameInstant(UTC).toLocalDateTime();
        dataNode.put("created", utcTs.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        for (int i = 1; i <= datas.length; i++) {
            if (datas[i - 1] != null) {
                dataNode.put("d" + i, datas[i - 1]);
            }
        }
        dataArrayNode.add(dataNode);
        rootNode.set("data", dataArrayNode);

        var jsonString = om.writeValueAsString(rootNode);

        // HTTP POST
        var url = "http://54.65.206.59/api/v2/channels/" + info.getChannelId() + "/dataarray";
        log.debug("request > " + url);
        log.debug("body > " + jsonString);

        var conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setDoOutput(true);
        try (var os = conn.getOutputStream()) {
            os.write(jsonString.getBytes(StandardCharsets.UTF_8));
        }
        var resCode = conn.getResponseCode();
        if (resCode != 200) {
            throw new IOException("Ambient Response Code " + resCode);
        }

        beforeSend = System.currentTimeMillis();
    }

    /**
     * 1日分のデータ取得
     * 
     * @param info チャネル情報
     * @param date 日付
     * @return 1日分のデータ
     * @throws IOException
     */
    public List<ReadData> read(Ambient info, LocalDate date) throws IOException {
        // HTTP GET
        var url = "http://54.65.206.59/api/v2/channels/" + info.getChannelId() + "/data?readKey=" + info.getReadKey()
                + "&date=" + date.format(DateTimeFormatter.ISO_DATE);
        log.debug("request > " + url);

        var conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(20000);
        var resCode = conn.getResponseCode();
        if (resCode != 200) {
            throw new IOException("Ambient Response Code " + resCode);
        }

        try (var is = conn.getInputStream()) {
            return om.readValue(is, new TypeReference<List<ReadData>>() {
            });
        }
    }

    /**
     * 指定期間のデータ取得
     * 
     * @param info  チャネル情報
     * @param start 開始日時
     * @param end   終了日時
     * @return 指定期間のデータ
     * @throws IOException
     */
    public List<ReadData> read(Ambient info, ZonedDateTime start, ZonedDateTime end) throws IOException {
        var utcStart = start.withZoneSameInstant(UTC).toLocalDateTime();
        var utcEnd = end.withZoneSameInstant(UTC).toLocalDateTime();

        // HTTP GET
        var url = "http://54.65.206.59/api/v2/channels/" + info.getChannelId() + "/data?readKey=" + info.getReadKey();
        url += "&start=" + URLEncoder.encode(utcStart.format(DateTimeFormatter.ISO_DATE_TIME), "UTF-8");
        url += "&end=" + URLEncoder.encode(utcEnd.format(DateTimeFormatter.ISO_DATE_TIME), "UTF-8");
        log.debug("request > " + url);

        var conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(20000);
        var resCode = conn.getResponseCode();
        if (resCode != 200) {
            throw new IOException("Ambient Response Code " + resCode);
        }

        try (var is = conn.getInputStream()) {
            return om.readValue(is, new TypeReference<List<ReadData>>() {
            });
        }
    }

    /**
     * 直近n個のデータ取得
     * 
     * @param info チャネル情報
     * @param n    取得個数
     * @return 直近n個のデータ
     * @throws IOException
     */
    public List<ReadData> read(Ambient info, int n) throws IOException {
        // HTTP GET
        var url = "http://54.65.206.59/api/v2/channels/" + info.getChannelId() + "/data?readKey=" + info.getReadKey()
                + "&n=" + n;
        log.debug("request > " + url);

        var conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(20000);
        var resCode = conn.getResponseCode();
        if (resCode != 200) {
            throw new IOException("Ambient Response Code " + resCode);
        }

        try (var is = conn.getInputStream()) {
            return om.readValue(is, new TypeReference<List<ReadData>>() {
            });
        }
    }

    @Getter
    @ToString
    public static class ReadData {
        private String created;
        private Double d1;
        private Double d2;
        private Double d3;
        private Double d4;
        private Double d5;
        private Double d6;
        private Double d7;
        private Double d8;
    }
}