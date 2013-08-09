package com.jd.monitor.server.context.support;


import com.jd.monitor.server.context.MonitorQuota;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: xiangkui
 * Date: 13-6-21
 * Time: 下午6:13
 */
public abstract class AbstractMonitorQuota implements MonitorQuota {
    protected static Logger logger = LoggerFactory.getLogger(AbstractMonitorQuota.class);
    protected String name = "";
    protected Map<String, Number> quataMap = new ConcurrentHashMap<String, Number>();

    public AbstractMonitorQuota() {
        name = this.getClass().getSimpleName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Map<String, Number> getQuataTable() {
        return quataMap;
    }


}
