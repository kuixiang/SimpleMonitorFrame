package phenixtest.case2_2;

import com.jd.monitor.server.context.MonitorFactory;
import com.jd.monitor.server.context.MonitorQuota;
import com.jd.monitor.server.context.Quata;
import com.jd.monitor.server.context.support.common.CommonMonitorFactory;
import com.jd.monitor.server.jms.JMSAgent;
import com.jd.monitor.server.jms.MetaqSender;
import com.jd.monitor.server.util.MessageHelper;
import com.jd.monitor.server.util.NetUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-7-2
 * Time: 下午1:49
 */
public class DaynamicModifyFault {
    private JMSAgent jmsAgent;
    private static final Logger logger = LoggerFactory.getLogger(DaynamicModifyFault.class);

    @Before
    public void startUp() throws Exception {
        String confPath = "E:\\company_workspace\\phenix\\monitors\\myMonitor\\src\\main\\resources\\conf\\";
        jmsAgent = new MetaqSender(confPath);
        Thread.sleep(5000);
    }

    @Test
    public void testCase1() throws Exception {
        String ip = NetUtils.getLocalAddress().getHostAddress();
        MonitorFactory monitorFactory = new CommonMonitorFactory();
        MessageHelper helper;
        //伪造假时间戳信息
        long currTime = System.currentTimeMillis();
        //伪造过去7天的数据，模拟一天一条数据 ，其中过去的8天数据特别
        for (int i = 1; i <= 8; i++) {
            helper = new MessageHelper(ip);
            helper.setTime(DateUtils.addDays(new Date(currTime), -1 * i).getTime() - 1000 * 60 * 10 * i);//10*i min
            if (i == 8) {
                helper.setTime(DateUtils.addDays(new Date(currTime), -1 * i).getTime() + 1000 * 60 * 60 * 3);//3hours after
            }
            //test1
            helper.setCurrentLcId("testInfo1");
            MonitorQuota testQuota1 = monitorFactory.getExtentionQuota("phenixtest.support.TestQuata1");
            for (Map.Entry<String, Number> entry : testQuota1.getQuataTable().entrySet()) {
                helper.addMe(entry.getKey(), entry.getValue().toString(), MessageHelper.MeDataType.l);
            }
            //test2
            helper.setCurrentLcId("testInfo2");
            MonitorQuota testQuota2 = monitorFactory.getExtentionQuota("phenixtest.support.TestQuata2");
            for (Map.Entry<String, Number> entry : testQuota2.getQuataTable().entrySet()) {
                helper.addMe(entry.getKey(), entry.getValue().toString(), MessageHelper.MeDataType.s);
            }
            logger.info(helper.getJsonResult().toJSONString());
            jmsAgent.sendMessage(helper.getJsonResult());
        }
        System.out.println("数据发送完毕");
    }

    @Test
    public void testCase2() throws Exception {
        String ip = NetUtils.getLocalAddress().getHostAddress();
        MonitorFactory monitorFactory = new CommonMonitorFactory();
        MessageHelper helper;
        //伪造假时间戳信息
        long currTime = System.currentTimeMillis();
        //发送4条数据，模拟一天一条数据
        for (int i = 1; i <= 4; i++) {
            helper = new MessageHelper(ip);
            helper.setTime(DateUtils.addDays(new Date(currTime), -1 * i).getTime() - 1000 * 60 * 10 * i);//10*i min
            //test1
            helper.setCurrentLcId("testInfo1");
            MonitorQuota testQuota1 = monitorFactory.getExtentionQuota("phenixtest.support.TestQuata1");
            for (Map.Entry<String, Number> entry : testQuota1.getQuataTable().entrySet()) {
                helper.addMe(entry.getKey(), entry.getValue().toString(), MessageHelper.MeDataType.l);
            }
            //test2
            helper.setCurrentLcId("testInfo2");
            MonitorQuota testQuota2 = monitorFactory.getExtentionQuota("phenixtest.support.TestQuata1");
            for (Map.Entry<String, Number> entry : testQuota2.getQuataTable().entrySet()) {
                helper.addMe(entry.getKey(), entry.getValue().toString(), MessageHelper.MeDataType.s);
            }
            logger.info(helper.getJsonResult().toJSONString());
            jmsAgent.sendMessage(helper.getJsonResult());
        }
        System.out.println("数据发送完毕");
    }

    @Test
    public void testCase3() throws Exception {
        String ip = NetUtils.getLocalAddress().getHostAddress();
        MonitorFactory monitorFactory = new CommonMonitorFactory();
        MessageHelper helper;
        //伪造假时间戳信息
        long currTime = System.currentTimeMillis();
        //发送7条数据，模拟一天一条数据
        for (int i = 1; i <= 300; i++) {
            helper = new MessageHelper(ip);
            //test1
            helper.setCurrentLcId("testInfo1");
            MonitorQuota testQuota1 = monitorFactory.getExtentionQuota("phenixtest.support.TestQuata1");
            for (Map.Entry<String, Number> entry : testQuota1.getQuataTable().entrySet()) {
                helper.addMe(entry.getKey(), entry.getValue().toString(), MessageHelper.MeDataType.l);
            }
            logger.info(helper.getJsonResult().toJSONString());
            jmsAgent.sendMessage(helper.getJsonResult());
            Thread.sleep(5000);
        }
        System.out.println("数据发送完毕");
    }


}
