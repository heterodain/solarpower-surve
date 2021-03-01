package com.heterodain.solarpower.model;

import lombok.Data;

/**
 * 太陽光発電の集計値
 */
@Data
public class PvDataSummary extends PvData {
    public int count = 0;

    public void add(double pvVolt, double pvPower, double battVolt, double battPower, double loadPower) {
        this.pvVolt += pvVolt;
        this.pvPower += pvPower;
        this.battVolt += battVolt;
        this.battPower += battPower;
        this.loadPower += loadPower;
        this.count++;
    }

    public PvData average() {
        if (this.count <= 1) {
            return this;
        }

        PvData data = new PvData();
        data.setPvVolt(this.pvVolt / this.count);
        data.setPvPower(this.pvPower / this.count);
        data.setBattVolt(this.battVolt / this.count);
        data.setBattPower(this.battPower / this.count);
        data.setLoadPower(this.loadPower / this.count);

        return data;
    }

    public void clear() {
        this.pvVolt = 0.0D;
        this.pvPower = 0.0D;
        this.battVolt = 0.0D;
        this.battPower = 0.0D;
        this.loadPower = 0.0D;
        this.count = 0;
    }
}
