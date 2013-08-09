package com.jd.monitor.server.util.support;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: xiangkui
 * Date: 13-1-24
 * Time: 下午6:57
 */
/*监控域对象，包含若干个MonitorItem*/
public class Monitor extends Node{
    private static final String defaultName = "root";
    private static final String illegalStr= Item.lableName;
    Map keyComments=new HashMap();//针对关键字添加的属性列表说明
    public void addKeyComment(String key, String value) {
        keyComments.put(key, value);
    }
    public String getKeyComment(String key){
        return (String) keyComments.get(key);
    }
    /**
     * 返回整个属性表
     * @return
     */
    public Map<String,String> getKeyCommentsTable(){

        return keyComments;
    }

    public Monitor(String indexName) {
        super(indexName);
        if (indexName.contains(illegalStr))
            throw new IllegalArgumentException("you can not set the name of Node contain '#' ");

    }

    public Monitor() {
        super(defaultName);
    }

    List<Node> items = new ArrayList();

    //包含一个迭代索引对象名字的迭代器

    @Override
    public void add(Node node) {
        super.add(node);
        items.add(node);
    }
    public void addIfAbsent(Node node) {
        super.add(node);
        if(this.getChild(node.getIndexName())!=null)
            return;
        items.add(node);
    }

    @Override
    public void remove(Node node) {
        items.remove(node);
    }

    @Override
    public Node getChild(int i) {
        return items.get(i);
    }

    @Override
    public Node getChild(String indexName) {
        Iterator iterator=items.iterator();
        while(iterator.hasNext()){
            Node node= (Node) iterator.next();
            if(node.getIndexName().equals(indexName))
                return node;
        }
        return null;
    }
    @Override
    public Iterator<Node> newChildNodeInterator() {
        return items.iterator();
    }

}
