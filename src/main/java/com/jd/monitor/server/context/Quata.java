package com.jd.monitor.server.context;

import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-6-25
 * Time: 下午6:53
 */
public enum Quata {
    CPU("com.jd.monitor.server.context.support.CPU"),
    CPUBuider("com.jd.monitor.server.context.support.group.CPUBuider"),
    MySWAP("com.jd.monitor.server.context.support.MySwap"),
    MEMORY("com.jd.monitor.server.context.support.Memory"),;
    private String value = "";

    Quata(String value) {
        this.value = value;
    }


    public String getValue() {
        return this.value;
    }
}
