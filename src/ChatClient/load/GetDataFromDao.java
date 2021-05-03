package ChatClient.load;

import bean.Information;
import dao.FriendDaompl;
import dao.InformationDaoImpl;
import dao.UserDaoImpl;

import java.util.List;

/**
 * 与dao层交互，获取数据
 */
public class GetDataFromDao {

    UserDaoImpl userDaompl;

    InformationDaoImpl informationDaompl;

    FriendDaompl friendDaompl;

    public GetDataFromDao(){
        userDaompl = new UserDaoImpl();
        informationDaompl = new InformationDaoImpl();
        friendDaompl = new FriendDaompl();
    }

    public List<Information> getAllData(){
        List<Information> informationList = informationDaompl.getAllImformation();
        return informationList;
    }

    /**
     * 获得朋友列表
     * @param userid 用户id
     * @return 好友列表
     */
    public String[] getFriends(String userid){
        List<String> list = friendDaompl.getFriends(userid);
        String []fd = new String[list.size()];
        for(int i =0;i < list.size();i++){
            fd[i] = list.get(i);
        }
        return fd;
    }

    /**
     * 获得昵称
     * @return 昵称
     */
    public String getNickName(String userid){
        Information information = informationDaompl.getImformation(userid);
        return information.getNickName();
    }

    /**
     * 获得个性签名
     */
    public String getSignNature(String userid){
        Information information = informationDaompl.getImformation(userid);
        return information.getSignNature();
    }

    /**
     * 更新昵称
     * @param nickName 昵称
     * @param userid id
     */
    public void setNickName(String userid,String nickName){
        informationDaompl.storeNickName(userid,nickName);
    }

    /**
     * 更新个性签名
     * @param signature 个性签名
     * @param userid 用户id
     */
    public void setSignature(String userid,String signature){
        informationDaompl.storeSignature(userid,signature);
    }

    /**
     * 验证密码是否正确
     * @param userid 用户id
     * @param oldPassword 密码
     * @return 判断
     */
    public boolean verifyPassword(String userid,String oldPassword){
        boolean isRight = userDaompl.verifyPassword(userid,oldPassword);
        return isRight;
    }

    public void modifyPassword(String userid, String password){
        userDaompl.modifyPassword(userid,password);
    }
}
