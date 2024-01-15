import javax.swing.*;
import java.awt.*;

public class CustomJCheckBox extends JCheckBox {
    private Color nonSelectedColor;
    private Color selectedColor;
    private final int BORDER = 4;

    CustomJCheckBox(Color nonSelectedColor, Color selectedColor){
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setBackground(nonSelectedColor);
        this.nonSelectedColor = nonSelectedColor;
        this.selectedColor = selectedColor;
    }

    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
        Graphics2D g2D = (Graphics2D) graphics;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ly = (getHeight() - 16) / 2;

        if(isSelected()){
            g2D.setColor(Color.WHITE);
            g2D.fillRoundRect(1, ly, 16, 16, BORDER, BORDER);
            g2D.setColor(selectedColor);
            g2D.fillRoundRect(2, ly + 1, 14, 14, BORDER, BORDER);

            int px[] = {4, 8, 14, 12, 8, 6};
            int py[] = {ly + 8, ly + 14, ly + 5, ly + 3, ly + 10, ly + 6};

            g2D.setColor(Color.WHITE);
            g2D.fillPolygon(px, py, px.length);

        }
        else{
            g2D.setColor(Color.WHITE);
            g2D.fillRoundRect(1, ly, 16, 16, BORDER, BORDER);
            g2D.setColor(nonSelectedColor);
            g2D.fillRoundRect(2, ly + 1, 14, 14, BORDER, BORDER);

        }
        g2D.dispose();
    }
}
