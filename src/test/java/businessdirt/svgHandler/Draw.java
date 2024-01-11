package businessdirt.svgHandler;

import com.vm.jcomplex.Complex;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.LinkedList;
import java.util.List;

public class Draw extends JLabel {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Figure> figures;
    private boolean drawLines;

    public Draw() {
        figures = new LinkedList<>();
        drawLines = false;
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLUE);
        for (Figure f : this.figures) f.draw(g2d, this.drawLines);

        repaint();
    }

    public void drawFigure(Figure f) {
        this.figures.add(f);
    }

    public void setDrawLines(boolean drawLines) {
        this.drawLines = drawLines;
    }

    public static class Figure {

        private final List<Complex> points;

        public Figure() {
            this.points = new LinkedList<>();
        }

        public void add(Complex z) {
            this.points.add(z);
        }

        public void draw(Graphics2D g2d, boolean drawLines) {
            for (int i = 0; i < this.points.size(); i++) {
                if (drawLines) {
                    drawLines(g2d, i);
                    continue;
                }
                g2d.drawOval((int) this.points.get(i).getReal(), (int) this.points.get(i).getImaginary(), 2, 2);
            }
        }

        private void drawLines(Graphics2D g2d, int i) {
            int i2 = i == this.points.size() - 1 ? 0 : i + 1;
            Complex z1 = this.points.get(i), z2 = this.points.get(i2);
            g2d.drawLine((int) z1.getReal(), (int) z1.getImaginary(), (int) z2.getReal(), (int) z2.getImaginary());
        }
    }
}