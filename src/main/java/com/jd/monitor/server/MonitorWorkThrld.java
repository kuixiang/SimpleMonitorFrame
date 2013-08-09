package com.jd.monitor.server;

import com.jd.monitor.server.context.MonitorFactory;
import com.jd.monitor.server.context.MonitorQuota;
import com.jd.monitor.server.context.Quata;
import com.jd.monitor.server.context.support.common.CommonMonitorFactory;
import com.jd.monitor.server.jms.JMSAgent;
import com.jd.monitor.server.jms.MetaqSender;
import com.jd.monitor.server.util.MessageHelper;
import com.jd.monitor.server.util.NetUtils;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * User: xiangkui
 * Date: 13-6-26
 * Time: 上午10:43
 */
class MonitorWorkThrld extends TimerTask {
    protected JMSAgent jmsAgent;

    public MonitorWorkThrld(String directoryParm) {
        jmsAgent = new MetaqSender(directoryParm);
    }

    public void run() {
        try {
            String ip = NetUtils.getLocalAddress().getHostAddress();
            MonitorFactory monitorFactory = new CommonMonitorFactory();
            MessageHelper helper = new MessageHelper(ip);
            //swapInfo
            helper.setCurrentLcId("swapInfo");
            MonitorQuota swapQuota = monitorFactory.getQuota(Quata.MySWAP);
            for (Map.Entry<String, Number> entry : swapQuota.getQuataTable().entrySet()) {
                helper.addMe(entry.getKey(), entry.getValue().toString(), MessageHelper.MeDataType.l);
            }
            helper.setCurrentLcId("memInfo");
            MonitorQuota memQuota = monitorFactory.getQuota(Quata.MEMORY);
            for (Map.Entry<String, Number> entry : memQuota.getQuataTable().entrySet()) {
                helper.addMe(entry.getKey(), entry.getValue().toString(), MessageHelper.MeDataType.l);
            }
            helper.setCurrentLcId("cpuInfos");
            List<MonitorQuota> cpuQuotas = monitorFactory.getQuotaGroup(Quata.CPUBuider);
            for (MonitorQuota quota : cpuQuotas) {
                helper.setCurrentLcId("cpuInfos", "cpu-" + quota.getName());
                for (Map.Entry<String, Number> entry : quota.getQuataTable().entrySet()) {
                    helper.addMe(entry.getKey(), entry.getValue().toString(), MessageHelper.MeDataType.d);
                }
            }
            System.out.println(helper.getJsonResult());
            jmsAgent.sendMessage(helper.getJsonResult());
            //end modify
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
