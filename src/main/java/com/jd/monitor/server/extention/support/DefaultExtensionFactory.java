package com.jd.monitor.server.extention.support;


import com.jd.monitor.server.extention.ExtensionFactory;

public class DefaultExtensionFactory implements ExtensionFactory {

    public DefaultExtensionFactory() {
    }

    public <T> T getExtension(String name) {
        T clazz = null;
        try {
            clazz = (T) Class.forName(name).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) clazz;
    }

}
