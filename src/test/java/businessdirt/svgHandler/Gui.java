package businessdirt.svgHandler;

import businessdirt.svgHandler.svg.ComplexNumber;

import javax.swing.*;
import java.awt.*;

public class Gui {

    private final Draw draw;

    private static final int width = 1280;
    private static final int height = 780;

    public Gui() {
        JFrame jf = new JFrame();
        jf.setSize(width,height);
        jf.setResizable(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setMinimumSize(new Dimension(width, height));
        jf.setTitle("SVGPath");

        this.draw = new Draw();
        jf.setBounds(0, 0, width, height);
        this.draw.setVisible(true);
        jf.add(this.draw);

        jf.setVisible(true);
    }

    public void drawFigure(Draw.Figure f) {
        this.draw.drawFigure(f);
    }

    public void setDrawLines(boolean drawLines) {
        this.draw.setDrawLines(drawLines);
    }
}