package com.jd.monitor.server;

import org.ini4j.Ini;
import org.ini4j.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class MonitorMain {
    private Timer monitorTimer;
    private int timeInterval = 3;
    private String monitorview_serverGroup = "servers";
    private final Logger logger = LoggerFactory.getLogger(MonitorMain.class);

    public MonitorMain() {
    }

    public void init(String directoryParm) {
        try {
            String tmpIniFilePath = directoryParm + "conf.ini";
            File tmpFile = new File(tmpIniFilePath);
            if (!tmpFile.exists()) {
                System.out.println("File " + tmpIniFilePath + " is not exists");
                System.exit(0);
            }
            Ini tmpIni = new Ini(tmpFile);
            Profile.Section tmpMonitorSec = tmpIni.get("monitor"); // get the section
            timeInterval = Integer.parseInt(tmpMonitorSec.get("timeInterval"));
            Profile.Section tmpMonitorviewSec = tmpIni.get("monitorview"); // get the section
            monitorview_serverGroup = tmpMonitorviewSec.get("serverGroup");
            monitorTimer = new Timer("monitorTimer", true);
            monitorTimer.schedule(new MonitorWorkThrld(directoryParm), 10, 3000);
            logger.info("服务器硬件采集任务启动成功!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}