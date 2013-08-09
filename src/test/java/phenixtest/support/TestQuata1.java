package phenixtest.support;


import com.jd.monitor.server.context.support.AbstractMonitorQuota;

import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-6-21
 * Time: 下午6:13
 */
public class TestQuata1 extends AbstractMonitorQuota {
    public TestQuata1() {
    }

    private static int i = 0;

    @Override
    public Map<String, Number> getQuataTable() {
        //单位M
        quataMap.put("day_string", i % 7);
        i++;
        return super.getQuataTable();
    }


}

