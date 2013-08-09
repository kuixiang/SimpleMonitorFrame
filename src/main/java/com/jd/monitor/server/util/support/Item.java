package com.jd.monitor.server.util.support;


import java.util.HashMap;
import java.util.Map;

/**
 * User: xiangkui
 * Date: 13-1-24
 * Time: 下午6:57
 */
public class Item extends Node {

    /*key-value对为业务逻辑的数据Map*/
    public static final String lableName = "#";
    private String key;
    private String value;
    private Map<String, String> comment;

    /**
     * 索引关键字名称，为全局路劲表示法
     * 如a.b.c表示绝对路径名称，且当前key名称为c,且当前也为索引节点
     * 如a.b.#也表示绝对路径名称，当前非索引节点，名称为#
     */
    public Item(String indexName) {
        super(lableName + indexName);
    }

    public Item() {
        super(lableName);
    }

    public Item(String key, String value) {
        super(lableName + key);
        if (key.contains(lableName)) {
            throw new IllegalArgumentException("can not contain special key " + lableName);
        }
        this.key = key;
        this.value = value;
    }

    public String getIndexName() {
        String indexName = super.getIndexName();
        int lablePos = indexName.indexOf(lableName);
        return indexName.substring(lablePos + lableName.length());
    }

    /**
     * 添加一项备注项,包括name、ud、dt等说明
     *
     * @param key
     * @param value
     */
    public void addComment(String key, String value) {
        if (comment == null)
            comment = new HashMap<String, String>();
        comment.put(key, value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Item{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", comment=" + comment +
                '}';
    }

    /*叶子节点不支持添加子节点*/
    public void add(Node node) {
        throw new UnsupportedOperationException();
    }

    public void addIfAbsent(Node node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node getChild(String indexName) {
        throw new UnsupportedOperationException();
    }
}
