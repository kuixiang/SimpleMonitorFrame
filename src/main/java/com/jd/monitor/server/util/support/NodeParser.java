package com.jd.monitor.server.util.support;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * 定义一个节点转换器，将DomianNode转换成Json、XML等串
 * User: xiangkui
 * Date: 13-1-25
 * Time: 下午7:09
 */
public class NodeParser {
    /**
     * 根据一个节点信息，构建一个Json对象
     *
     * @param node
     * @return
     */
    public static JSONObject parseJson(Node node) {
        JSONObject rootJson = new JSONObject();
        //先初始化构建rootJson对象
        //附带上comment表
        Map pTable = node.getPropertyTable();
        Iterator pIterator = pTable.entrySet().iterator();
        while (pIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) pIterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            rootJson.put(key, value);
        }
        JSONObject rootlcObj = new JSONObject();
        if (node instanceof Monitor) {
            rootlcObj.put("s_1_k", node.getIndexName());
            //附带上comment表
            Map keyCommentsTable = ((Monitor) node).getKeyCommentsTable();
            Iterator keyIterator = keyCommentsTable.entrySet().iterator();
            while (keyIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) keyIterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                rootlcObj.put(key, value);
            }
            rootJson.put("lc", rootlcObj);
            //参与后续子节点问题计算
        } else if (node instanceof Item) {
            //获取具体的Me业务值了
            String key = ((Item) node).getKey();
            String value = ((Item) node).getValue();
            rootJson.put("k", key);
            rootJson.put("v", value);
        } else
            ;
        //将所有子节点一律放在meObj中
        JSONArray meObj = new JSONArray();
        Iterator<Node> iterator = node.newChildNodeInterator();
        while (iterator.hasNext()) {
            Node childNode = iterator.next();
            meObj.add(NodeParser.parseJson(childNode));//递归拼装Json对象
        }
        rootJson.put("me", meObj);
        return rootJson;

    }
}
