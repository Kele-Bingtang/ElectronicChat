package ChatClient.Swing.Frame;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;

/**
 * 重写下拉列表的UI
 */
public class MyComBoxUI extends MetalComboBoxUI {

    @Override
    public void configureArrowButton() {
        super.configureArrowButton();
        if (arrowButton != null) {
            arrowButton.setBorder(BorderFactory.createEmptyBorder());
        }
    }

}

/**
 * 更改选择项的前景色，背景色
 */
class MyComBoxUIBackground extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
        return super.createArrowButton();
    }

    @Override
    public void installUI(JComponent comboBox) {
        super.installUI(comboBox);
        listBox.setForeground(Color.BLACK);
        listBox.setSelectionBackground(Color.LIGHT_GRAY);
        listBox.setSelectionForeground(Color.BLACK);

    }


}

class MyCellRenderer extends JLabel implements ListCellRenderer {
    public MyCellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        setText(value.toString());

        Color background;
        Color foreground;

        // check if this cell represents the current DnD drop location
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            background = Color.BLUE;
            foreground = Color.WHITE;

            // check if this cell is selected
        } else if (isSelected) {
            background = Color.LIGHT_GRAY;
            foreground = Color.WHITE;

            // unselected, and not the DnD drop location
        } else {
            background = Color.WHITE;
            foreground = Color.BLACK;
        };

        setBackground(background);
        setForeground(foreground);

        return this;
    }
}