package hash;

import java.util.*;

/**
 * @author phil.zhang
 * @date 2019/1/26
 */
public class ConsistHashWithVirtualNode {

    private static String[] servers = {"192.168.0.0:111","192.168.0.1:111","192.168.0.2:111"
            ,"192.168.0.3:111","192.168.0.4:111"};

    private static List<String> realNodes = new LinkedList<String>();

    private static SortedMap<Integer, String> virtualNodes = new TreeMap<Integer, String>();

    private static final int VIRTUAL_NODES = 5;



    static{
        //先把原始的服务器添加到真实结点列表中
        for(int i=0; i<servers.length; i++) {
            realNodes.add(servers[i]);
        }

        //再添加虚拟节点，遍历LinkedList使用foreach循环效率会比较高
        for (String str : realNodes){
            for(int i=0; i<VIRTUAL_NODES; i++){
                String virtualNodeName = str + "&&VN" + i;
                int hash = getHash(virtualNodeName);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
        System.out.println();
    }


    private static int getHash(String str){
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
            hash += hash << 13;
            hash ^= hash >> 7;
            hash += hash << 3;
            hash ^= hash >> 17;
            hash += hash << 5;
        }

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }


    //得到应当路由到的结点
    private static String getServer(String key){
        //得到该key的hash值
        int hash = getHash(key);
        // 得到大于该Hash值的所有Map
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        String virtualNode;
        if(subMap.isEmpty()){
            //如果没有比该key的hash值大的，则从第一个node开始
            Integer i = virtualNodes.firstKey();
            //返回对应的服务器
            virtualNode = virtualNodes.get(i);
        }else{
            //第一个Key就是顺时针过去离node最近的那个结点
            Integer i = subMap.firstKey();
            //返回对应的服务器
            virtualNode = subMap.get(i);
        }
        //virtualNode虚拟节点名称要截取一下
        if(null != virtualNode){
            return virtualNode.substring(0, virtualNode.indexOf("&&"));
        }
        return null;
    }


    public static void removeNode(String server) {
        for (int i = 0; i < VIRTUAL_NODES; i ++) {
            String virtuaNodeName = server+"&&VN"+i;
            int hash = getHash(virtuaNodeName);
            virtualNodes.remove(hash);
        }
    }


    public static void main(String[] args){
        Map<String, String> map = new HashMap<>();
        String[] keys = {"aaa","bbb","ccc","ddd","eee","fff","ggg","hhh","iii","jjj","kkk","lll","mmm", "nnn"
                ,"ooo","ppp","qqq","rrr","sss","ttt","uuu","vvv","www","xxx","yyy","zzz","abc","def"};


        for (String key : keys) {
            System.out.println("[" + key + "]的hash值为" + getHash(key)
                    + ", 被路由到结点[" + getServer(key) + "]");
            map.put(key,getServer(key));
        }

        System.out.println();
        removeNode("192.168.0.4:111");
        System.out.println();
        System.out.println("--------删除节点后-------");
        System.out.println();
        int sum = map.size();
        int num = 0;
        for (String key : keys) {
            System.out.println("[" + key + "]的hash值为" + getHash(key)
                    + ", 被路由到结点[" + getServer(key) + "]");
            if (map.get(key).equals(getServer(key))) {
                num += 1;
            }
        }

        System.out.println();
        System.out.println("缓存命中率为：" + ((double)num/(double)sum)*100 + "%");

    }

}
