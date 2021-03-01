package com.heterodain.solarpower.model;

import lombok.Data;

/**
 * 太陽光発電の瞬時値
 */
@Data
public class PvData {
    public double pvVolt = 0.0D;
    public double pvPower = 0.0D;
    public double battVolt = 0.0D;
    public double battPower = 0.0D;
    public double loadPower = 0.0D;
}
