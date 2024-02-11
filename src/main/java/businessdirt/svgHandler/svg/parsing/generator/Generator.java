package businessdirt.svgHandler.svg.parsing.generator;

import businessdirt.svgHandler.svg.parsing.parser.AttributeNode;
import businessdirt.svgHandler.svg.parsing.parser.IdentifierNode;
import businessdirt.svgHandler.svg.parsing.parser.Parser;
import businessdirt.svgHandler.svg.path.*;
import com.vm.jcomplex.Complex;

import java.util.Locale;

public class Generator {

    private Path path;
    private final Parser parser;
    
    public Generator(Parser parser) {
        this.parser = parser;
        this.parsePath();
    }

    private void parsePath() {
        this.path = new Path();
        Complex pos, startPos = null, currentPos = new Complex(0, 0);
        Complex control1, control2, end;
        String command = "", lastCommand;
        boolean absolute = false;
        AttributeNode attr = null;

        while (!this.parser.isEmpty()) {
            if (this.parser.get(0) instanceof IdentifierNode
                    && (this.path.isEmpty() || (attr != null && attr.isEmpty()))) {
                // new command
                lastCommand = command;
                IdentifierNode identifierNode = (IdentifierNode) this.parser.remove(0);
                command = identifierNode.getValue();
                absolute = identifierNode.absolute;

                if (!"zZ".contains(command))
                    attr = (AttributeNode) this.parser.remove(0);
            } else {
                if (command.isEmpty()) throw new RuntimeException("Unallowed implicit command");
                lastCommand = command;
            }

            switch (command) {
                case "M":
                    // Moveto command
                    pos = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());
                    currentPos = absolute ? pos : currentPos.add(pos);
                    this.path.add(new Move(currentPos));
                    startPos = currentPos;
                    command = "L";
                    break;

                case "Z":
                    // close path
                    this.path.add(new Close(currentPos, startPos));
                    currentPos = startPos;
                    startPos = null;
                    command = "";
                    break;

                case "L":
                    pos = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());
                    if (!absolute) pos = pos.add(currentPos);
                    this.path.add(new Line(currentPos, pos));
                    currentPos = pos;
                    break;

                case "H":
                    pos = new Complex(attr.remove(0).asDouble(), currentPos.getImaginary());
                    if (!absolute) pos = pos.add(currentPos.getReal());
                    this.path.add(new Line(currentPos, pos));
                    currentPos = pos;
                    break;

                case "V":
                    pos = new Complex(currentPos.getReal(), attr.remove(0).asDouble());
                    if (!absolute) pos = pos.add(new Complex(0, currentPos.getImaginary()));
                    this.path.add(new Line(currentPos, pos));
                    currentPos = pos;
                    break;

                case "C":
                    control1 = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());
                    control2 = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());
                    end = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());

                    if (!absolute) {
                        control1 = control1.add(currentPos);
                        control2 = control2.add(currentPos);
                        end = end.add(currentPos);
                    }

                    this.path.add(new CubicBezier(currentPos, control1, control2, end));
                    currentPos = end;
                    break;

                case "S":
                    // smooth curve
                    if ("CS".contains(lastCommand)) {
                        control1 = currentPos;
                    } else {
                        CubicBezier l = (CubicBezier) this.path.getLast();
                        control1 = currentPos.add(currentPos.subtract(l.getControl2()));
                    }
                    control2 = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());
                    end = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());

                    if (!absolute) {
                        control2 = control2.add(currentPos);
                        end = end.add(currentPos);
                    }

                    this.path.add(new CubicBezier(currentPos, control1, control2, end));
                    currentPos = end;
                    break;

                case "Q":
                    control1 = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());
                    end = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());

                    if (!absolute) {
                        control1 = control1.add(currentPos);
                        end = end.add(currentPos);
                    }

                    this.path.add(new QuadraticBezier(currentPos, control1, end));
                    currentPos = end;
                    break;

                case "T":
                    if ("QT".contains(lastCommand))
                        control1 = currentPos;
                    else {
                        QuadraticBezier l = (QuadraticBezier) this.path.getLast();
                        control1 = currentPos.add(currentPos.subtract(l.getControl()));
                    }
                    end = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());

                    if (!absolute) end = end.add(currentPos);
                    this.path.add(new QuadraticBezier(currentPos, control1, end));
                    currentPos = end;
                    break;

                case "A":
                    Complex radius = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());
                    double rotation = attr.remove(0).asDouble();
                    boolean arc = attr.remove(0).asBoolean();
                    boolean sweep = attr.remove(0).asBoolean();
                    end = new Complex(attr.remove(0).asDouble(), attr.remove(0).asDouble());

                    if (!absolute) end = end.add(currentPos);
                    this.path.add(new Arc(currentPos, radius, rotation, arc, sweep, end));
                    currentPos = end;
                    break;
            }
        }
    }

    public Path path() {
        return this.path;
    }
}
