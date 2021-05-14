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

public class HeadFrame extends JFrame{


    //头像列表
    public static List<JButton> icons=new ArrayList<JButton>();
    //通信
    Socket socket;
    //
    JButton headButton;

    String userid;
    int iconID;

    public HeadFrame(Socket socket, String userid, int iconID, JButton headButton){
        this.socket = socket;
        this.userid = userid;
        this.iconID = iconID;
        this.headButton = headButton;
        init();
    }

    public void init(){
        setLayout(null);
        setLocation(500,500);
        setSize(new Dimension(500,500));
        //获取头像图片
        GetIcon(icons,"src/ChatClient/Image/icon",50,50,this.getWidth(),40,50,50);

        for(int i=0;i<icons.size();i++){
            add(icons.get(i));
            JButton tmpIcon = icons.get(i);
            tmpIcon.addActionListener(e -> iconActionPerformed(e,this,icons,tmpIcon,headButton));
        }

    }

    public void GetIcon(List<JButton> buttonList,String path,int initX,int initY,int width,int span,int bk_LR,int bk_UD){
        //bk_LR:左右边框距离
        //bk_UP:上下边框距离
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
                x += (span+tmpButton.getWidth());
            else
            {
                x = initX;
                y += (span+tmpButton.getHeight());
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
        Image temp = tmpImg.getImage().getScaledInstance(tmpButton.getWidth(), tmpButton.getHeight(), tmpImg.getImage().SCALE_DEFAULT);
        tmpImg= new ImageIcon(temp);
        return tmpImg;
    }

    private void iconActionPerformed(ActionEvent e, JFrame IconFrame, List<JButton> icons, JButton IconButton, JButton headButton){
        headButton.setIcon(ChangeImgSize((ImageIcon) IconButton.getIcon(),headButton));
        iconID = icons.indexOf(IconButton);
        new Send(socket).sendMsg(EnMsgType.EN_MSG_MODIFY_ICON.toString() + " " + userid + ":" + iconID);
        IconFrame.setVisible(false);
    }
}
