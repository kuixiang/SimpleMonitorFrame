package com.jd.monitor.server.jms;


/**
 * Date: 13-6-19
 * Time: 下午4:09
 */
public interface JMSAgent {
    /*发送一个监控报文*/
    public void sendMessage(Object obj);

}
