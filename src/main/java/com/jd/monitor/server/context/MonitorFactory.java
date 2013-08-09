package com.jd.monitor.server.context;

import java.util.List;

/**
 * User: xiangkui
 * Date: 13-6-21
 * Time: 下午6:11
 */
public interface MonitorFactory {
    public MonitorQuota getQuota(Quata quata);

    public List<MonitorQuota> getQuotaGroup(Quata quata);

    //扩展点的方式装配监控点
    public MonitorQuota getExtentionQuota(String name);

}
