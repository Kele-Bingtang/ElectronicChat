package ChatClient.Swing.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmojiFrame extends JFrame {

    public static List<JLabel> emojiList = new ArrayList<>();

    int X,Y;

    JTextPane textPane;

    public EmojiFrame(int X,int Y,JTextPane textPane){
        this.X = X;
        this.Y = Y;
        this.textPane = textPane;
        init();
    }

    public void init(){
        JScrollPane scrollPane = new JScrollPane();
        this.setContentPane(scrollPane);
        setLayout(null);
        setIconImage(new ImageIcon("src/ChatClient/Image/8Icon.png").getImage());
        setSize(530,344);
        setLocation(X,Y);
        scrollPane.setSize(527,344);
        String[] emojis = initEmoji();

        getEmoji(emojiList,emojis,15,10,this.getWidth(),5,5);

        for(int i = 0;i < emojiList.size();i++){
            scrollPane.add(emojiList.get(i));
            JLabel emojiIcon = emojiList.get(i);
            //响应点击操作
            emojiIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    textPane.setText(emojiIcon.getText());
                    setVisible(false);
                    dispose();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    emojiIcon.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    emojiIcon.setBorder(BorderFactory.createEmptyBorder());
                }
            });

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    setVisible(false);
                    dispose();
                }
            });
        }

        setVisible(true);
    }

    public String[] initEmoji(){
        String[] emoji = new String[34];
        //微笑😀
        emoji[0] = "\uD83D\uDE00";
        //大笑😁
        emoji[1] = "\uD83D\uDE01";
        //笑哭😂
        emoji[2] = "\uD83D\uDE02";
        //眯眼笑😄
        emoji[3] = "\uD83D\uDE04";
        //滴汗😅
        emoji[4] = "\uD83D\uDE05";
        //闭眼笑😆
        emoji[5] = "\uD83D\uDE06";
        //光圈😇
        emoji[6] = "\uD83D\uDE07";
        //愤怒😈
        emoji[7] = "\uD83D\uDE08";
        //眨眼😉
        emoji[8] = "\uD83D\uDE09";
        //红脸笑😊
        emoji[9] = "\uD83D\uDE0A";
        //吐舌头😋
        emoji[10] = "\uD83D\uDE0B";
        //困😌
        emoji[11] = "\uD83D\uDE0C";
        //红心眼😍
        emoji[12] = "\uD83D\uDE0D";
        //酷😎
        emoji[13] = "\uD83D\uDE0E";
        //挑嘴😏
        emoji[14] = "\uD83D\uDE0F";
        //直线嘴😐
        emoji[15] = "\uD83D\uDE10";
        //闭眼直线嘴😑
        emoji[16] = "\uD83D\uDE11";
        //滴汗😓
        emoji[17] = "\uD83D\uDE13";
        //撇嘴😕
        emoji[18] = "\uD83D\uDE15";
        //瓷嘴😖
        emoji[19] = "\uD83D\uDE16";
        //没嘴😗
        emoji[20] = "\uD83D\uDE17";
        //吐爱心😘
        emoji[21] = "\uD83D\uDE18";
        //嘟嘴😙
        emoji[22] = "\uD83D\uDE19";
        //可怜😞
        emoji[23] = "\uD83D\uDE1E";
        //生气无奈😨
        emoji[24] = "\uD83D\uDE28";
        //大哭😭
        emoji[25] = "\uD83D\uDE2D";
        //晚安😴
        emoji[26] = "\uD83D\uDE34";
        //到微笑🙃
        emoji[27] = "\uD83D\uDE43";
        //无语😒
        emoji[28] = "\uD83D\uDE12";
        //钱🤑
        emoji[29] = "\uD83E\uDD11";
        //闭嘴😷
        emoji[30] = "\uD83D\uDE37";
        //拜托🙏
        emoji[31] = "\uD83D\uDE4F";
        //眼睛👀
        emoji[32] = "\uD83D\uDC40";
        //吐气😮‍💨
        emoji[33] = "\uD83D\uDE2E\u200D\uD83D\uDCA8";

        return emoji;
    }

    public void getEmoji(List<JLabel> emojiList,String[] emojis, int initX, int initY, int width, int span, int bk_LR){
        int x = initX;
        int y = initY;
        for(String s : emojis){
            JLabel emojiLabel = new JLabel(s);
            emojiLabel.setFont(new Font(null,Font.PLAIN,40));
            emojiLabel.setBounds(x,y,50,30);
            emojiList.add(emojiLabel);
            if(x + emojiLabel.getWidth() < width - emojiLabel.getWidth()) {
                x += (span + emojiLabel.getWidth());
            }
            else {
                x = initX;
                y += (span + emojiLabel.getHeight());
            }

        }
    }

}
