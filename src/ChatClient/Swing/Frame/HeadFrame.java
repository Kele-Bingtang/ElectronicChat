package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatClient.cons.EnMsgType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储多个头像的界面
 */
public class HeadFrame extends JFrame{

    //头像列表
    public static List<JButton> icons=new ArrayList<JButton>();
    //通信
    Socket socket;
    //主界面的头像框
    JButton headButton;
    //自己的用户id
    String userid;
    //头像id
    int iconID;

    public HeadFrame(Socket socket, String userid, int iconID, JButton headButton){
        this.socket = socket;
        this.userid = userid;
        this.iconID = iconID;
        this.headButton = headButton;
        init();
    }

    /**
     * 初始化界面
     */
    public void init(){
        //界面布局样式
        setLayout(null);
        //出现位置
        setLocation(500,250);
        //界面大小
        setSize(new Dimension(500,500));
        //设置标题图片
        setIconImage(new ImageIcon("src/ChatClient/Image/8Icon.png").getImage());
        //获取头像图片
        GetIcon(icons,"src/ChatClient/Image/icon",50,50,this.getWidth(),40,50,50);
        //遍历存储头像的列表，响应点击某图像的操作
        for(int i = 0;i < icons.size();i++){
            add(icons.get(i));
            JButton tmpIcon = icons.get(i);
            //响应点击操作
            tmpIcon.addActionListener(e -> iconActionPerformed(e,this,icons,tmpIcon,headButton));
        }

    }

    /**
     * 将指定目录下的图片转换为头像
     * @param buttonList 存储图像的集合
     * @param path 文件目录
     * @param initX 坐标X位置
     * @param initY 坐标Y位置
     * @param width 头像界面宽度
     * @param span 初始间距
     * @param bk_LR 左右边框距离
     * @param bk_UD 上下边框距离
     */
    public void GetIcon(List<JButton> buttonList,String path,int initX,int initY,int width,int span,int bk_LR,int bk_UD){
        //1.获取图像文件
        int x = initX;
        int y = initY;
        File file = new File(path);
        File[] tmpFileList = file.listFiles();
        //2.为按钮设置图像文件
        for(int i = 0;i < tmpFileList.length;i++){
            ImageIcon tmpImg = new ImageIcon(tmpFileList[i].getPath());
            JButton tmpButton = new JButton();
            tmpButton.setBounds(x,y,70,70);
            tmpButton.setIcon(ChangeImgSize(tmpImg,tmpButton));
            tmpButton.setToolTipText("image");
            buttonList.add(tmpButton);
            if(x + tmpButton.getWidth() < width - bk_LR)
                x += (span + tmpButton.getWidth());
            else
            {
                x = initX;
                y += (span + tmpButton.getHeight());
            }
        }
    }

    /**
     * 获取头像图片，修改大小适应头像框
     * @param tmpImg 头像图片
     * @param tmpButton 头像框
     * @return 头像图片
     */
    public static ImageIcon ChangeImgSize(ImageIcon tmpImg, JButton tmpButton){
        //图片尺寸适应头像框
        Image temp = tmpImg.getImage().getScaledInstance(tmpButton.getWidth(), tmpButton.getHeight(), tmpImg.getImage().SCALE_DEFAULT);
        tmpImg= new ImageIcon(temp);
        return tmpImg;
    }

    /**
     * 响应点击头像后的操作，将点击的头像获取，主界面的头像框更新该头像
     * 向服务器发送更改头像的id，数据库进行更新
     * @param e 监听操作
     * @param IconFrame 头像列表界面
     * @param icons 存储头像的集合
     * @param IconButton 头像图片
     * @param headButton 主界面的头像框
     */
    private void iconActionPerformed(ActionEvent e, JFrame IconFrame, List<JButton> icons, JButton IconButton, JButton headButton){
        //主界面的头像改为选择后的头像
        headButton.setIcon(ChangeImgSize((ImageIcon) IconButton.getIcon(),headButton));
        //获取更改头像ID
        iconID = icons.indexOf(IconButton);
        //发给服务器
        new Send(socket).sendMsg(EnMsgType.EN_MSG_MODIFY_ICON.toString() + " " + userid + ":" + iconID);
        //关闭界面
        IconFrame.setVisible(false);
    }
}
