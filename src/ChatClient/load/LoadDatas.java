package ChatClient.load;

import Swing.MainFrame;
import bean.Imformation;
import dao.FriendDaompl;
import dao.ImformationDaoImpl;

import java.util.List;

public class LoadDatas {

    public void loadData(String userid){
        Imformation imformation = new ImformationDaoImpl().getImformation(userid);

        if(null != imformation.getNickName()){
            //昵称
            MainFrame.nickNameLabel.setText(imformation.getNickName());
        }
        if(null != imformation.getSignNature()){
            //个性签名
            MainFrame.signField.setText(imformation.getSignNature());
        }
    }

    /**
     * 获得朋友列表
     * @param userid
     * @return
     */
    public String[] getFriends(String userid){
        List<String> list = new FriendDaompl().getFriends(userid);
        String []fd = new String[list.size()];
        for(int i =0;i < list.size();i++){
            fd[i] = list.get(i);
        }
        return fd;
    }

    /**
     * 获得昵称
     * @return
     */
    public String getNickName(String userid){
        Imformation imformation = new ImformationDaoImpl().getImformation(userid);
        return imformation.getNickName();
    }

    /**
     * 获得个性签名
     */
    public String getSignNature(String userid){
        Imformation imformation = new ImformationDaoImpl().getImformation(userid);
        return imformation.getSignNature();
    }

}
