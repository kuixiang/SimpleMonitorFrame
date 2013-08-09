package com.jd.monitor.server.context.support;

import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-6-21
 * Time: 下午6:13
 */
public class CPU extends AbstractMonitorQuota {
    double systemRate; // 系统使用率
    double userRate; // 用户使用率
    double waitRate;  // 当前等待率
    double idleRate;  // 当前空闲率
    double niceRate;
    double combinedRate;// 总的使用率

    public double getSystemRate() {
        return systemRate;
    }

    public void setSystemRate(double systemRate) {
        this.systemRate = systemRate;
    }

    public double getUserRate() {
        return userRate;
    }

    public void setUserRate(double userRate) {
        this.userRate = userRate;
    }

    public double getWaitRate() {
        return waitRate;
    }

    public void setWaitRate(double waitRate) {
        this.waitRate = waitRate;
    }

    public double getIdleRate() {
        return idleRate;
    }

    public void setIdleRate(double idleRate) {
        this.idleRate = idleRate;
    }

    public double getNiceRate() {
        return niceRate;
    }

    public void setNiceRate(double niceRate) {
        this.niceRate = niceRate;
    }

    public double getCombinedRate() {
        return combinedRate;
    }

    public void setCombinedRate(double combinedRate) {
        this.combinedRate = combinedRate;
    }
    @Override
    public Map<String, Number> getQuataTable() {
        quataMap.put("User", 100 * getUserRate());// 用户使用率
        quataMap.put("Wait", 100 * getWaitRate());// 当前等待率
        quataMap.put("Nice", 100 * getNiceRate());//
        quataMap.put("Sys", 100 * getSystemRate());// 系统使用率
        quataMap.put("Idle", 100 * getIdleRate());// 当前空闲率
        quataMap.put("Total", 100 * getCombinedRate());// 总的使用率
        return super.getQuataTable();
    }
}

