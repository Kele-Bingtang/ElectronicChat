package ChatServer.load;

import ChatServer.bean.Group;
import ChatServer.bean.Information;
import ChatServer.dao.FriendDaompl;
import ChatServer.dao.GroupChatDaoImpl;
import ChatServer.dao.InformationDaoImpl;
import ChatServer.dao.UserDaoImpl;

import java.util.List;

/**
 * 与dao层交互，获取数据
 */
public class GetDataFromDao {

    UserDaoImpl userDaompl;

    InformationDaoImpl informationDaompl;

    FriendDaompl friendDaompl;

    GroupChatDaoImpl groupChatDaompl;

    public GetDataFromDao(){
        userDaompl = new UserDaoImpl();
        informationDaompl = new InformationDaoImpl();
        friendDaompl = new FriendDaompl();
        groupChatDaompl = new GroupChatDaoImpl();
    }

    /**
     * 获得好友昵称和状态(可修改)
     * @param userid 用户id
     * @return 好友列表
     */
    public String[] getFriendNickNameAndStatus(String userid){
        List<Information> list = getFriends(userid);
        String []friendName = new String[list.size()];
        for(int i =0;i < list.size();i++){
            friendName[i] = list.get(i).getNickName() + "     (" + list.get(i).getStatus() + ")";
        }
        return friendName;
    }

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
     * 获取全部用户信息
     * @return 存储用户信息的集合
     */
    public List<Information> getAllData(){
        List<Information> informationList = informationDaompl.getAllImformation();
        return informationList;
    }

    /**
     * 获取全部好友
     * @param userid 用户id
     * @return 存储好友信息的集合
     */
    public List<Information> getFriends(String userid){
        List<Information> friends = friendDaompl.getFriends(userid);
        return friends;
    }


    /**
     * 获取好友id
     * @param userid 用户id
     * @return 好友id
     */
    public String[] getFriendid(String userid){
        List<Information> list = getFriends(userid);
        String []friendid = new String[list.size()];
        for(int i =0;i < list.size();i++){
            friendid[i] = list.get(i).getUid();
        }
        return friendid;
    }

    /**
     * 添加好友
     * @param userid 用户id
     * @param friendid 好友id
     */
    public void addFriend(String userid,String friendid){
        friendDaompl.addFriend(userid,friendid);
    }

    public void modifyStatus(String userid,String status){
        informationDaompl.modifyStatus(userid,status);
    }


    public void deleteFriend(String userid, String friendid){
        friendDaompl.deleteFriend(userid,friendid);
    }

    /**
     * 获得用户的信息(昵称、签名、状态)
     * @return 昵称
     */
    public Information getinformationByUserid(String userid){
        Information information = informationDaompl.getImformation(userid);
        return information;
    }


    /**
     * 更新昵称
     * @param nickName 昵称
     * @param userid id
     */
    public void modifyNickName(String userid,String nickName){
        informationDaompl.storeNickName(userid,nickName);
    }

    /**
     * 更新个性签名
     * @param signature 个性签名
     * @param userid 用户id
     */
    public void modifySignature(String userid,String signature){
        informationDaompl.storeSignature(userid,signature);
    }


    /**
     * 修改密码
     * @param userid 用户id
     * @param password 密码
     */
    public void modifyPassword(String userid, String password){
        userDaompl.modifyPassword(userid,password);
    }

    public boolean verifyFriendID(String userid){
        return userDaompl.verifyFriendID(userid);
    }

    public Information getUserIDByNickName(String nickName){
        return informationDaompl.getUserIDByNickName(nickName);
    }

    /**
     * 获取群的信息
     * @return 群的信息
     */
    public List<Group> getGroupInformation(){
        return groupChatDaompl.getGroupInformation();
    }


    public String[] getGroupName(){
        List<Group> groupList = getGroupInformation();
        String []groupName = new String[groupList.size()];
        for(int i =0;i < groupList.size();i++){
            groupName[i] = groupList.get(i).getGroupName();
        }
        return groupName;
    }

    public Group getGroupidByName(String groupName){
        return groupChatDaompl.getGroupid(groupName);
    }


}
