package com.jd.monitor.server.context.support.group;

import com.jd.monitor.server.context.MonitorQuota;
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
public interface GroupBuider {
    public List<MonitorQuota> getGroupQuata();
}

