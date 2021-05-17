package ChatServer.load;

import ChatServer.bean.Group;
import ChatServer.bean.Information;
import ChatServer.dao.FriendDaompl;
import ChatServer.dao.GroupChatDaoImpl;
import ChatServer.dao.InformationDaoImpl;
import ChatServer.dao.UserDaoImpl;
import java.util.List;

/**
 * 与Dao层交互，获取数据
 * 保护Dao，中间人，处理消息，判断消息的安全性
 */
public class GetDataFromDao {
    //UserDaoImpl实例
    UserDaoImpl userDaompl;
    //InformationDaoImpl实例
    InformationDaoImpl informationDaompl;
    //FriendDaompl实例
    FriendDaompl friendDaompl;
    //GroupChatDaoImpl实例
    GroupChatDaoImpl groupChatDaompl;

    public GetDataFromDao(){
        userDaompl = new UserDaoImpl();
        informationDaompl = new InformationDaoImpl();
        friendDaompl = new FriendDaompl();
        groupChatDaompl = new GroupChatDaoImpl();
    }

    /*------------------------------------UserDao------------------------------------*/

    /**
     * 判断用户名和密码是否正确
     * @param userid 用户id
     * @param password 密码
     * @return true false
     */
    public boolean verifyUseridAndPassword(String userid,String password){
        return userDaompl.verifyUseridAndPassword(userid,password);
    }

    /**
     * 注册用户名和密码
     * @param userid 用户id
     * @param password 密码
     */
    public void register(String userid,String password){
        userDaompl.register(userid,password);

    }

    /**
     * 修改密码
     * @param userid 用户id
     * @param password 密码
     */
    public void modifyPassword(String userid, String password){
        userDaompl.modifyPassword(userid,password);
    }

    /**
     * 验证是否存在所添加好友的id
     * @param userid 用户id
     * @return 存在id或不存在id    true or false
     */
    public boolean verifyFriendID(String userid){
        return userDaompl.verifyFriendID(userid);
    }

    /*------------------------------------InformationDao------------------------------------*/

    /**
     * 注册后的userid存入数据库
     * @param userid 用户id
     */
    public void registerUserid(String userid){
        informationDaompl.registerUserid(userid);
    }

    /**
     * 获取全部用户信息
     * @return 存储用户信息的集合
     */
    public List<Information> getAllImformation(){
        return informationDaompl.getAllImformation();
    }

    /**
     * 根据用户id获得用户的信息(昵称、签名、状态)
     * @return 昵称
     */
    public Information getImformationByUserid(String userid){
        return informationDaompl.getImformationByUserid(userid);
    }

    /**
     * 通过昵称获取用户名
     * @param nickName 昵称
     * @return  information的userid
     */
    public Information getUserIDByNickName(String nickName){
        return informationDaompl.getUserIDByNickName(nickName);
    }

    /**
     * 修改的昵称发给Dao层
     * @param userid id
     * @param nickName 昵称
     */
    public void modifyNickName(String userid,String nickName){
        informationDaompl.modifyNickName(userid,nickName);
    }

    /**
     * 修改的个性签名发给Dao层
     * @param userid 用户id
     * @param signature 个性签名
     */
    public void modifySignature(String userid,String signature){
        informationDaompl.modifySignature(userid,signature);
    }
    /**
     * 修改的头像ID发给Dao层
     * @param userid 用户id
     * @param iconID 头像ID
     */
    public void modifyIconID(String userid,int iconID){
        informationDaompl.modifyIconID(userid,iconID);
    }

    /**
     * 修改的状态发给Dao层
     * @param userid 用户id
     * @param status 状态
     */
    public void modifyStatus(String userid,String status){
        informationDaompl.modifyStatus(userid,status);
    }

    /*------------------------------------FriendDao------------------------------------*/

    /**
     * 获取全部好友
     * @param userid 用户id
     * @return 存储好友信息的集合
     */
    public List<Information> getFriends(String userid){
        return friendDaompl.getFriends(userid);
    }

    /**
     * 获取好友id
     * 调用getFriends方法
     * @param userid 用户id
     * @return 好友id
     */
    public String[] getFriendid(String userid){
        //调用getFriends方法获取全部好友
        List<Information> list = getFriends(userid);
        //存有好友id
        String []friendid = new String[list.size()];
        for(int i =0;i < list.size();i++){
            friendid[i] = list.get(i).getUid();
        }
        return friendid;
    }

    /**
     * 获得好友昵称和状态(可修改)
     * 调用getFriends方法
     * @param userid 用户id
     * @return 好友列表
     */
    public String[] getFriendNickNameAndStatus(String userid){
        //调用getFriends方法获取全部好友
        List<Information> list = getFriends(userid);
        //存有好友昵称
        String []friendName = new String[list.size()];
        for(int i =0;i < list.size();i++){
            //自定义格式
            friendName[i] = list.get(i).getNickName() + "     (" + list.get(i).getStatus() + ")";
        }
        return friendName;
    }

    /**
     * 添加好友
     * @param userid 用户id
     * @param friendid 好友id
     */
    public void addFriend(String userid,String friendid){
        friendDaompl.addFriend(userid,friendid);
    }

    /**
     * 删除好友
     * @param userid 用户id
     * @param friendid 好友id
     */
    public void deleteFriend(String userid, String friendid){
        friendDaompl.deleteFriend(userid,friendid);
    }

    /*------------------------------------GroupDao------------------------------------*/

    /**
     * 获取群的全部信息
     * @return 群的信息
     */
    public List<Group> getGroupInformation(){
        return groupChatDaompl.getGroupInformation();
    }

    /**
     * 获取全部群名
     * 调用getGroupInformation方法
     * @return 全部群名
     */
    public String[] getGroupName(){
        //调用getGroupInformation方法
        List<Group> groupList = getGroupInformation();
        String []groupName = new String[groupList.size()];
        for(int i =0;i < groupList.size();i++){
            groupName[i] = groupList.get(i).getGroupName();
        }
        return groupName;
    }

    /**
     * 根据群名查询群id
     * @param groupName 群名
     * @return group的groupid
     */
    public Group getGroupidByName(String groupName){
        return groupChatDaompl.getGroupidByName(groupName);
    }


}
