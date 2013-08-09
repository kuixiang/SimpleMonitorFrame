package com.jd.monitor.server.context.support.group;

import com.jd.monitor.server.context.MonitorQuota;
import com.jd.monitor.server.context.support.CPU;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.util.ArrayList;
import java.util.List;

/**
 * User: xiangkui
 * Date: 13-6-21
 * Time: 下午6:13
 */
public class CPUBuider implements GroupBuider {
    Sigar sigar = new Sigar();
    CpuInfo infos[];//静态信息
    int cpuCount;

    public CPUBuider() {
        //获得静态信息
        try {
            infos = sigar.getCpuInfoList();
            cpuCount = infos.length + 1;
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<MonitorQuota> getGroupQuata() {
        CpuPerc cpuList[] = new CpuPerc[0];//动态信息
        try {
            cpuList = sigar.getCpuPercList();
        } catch (SigarException e) {
            e.printStackTrace();
        }
        List<MonitorQuota> cpus = new ArrayList<MonitorQuota>(cpuCount);
        int i = 0;
        for (CpuPerc cpuinfo : cpuList) {
            CPU theCpu = new CPU();
            theCpu.setCombinedRate(cpuinfo.getCombined());
            theCpu.setIdleRate(cpuinfo.getIdle());
            theCpu.setNiceRate(cpuinfo.getNice());
            theCpu.setSystemRate(cpuinfo.getSys());
            theCpu.setUserRate(cpuinfo.getUser());
            theCpu.setWaitRate(cpuinfo.getWait());
            theCpu.setName(String.valueOf(i++));
            cpus.add((MonitorQuota) theCpu);
        }
        return cpus;
    }
}

