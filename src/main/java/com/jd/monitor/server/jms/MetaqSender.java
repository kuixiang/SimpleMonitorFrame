package com.jd.monitor.server.jms;

import com.alibaba.fastjson.JSON;
import com.jd.mqtaqclient.sender.MetaQClientException;
import com.jd.mqtaqclient.sender.MetaQProducer;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Date: 13-6-19
 * Time: 下午4:09
 */
public class MetaqSender implements JMSAgent {
    private final static String Monitor_Logic_Topic = "monitor_server";
    MetaQProducer producer = MetaQProducer.getInstance();

    public MetaqSender(String directoryParm) {
        String tmpIniFilePath = directoryParm + "conf.ini";
        File tmpFile = new File(tmpIniFilePath);
        Ini tmpIni = null;
        try {
            tmpIni = new Ini(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Profile.Section tmpMonitorSec = tmpIni.get("monitor"); // get the section
        String tmpZKConnect = tmpMonitorSec.get("zkConnect");
        String tmpTopic = tmpMonitorSec.get("topic");
        String tmpOneBatchWriteCnt = tmpMonitorSec.get("oneBatchWriteCnt");
        String tmpOneBatchTimePeriod = tmpMonitorSec.get("oneBatchTimePeriod");

        String[][] tmpConf = new String[1][2];
        tmpConf[0][0] = Monitor_Logic_Topic;
        tmpConf[0][1] = tmpTopic;
        try {
            MetaQProducer.getInstance().initFromStr(
                    tmpZKConnect, tmpOneBatchWriteCnt, tmpOneBatchTimePeriod, tmpConf);
            Thread.sleep(8000);
        } catch (MetaQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Object json) {
        try {
            producer.sendOneBytesMsg(Monitor_Logic_Topic, JSON.toJSONString(json).getBytes("UTF-8"));
        } catch (MetaQClientException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
