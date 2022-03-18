/*
 * Created by JFormDesigner on Sun May 09 15:59:46 CST 2021
 */
package ChatClient.Swing.Frame;

import Utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 显示二维码界面
 */
public class RewardFrame extends JFrame {
    public RewardFrame(String qrCodePath) {
        initComponents(qrCodePath);
    }
    private void initComponents(String qrCodePath) {
       //获取图片的格式，设置JFrame大小   g.drawImage(background.getImage(), 0, 0,getWidth(),getHeight(), this);
        setBak(qrCodePath);
        JPanel jp = new JPanel(); // 创建个JPanel
        jp.setOpaque(false); // 把JPanel设置为透明 这样就不会遮住后面的背景
        setSize(300, 300);
        setLocation(800,350);
        setIconImage(new ImageIcon(ImageUtils.getImageUrl("8Icon.png")).getImage());
        setAlwaysOnTop(true);
        setVisible(false);
        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.add(jp);
        contentPane.setPreferredSize(new Dimension(500, 500));

        setVisible(true);
        contentPane.setLayout(null);
        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents

    }
    public void setBak(String qrCodePath) {
        ((JPanel) this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon(qrCodePath);
        JLabel background = new JLabel(img);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
