package com.jd.monitor.server.util.support;

/**
 * Created with IntelliJ IDEA.
 * User: xiangkui
 * Date: 13-1-24
 * Time: 下午6:31
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 领域节点模型，为一个根节点或叶子节点
 * 能够迭代子节点
 */
public abstract class Node {
    /**
     * 索引关键字名称，为全局路劲表示法
     * 如a.b.c表示绝对路径名称，且当前key名称为c,且当前也为索引节点
     * 如a.b.#也表示绝对路径名称，当前非索引节点，名称为#
     */
    private String indexName = new String("");
    private StringBuilder absoluteIndexName = new StringBuilder(indexName);
    Map propertys = new HashMap();//附加的属性表,描述节点


    public void addProperty(String key, String value) {
        propertys.put(key, value);
    }
    public String getProperty(String key){
        return (String) propertys.get(key);
    }


    /**
     * 返回整个属性表
     * @return
     */
    public Map<String,String> getPropertyTable(){
        return propertys;
    }

    public Node(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexName() {
        return indexName;
    }

    public StringBuilder getAbsoluteIndexName() {
        return absoluteIndexName;
    }

    public void add(Node node) {
        node.absoluteIndexName.insert(0, absoluteIndexName + ".");
    }

    /**
     * 添加一个子节点，避免重复添加同名子节点
     * 如果有同名子节点，则放弃添加；如果没有该名称节点，则添加
     *
     * @param node
     */
    public void addIfAbsent(Node node) {
        // 注意直接getChild将出错
        if(this.getChild(node.getIndexName())!=null)
            return;
        node.absoluteIndexName.insert(0, indexName + ".");
    }

    public void remove(Node node) {
        throw new UnsupportedOperationException();
    }

    public Node getChild(int i) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据索引名获得子节点对象 ,为模板方法
     *
     * @param indexName
     * @return
     */
    public abstract Node getChild(String indexName);
    /*
    * 返回子节点迭代器，这个迭代器迭代出当前节点的所有子节点
    * */
    public Iterator<Node> newChildNodeInterator() {
        return new Iterator<Node>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Node next() {
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
