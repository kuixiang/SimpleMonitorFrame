package com.jd.monitor.server.context.support;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-6-21
 * Time: 下午6:13
 */
public class MySwap extends AbstractMonitorQuota {
    static {
        String[] libNames = new String[]{
                "libsigar-amd64-linux",
                "libsigar-x86-linux",
                "sigar-amd64-winnt",
                "sigar-x86-winnt"
        };
        for (String lib : libNames) {
            try {
                System.loadLibrary(lib);
                logger.info("[servermonitor] " + lib + " load ok ");
                break;//装载一个成功，即跳出
            } catch (SecurityException e) {
                logger.warn("[servermonitor] " + lib + " load fail ,main cause link error" + e);
            } catch (UnsatisfiedLinkError e) {
                //logger.warn("[servermonitor] " + lib + " load fail ,main cause link error" + e);
            }
        }
    }

    Sigar sigar;
    Swap swap;

    public MySwap() {
        try {
            String[] libNames = new String[]{
                    "libsigar-amd64-linux",
                    "libsigar-x86-linux",
                    "sigar-amd64-winnt",
                    "sigar-x86-winnt"
            };
            for (String lib : libNames) {
                try {
                    System.loadLibrary(lib);
                    logger.info("[servermonitor] " + lib + " load ok ");
                    break;//装载一个成功，即跳出
                } catch (SecurityException e) {
                    logger.warn("[servermonitor] " + lib + " load fail ,main cause link error" + e);
                } catch (UnsatisfiedLinkError e) {
                    //logger.warn("[servermonitor] " + lib + " load fail ,main cause link error" + e);
                }
            }
            sigar = new Sigar();
            swap = sigar.getSwap();
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Number> getQuataTable() {
        //单位M
        quataMap.put("swapTotal", swap.getTotal() / 1024L / 1024L);
        quataMap.put("swapUsed", swap.getUsed() / 1024L / 1024L);
        quataMap.put("swapFree", swap.getFree() / 1024L / 1024L);
        return super.getQuataTable();
    }


}

