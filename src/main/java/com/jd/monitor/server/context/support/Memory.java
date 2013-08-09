package com.jd.monitor.server.context.support;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-6-21
 * Time: 下午6:13
 */
public class Memory extends AbstractMonitorQuota {
    Sigar sigar = new Sigar();
    Mem mem;

    public Memory() {
        try {
            mem = sigar.getMem();
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Number> getQuataTable() {
        quataMap.put("memTotal", mem.getTotal() / 1024L / 1024L);
        quataMap.put("memUsed", mem.getUsed() / 1024L / 1024L);
        quataMap.put("memFree", mem.getFree() / 1024L / 1024L);
        return super.getQuataTable();
    }


}

