package hash;

import java.util.HashMap;
import java.util.Map;

/**
 * @author phil.zhang
 * @date 2019/2/21
 */
public class ModHash {
  private static String[] servers = {"192.168.0.0:111","192.168.0.1:111","192.168.0.2:111"
      ,"192.168.0.3:111","192.168.0.4:111"};
  private static Map<Integer, String> nodes = new HashMap<>();

  static {


    for (int i = 0; i< servers.length ; i ++) {
      nodes.put(i, servers[i]);
    }
  }

  private static String getServer(String key, int nodeNum) {
    int x = getHash(key, nodeNum);
    return nodes.get(x);
  }

  private static int getHash(String str, int nodeNum) {
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
    return hash % nodeNum;
  }

  private static void removeNode(String nodeName) {
    int num = 0;
    nodes.clear();
    for (int i = 0; i< servers.length ; i ++) {
      if (servers[i].equals(nodeName)) {

      }else {
        nodes.put(num, servers[i]);
        num +=1;
      }
    }
  }

  public static void main(String[] args) {
    Map<String, String> map = new HashMap<>();
    String[] keys = {"aaa","bbb","ccc","ddd","eee","fff","ggg","hhh","iii","jjj","kkk","lll","mmm", "nnn"
        ,"ooo","ppp","qqq","rrr","sss","ttt","uuu","vvv","www","xxx","yyy","zzz","abc","def"};

    for (String key : keys) {
      System.out.println("[" + key + "]的hash值为" + getHash(key, nodes.size())
          + ", 被路由到结点[" + getServer(key, nodes.size()) + "]");
      map.put(key,getServer(key, nodes.size()));
    }

    removeNode("192.168.0.4:111");
    System.out.println();
    System.out.println("--------删除节点后-------");
    System.out.println();
    int sum = map.size();
    int num = 0;
    for (String key : keys) {
      System.out.println("[" + key + "]的hash值为" + getHash(key, nodes.size())
          + ", 被路由到结点[" + getServer(key, nodes.size()) + "]");
      if (map.get(key) == getServer(key, nodes.size())) {
        num += 1;
      }
    }
    System.out.println();
    System.out.println("缓存命中率为：" + ((double)num/(double)sum)*100 + "%");

  }
}
