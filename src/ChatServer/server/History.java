package ChatServer.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 聊天记录存储区
 */
public class History {
    public static Map<String,StringBuilder> historyMap = new HashMap<>();
    StringBuilder  historyBui;
    public static StringBuilder buffs = new StringBuilder();

    public StringBuilder getHitory(String chatName) {
        StringBuilder hit = null;
        //先清空记录，再获取历史记录
        Set<String> set = historyMap.keySet();
        for (String key : set) {
            if (key.equals(chatName)) {
                hit = historyMap.get(key);
            }
        }
        return hit;
    }

    /**
     * 存储聊天记录
     * @param message 聊天消息
     * @param chatName 聊天对象
     */
    public void setHitory(String message,String chatName){
        historyBui = new StringBuilder();
        //刚开始keySet为空，所以不执行for循环
        Set<String> keySet = historyMap.keySet();
        for (String key : keySet) {
            if (key.equals(chatName)) {
                //1、先获取历史记录
                historyBui.append(historyMap.get(key).toString());
            }
        }
        //2、后获取聊天记录
        historyBui.append(message).append("\n");
        //3、覆盖原来的聊天历史记录
        historyMap.put(chatName,historyBui);
    }

    /**
     * 解析信息
     * 格式：聊天对象:自己昵称 消息
     * @param message 信息
     * @return 解析后的的信息
     */
    public String getRealMessage(String message){
        //格式：聊天对象:自己昵称 消息
        int index1 = message.indexOf(":");
        int index2 = message.indexOf(" ");
        //获取聊天对象
        String chatName = message.substring(0,index1);
        //获取发送人昵称
        //解析发送的名称
        String nickName = message.substring(index1+1,index2);
        //解析信息
        message = message.substring(index1 + 1);
        System.out.println("解析后的消息：" + message);
        return  message;
    }


}
