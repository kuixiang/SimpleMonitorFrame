package com.jd.monitor.server.command.support.excutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * User: xiangkui
 * Date: 13-7-16
 * Time: 下午5:27
 */
public class ShellExcutor {
    /**
     * 执行命令获得返回结果
     *
     * @param cmdParm         命令内容
     * @param charsetNameParm 命令编码方式
     * @return 命令执行结果
     * @throws Exception
     */
    public static String execSHCommand(String cmdParm, String charsetNameParm) throws Exception {
        InputStream in = null;
        StringBuilder result = new StringBuilder();

        String[] cmds = new String[]{"/bin/bash", "-c", cmdParm};
        Process pro = Runtime.getRuntime().exec(cmds);
        pro.waitFor();
        in = pro.getInputStream();

        BufferedReader read = new BufferedReader(new InputStreamReader(in, charsetNameParm));
        String line = "";
        while ((line = read.readLine()) != null) {
            result.append(line).append("\r\n");
        }
        pro.destroy();
        return result.toString();
    }
}
