package com.jd.monitor.server;

public class MonitorStartup {
    public static void main(String[] args) {
        MonitorMain tmpMonitorMain = new MonitorMain();
        tmpMonitorMain.init(args[0]);
    }
}
