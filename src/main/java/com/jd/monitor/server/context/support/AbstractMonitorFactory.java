package com.jd.monitor.server.context.support;

import com.jd.monitor.server.context.MonitorFactory;
import com.jd.monitor.server.context.MonitorQuota;
import com.jd.monitor.server.context.Quata;
import com.jd.monitor.server.context.support.group.GroupBuider;
import com.jd.monitor.server.extention.ExtensionFactory;
import com.jd.monitor.server.extention.support.DefaultExtensionFactory;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: xiangkui
 * Date: 13-6-24
 * Time: 下午6:14
 */
public class AbstractMonitorFactory implements MonitorFactory {
    Logger logger = LoggerFactory.getLogger(AbstractMonitorFactory.class);
    private static Map<String, MonitorQuota> quotas = new ConcurrentHashMap<String, MonitorQuota>();
    private ExtensionFactory extFactory = new DefaultExtensionFactory();

    @Override
    public MonitorQuota getQuota(Quata quata) {
        MonitorQuota monitorQuota = null;
        if (!quata.getValue().equals("")) {
            try {
                //如果MAP中有，则直接从取出，不用初始化了
                if (quotas.containsKey(quata.getValue())) {
                    monitorQuota = quotas.get(quata.getValue());
                } else {
                    monitorQuota = (MonitorQuota) Class.forName(quata.getValue()).newInstance();
                    quotas.put(quata.getValue(), monitorQuota);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return monitorQuota;
    }

    @Override
    public List<MonitorQuota> getQuotaGroup(Quata quata) {
        GroupBuider buider = null;
        if (!quata.getValue().equals("")) {
            try {
                buider = (GroupBuider) ClassUtils.getClass(quata.getValue(), true).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buider.getGroupQuata();
    }

    @Override
    public MonitorQuota getExtentionQuota(String name) {
        return extFactory.getExtension(name);
    }
}
