package dp1key.cfs;

import dp1key.cfs.svg.ComplexNumber;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.LinkedList;
import java.util.List;

public class Draw extends JLabel {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<ComplexNumber> complexNumberList;
    private boolean drawLines;

    public Draw() {
        complexNumberList = new LinkedList<>();
        drawLines = false;
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < complexNumberList.size(); i++) {
            if (!drawLines)
                g2d.drawOval((int) this.complexNumberList.get(i).getReal(), (int) this.complexNumberList.get(i).getImaginary(), 2, 2);
            else
                drawLines(g2d, i);
        }

        repaint();
    }

    private void drawLines(Graphics2D g2d, int i) {
        if (i == this.complexNumberList.size() - 1) {
            drawLinesHelper(g2d, i, 0);
            return;
        }
        drawLinesHelper(g2d, i, i+1);
    }

    private void drawLinesHelper(Graphics2D g2d, int i1, int i2) {
        ComplexNumber z1 = this.complexNumberList.get(i1);
        ComplexNumber z2 = this.complexNumberList.get(i2);
        g2d.drawLine((int) z1.getReal(), (int) z1.getImaginary(), (int) z2.getReal(), (int) z2.getImaginary());
    }

    public void drawComplexNumber(ComplexNumber z) {
        this.complexNumberList.add(z);
    }

    public void setDrawLines(boolean drawLines) {
        this.drawLines = drawLines;
    }
}