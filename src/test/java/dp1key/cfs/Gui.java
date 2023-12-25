package dp1key.cfs;

import dp1key.cfs.svg.ComplexNumber;
import dp1key.cfs.svg.parser.Parser;
import dp1key.cfs.svg.parser.XMLHandler;
import dp1key.cfs.svg.path.Path;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;

public class Gui {

    private final JFrame jf;
    private final Draw draw;

    private static final int width = 1280;
    private static final int height = 780;

    public Gui() {
        jf = new JFrame();
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

    public JFrame getJf() {
        return jf;
    }

    public void drawComplexNumber(ComplexNumber z) {
        this.draw.drawComplexNumber(z);
    }

    public void setDrawLines(boolean drawLines) {
        this.draw.setDrawLines(drawLines);
    }

    public int getWidth() {
        return jf.getWidth();
    }
    public int getHeight() {
        return jf.getHeight();
    }
}