package com.jd.monitor.server;


import com.jd.monitor.server.filemetrics.JFileWatcherEvent;
import com.jd.monitor.server.filemetrics.JFileWatcherService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.text.SimpleDateFormat;

public class MonitorFileMetics {
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.err.println("Usage: java JFileWatcher <file>|<folder> [<pattern> <pattern> ...]");
                System.exit(1);
            }
            String[] patterns = null;
            if (args.length > 1) {
                patterns = new String[args.length - 1];
                System.arraycopy(args, 1, patterns, 0, args.length - 1);
            }
            ApplicationContext context = new ClassPathXmlApplicationContext("monitor.xml");
            JFileWatcherService fservice = (JFileWatcherService) context.getBean("fservice");
            fservice.watch(new File(args[0]), patterns);
            fservice.start();
            SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
            while (true) {
                JFileWatcherEvent e = fservice.next();
                System.out.println(
                        fmt.format(e.getFile().lastModified())
                                + " : "
                                + e.getType()
                                + " : "
                                + e.getFile().getAbsolutePath()
                                + " : "
                                + e.getFile().getTotalSpace()/1024L + "M"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}