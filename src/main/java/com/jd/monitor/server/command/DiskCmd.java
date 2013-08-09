package com.jd.monitor.server.command;

/**
 * User: xiangkui
 * Date: 13-7-16
 * Time: 下午5:26
 */
public class DiskCmd extends Command{
    private String DISK_CMD = "df -h |grep ";

    @Override
    public void execute() {

    }
}
