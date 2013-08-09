package com.jd.monitor.server.util;


import com.alibaba.fastjson.JSONObject;
import com.jd.monitor.server.util.support.Item;
import com.jd.monitor.server.util.support.Monitor;
import com.jd.monitor.server.util.support.Node;
import com.jd.monitor.server.util.support.NodeParser;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiangkui
 * Date: 13-1-22
 * Time: 上午10:47
 */
public class MessageHelper {
    public MessageHelper() {
        monitorNode = new Monitor();
        indexNode=monitorNode;
        setTime(System.currentTimeMillis());
        setMessageType(MessageType.StuctAdaptMsg); //自适应报文格式
    }
    public MessageHelper(String rootKeyName) {
        monitorNode = new Monitor(rootKeyName);
        indexNode=monitorNode;
        setTime(System.currentTimeMillis());
        setMessageType(MessageType.StuctAdaptMsg); //自适应报文格式
    }

    public enum MessageType {
        addStruc("addStruc", "001"),
        delStruct("delStruct", "002"),
        monitorMsg("monitorMsg", "003"),
        StuctMsg("StuctMsg", "004"), //带结构的监控信息, 节点增量变更
        StuctAdaptMsg("StuctAdaptMsg","005"); //自适应节点 变化的报文格式
        // 成员变量
        private String name = "StuctAdaptMsg";
        private String index = "005";

        private MessageType(String name, String index) {
            this.name = name;
            this.index = index;
        }

        // 普通方法
        public static String getName(String index) {
            for (MessageType m : MessageType.values()) {
                if (m.getIndex().equals(index)) {
                    return m.name;
                }
            }
            return null;
        }

        public static String getIndex(String name) {
            for (MessageType m : MessageType.values()) {
                if (m.getName().equals(name)) {
                    return m.index;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }
    public enum MeDataType {
        s(String.class, "s"), i(int.class, "i"), l(long.class, "l"), d(double.class, "d");
        Class<?> clazz;
        String name = "N/A";
        public Class<?> getClazz() {
            return clazz;
        }
        public String getName() {
            return name;
        }
        private MeDataType(Class<?> clazz, String name) {
            this.clazz = clazz;
            this.name = name;
        }

        /**
         * 返回 MeDataType所支持的类型对应名字
         * @param tClass
         * @return
         */
        public static String getName(Class<?> tClass) {
            for (MeDataType t : MeDataType.values()) {
                if (tClass == t.getClazz()) {
                    return t.name;
                }
            }
            return null;
        }
    }
    public enum MeDataUd {
        ms;
    }

    private final Node monitorNode; //根节点对象,最终结果数据
    private long t = System.currentTimeMillis();
    private List<String> linkedIndex = new LinkedList<String>();

    //封装完成的消息体
    public void setTime(long time) {
        this.t = time;
        monitorNode.addProperty("t", "" + this.t);

    }
    //封装完成的消息体
    public void setMessageType(MessageType type) {
        monitorNode.addProperty("tp",type.getIndex());
    }

    /**
     * 用于封装必填的s_i_k参数项
     * 设定当前索引到的Lc地址
     * 一级索引-二级索引-三级索引
     * @param lcIndex
     */
    private Node indexNode;//从根几点开始索引

    public void setCurrentLcId(String... lcIndex) {
        if (lcIndex.length == 0) {
            throw new IllegalStateException("need one index at least to call function SetCurr..");
        }
        linkedIndex.clear();
        indexNode = monitorNode;
        for (int i = 0; i < lcIndex.length; i++) {
            indexNode.addIfAbsent(new Monitor(lcIndex[i]));
            indexNode = indexNode.getChild(lcIndex[i]);
            linkedIndex.add(lcIndex[i]);
        }
    }


    /**
     * 在 s_i_k 基础上设定 s_i_n值
     * 一一对应上，不足之处，后续sik不填充对应sin值，多余的忽略
     * 在setCurrentLcId方法之后使用
     * @param lcName
     */
    public void setLcName(String... lcName) throws IllegalStateException {
        if (lcName.length == 0) {
            throw new IllegalStateException("need one lcName at least to call function SetCurr..");
        }
        Node tempNode = monitorNode;//从根几点开始索引
        for (int i = 0; i < lcName.length; i++) {
            tempNode = tempNode.getChild(linkedIndex.get(i));
            if (tempNode instanceof Monitor)
                ((Monitor) tempNode).addKeyComment("s_1_n:", lcName[i]);
        }

    }
    /**
     * 装载最新ME数据Json对象到Root对象中
     * @param key   定义的指标key
     * @param value 指标value值
     */
    public void addMe(String key,String value,MeDataType dataType) {
        Node meNode = new Item(key, value);
        meNode.addProperty("dt", dataType.getName());
        indexNode.add(meNode);//添加一个叶子节点
    }

    public void setMeshowName(String meKey, String name) throws IllegalStateException {
        Node meNode = indexNode.getChild(meKey);
        if (meNode == null) {
            throw new UnsupportedOperationException("Forget to setMe ?(meKey=" + meKey + ")");
        }
        meNode.addProperty("n", name);
    }

    public void setMeDataType(String meKey, MeDataType dataType) throws IllegalStateException {
        Node meNode = indexNode.getChild(meKey);
        if (meNode == null) {
            throw new UnsupportedOperationException("Forget to setMe ?(meKey=" + meKey + ")");
        }
        meNode.addProperty("dt", dataType.getName());
    }

    /**
     * 设定单位描述，如ms
     * @param meKey
     * @throws IllegalStateException
     */
    public void setMeUd(String meKey, MeDataUd ud) throws IllegalStateException {
        Node meNode = indexNode.getChild(meKey);
        if (meNode == null) {
            throw new IllegalStateException("you have not add this me" + "[" + "key=" + meKey + "]");
        }
        meNode.addProperty("ud", ud.name());
    }
    /*获得最终的Json对象*/
    public JSONObject getJsonResult() {
        //屏蔽掉构建的祖先节点
        return  NodeParser.parseJson(monitorNode);
    }
}
