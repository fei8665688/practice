package hash;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author phil.zhang
 * @date 2019/1/26
 */
public class ConsistHashNoVirtualNode {

    private static String[] servers = {"192.168.0.0:111","192.168.0.1:111","192.168.0.2:111"
            ,"192.168.0.3:111","192.168.0.4:111"};

    private static SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();

    static {
        for (int i =0; i < servers.length; i++) {
            int hash = getHash(servers[i]);
//            System.out.println("["+servers[i] + "]加入集合中，其Hash值为" + hash);
            sortedMap.put(hash,servers[i]);
        }
        System.out.println();
    }

    private static String getServer(String key) {
        int hash = getHash(key);
        SortedMap<Integer, String> subMap = sortedMap.tailMap(hash);
        if (subMap.isEmpty()) {
            Integer i = sortedMap.firstKey();
            return sortedMap.get(i);
        }else {
            Integer i = subMap.firstKey();
            return subMap.get(i);
        }
    }


    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
            hash += hash << 13;
            hash ^= hash >> 7;
            hash += hash << 3;
            hash ^= hash >> 17;
            hash += hash << 5;
        }

        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }



    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();
        String[] keys = {"aaa","bbb","ccc","ddd","eee","fff","ggg","hhh","iii","jjj","kkk","lll","mmm", "nnn"
                ,"ooo","ppp","qqq","rrr","sss","ttt","uuu","vvv","www","xxx","yyy","zzz","abc","def"};

        for (String key : keys) {
            System.out.println("[" + key + "]的hash值为" + getHash(key)
                    + ", 被路由到结点[" + getServer(key) + "]");
            map.put(key,getServer(key));
        }

        sortedMap.remove(getHash("192.168.0.4:111"));
        System.out.println();
        System.out.println("--------删除节点后-------");
        System.out.println();
        int sum = map.size();
        int num = 0;
        for (String key : keys) {
            System.out.println("[" + key + "]的hash值为" + getHash(key)
                    + ", 被路由到结点[" + getServer(key) + "]");
            if (map.get(key) == getServer(key)) {
                num += 1;
            }
        }
        System.out.println();
        System.out.println("缓存命中率为：" + ((double)num/(double)sum)*100 + "%");
    }
}
