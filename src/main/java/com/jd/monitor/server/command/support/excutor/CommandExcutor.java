package com.jd.monitor.server.command.support.excutor;

/**
 * User: xiangkui
 * Date: 13-7-16
 * Time: 下午5:32
 */
public interface CommandExcutor {
    public String execSHCommand(String cmdParm, String charsetNameParm) throws Exception;
}
