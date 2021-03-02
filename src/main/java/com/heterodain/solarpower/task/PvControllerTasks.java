package com.heterodain.solarpower.task;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.heterodain.solarpower.config.ApplicationSetting;
import com.heterodain.solarpower.model.PvData;
import com.heterodain.solarpower.model.PvDataSummary;
import com.heterodain.solarpower.service.AmbientService;
import com.heterodain.solarpower.service.PvService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.var;
import lombok.extern.slf4j.Slf4j;

/**
 * PVコントローラー関連の非同期タスク
 */
@Component
@Slf4j
public class PvControllerTasks {
    @Autowired
    private ApplicationSetting settings;
    @Autowired
    private PvService pvService;
    @Autowired
    private AmbientService ambientService;

    private SerialConnection conn;

    private PvDataSummary instant = new PvDataSummary();
    private PvDataSummary hourly = new PvDataSummary();

    /**
     * 初期化
     */
    @PostConstruct
    public void init() throws IOException {
        // PVコントローラーに接続
        var pvcSetting = settings.getDevice().getPvController();
        log.info("チャージコントローラーに接続します。unitId={}", pvcSetting.getUnitId());

        var serialParam = new SerialParameters();
        serialParam.setPortName(pvcSetting.getComPort());
        serialParam.setBaudRate(115200);
        serialParam.setDatabits(8);
        serialParam.setParity("None");
        serialParam.setStopbits(1);
        serialParam.setEncoding("rtu");
        serialParam.setEcho(false);
        conn = new SerialConnection(serialParam);
        conn.open();
    }

    /**
     * PVコントローラーからデータ取得
     */
    @Scheduled(initialDelay = 3 * 1000, fixedDelay = 3 * 1000)
    public void realtime() {
        try {
            var data = pvService.readCurrent(settings.getDevice().getPvController(), conn);
            log.debug("{}", data);

            synchronized (instant) {
                instant.add(data.getPvVolt(), data.getPvPower(), data.getBattVolt(), data.getBattPower(),
                        data.getLoadPower());
            }
            synchronized (hourly) {
                hourly.add(data.getPvVolt(), data.getPvPower(), data.getBattVolt(), data.getBattPower(),
                        data.getLoadPower());
            }
        } catch (Exception e) {
            log.warn("チャージコントローラーへのアクセスに失敗しました。", e);
        }
    }

    /**
     * 3分毎にAmbientにデータ送信
     */
    @Scheduled(cron = "0 */3 * * * *")
    public void summary() {
        try {
            PvData average;
            synchronized (instant) {
                average = instant.average();
                instant.clear();
            }
            log.debug("Ambientに3分値を送信します。data={}", average);

            ambientService.send(settings.getService().getAmbientRealtime(), ZonedDateTime.now(), average.getPvVolt(),
                    average.getPvPower(), average.getBattVolt(), average.getBattPower(), average.getLoadPower());
        } catch (Exception e) {
            log.warn("Ambientへのデータ送信に失敗しました。", e);
        }
    }

    /**
     * 1時間毎にAmbientにデータ送信
     */
    @Scheduled(cron = "0 0 * * * *")
    public void daily() {
        try {
            PvData average;
            synchronized (hourly) {
                average = hourly.average();
                hourly.clear();
            }
            log.debug("Ambientに時間値を送信します。data={}", average);

            ambientService.send(settings.getService().getAmbientDaily(),
                    ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS), average.getPvPower(), average.getBattPower(),
                    average.getLoadPower());
        } catch (Exception e) {
            log.warn("Ambientへのデータ送信に失敗しました。", e);
        }
    }

    /**
     * 終了処理
     */
    @PreDestroy
    public void destroy() {
        if (conn != null) {
            log.debug("チャージコントローラーを切断します。unitId={}", settings.getDevice().getPvController().getUnitId());
            conn.close();
        }
    }
}
