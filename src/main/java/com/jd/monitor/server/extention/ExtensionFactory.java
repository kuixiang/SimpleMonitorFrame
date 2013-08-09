
package com.jd.monitor.server.extention;


/**
 * 监控扩展点，用于动态装载自定义监控点
 */
public interface ExtensionFactory {
    <T> T getExtension(String name);
}
