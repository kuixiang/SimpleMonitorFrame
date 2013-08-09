package com.jd.monitor.server.context;

import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-6-21
 * Time: 下午6:13
 */
public interface MonitorQuota {
    public String getName();

    public void setName(String name);

    public Map<String, Number> getQuataTable();
}
