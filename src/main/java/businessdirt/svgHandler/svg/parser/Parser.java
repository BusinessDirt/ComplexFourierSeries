package businessdirt.svgHandler.svg.parser;

import businessdirt.svgHandler.svg.ComplexNumber;
import businessdirt.svgHandler.svg.path.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final String COMMANDS = "MmZzLlHhVvCcSsQqTtAa";
    private static final String UPPERCASE = "MZLHVCSQTA";
    private static final Pattern COMMAND_PATTERN = Pattern.compile("((?=[" + COMMANDS + "])|(?<=[" + COMMANDS + "]))");
    private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?[0-9]*[.]?[0-9]+(?:[eE][-+]?[0-9]+)?");


    private static List<String> tokenizePath(String path) {
        List<String> tokenized = new ArrayList<>();
        COMMAND_PATTERN.splitAsStream(path).forEach(command -> {
            if (COMMANDS.contains(command)) tokenized.add(command);
            else {
                Matcher m = FLOAT_PATTERN.matcher(command);
                while (m.find()) tokenized.add(m.group());
            }
        });
        return tokenized;
    }

    public static Path[] parsePaths(String[] paths) {
        Path[] parsed = new Path[paths.length];
        for (int i = 0; i < paths.length; i++) parsed[i] = parsePath(paths[i]);
        return parsed;
    }

    public static Path parsePath(String path) {
        List<String> elements = tokenizePath(path);
        Path segments = new Path();
        ComplexNumber startPos = null, currentPos = new ComplexNumber();
        String command = null, lastCommand = "";
        boolean absolute = false;

        while (!elements.isEmpty()) {
            if (COMMANDS.contains(elements.get(0))) {
                // new command
                lastCommand = command;
                command = elements.remove(0);
                absolute = UPPERCASE.contains(command);
                command = command.toUpperCase(Locale.ROOT);
            } else {
                if (command == null)
                    throw new RuntimeException("Unallowed implicit command in " + path + ", position " + (path.split("").length - elements.size()));
                lastCommand = command;
            }

            switch (command) {
                case "M":
                    // Moveto command
                    double x = Double.parseDouble(elements.remove(0));
                    double y = Double.parseDouble(elements.remove(0));
                    ComplexNumber pos = new ComplexNumber(x, y);
                    if (absolute)
                        currentPos = pos;
                    else
                        currentPos.add(pos);
                    segments.add(new Move(currentPos));
                    startPos = currentPos;
                    command = "L";
                    break;
                case "Z":
                    // close path
                    segments.add(new Close(currentPos, startPos));
                    currentPos = startPos;
                    startPos = null;
                    command = "";
                    break;
                case "L":
                    x = Double.parseDouble(elements.remove(0));
                    y = Double.parseDouble(elements.remove(0));
                    pos = new ComplexNumber(x, y);
                    if (!absolute) pos.add(currentPos);
                    segments.add(new Line(currentPos, pos));
                    currentPos = pos;
                    break;
                case "H":
                    x = Double.parseDouble(elements.remove(0));
                    pos = new ComplexNumber(x, currentPos.getImaginary());
                    if (!absolute) pos.add(currentPos.getReal());
                    segments.add(new Line(currentPos, pos));
                    currentPos = pos;
                    break;
                case "V":
                    y = Double.parseDouble(elements.remove(0));
                    pos = new ComplexNumber(currentPos.getReal(), y);
                    if (!absolute) pos.add(new ComplexNumber(0, currentPos.getImaginary()));
                    segments.add(new Line(currentPos, pos));
                    currentPos = pos;
                    break;
                case "C":
                    ComplexNumber control1 = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));
                    ComplexNumber control2 = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));
                    ComplexNumber end = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));

                    if (!absolute) {
                        control1.add(currentPos);
                        control2.add(currentPos);
                        end.add(currentPos);
                    }

                    segments.add(new CubicBezier(currentPos, control1, control2, end));
                    currentPos = end;
                    break;
                case "S":
                    // smooth curve
                    if ("CS".contains(lastCommand)) {
                        control1 = currentPos;
                    } else {
                        CubicBezier l = (CubicBezier) segments.getLast();
                        control1 = ComplexNumber.add(currentPos, ComplexNumber.subtract(currentPos, l.getControl2()));
                    }
                    control2 = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));
                    end = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));

                    if (!absolute) {
                        control2.add(currentPos);
                        end.add(currentPos);
                    }
                    segments.add(new CubicBezier(currentPos, control1, control2, end));
                    currentPos = end;
                    break;
                case "Q":
                    ComplexNumber control = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));
                    end = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));

                    if (!absolute) {
                        control.add(currentPos);
                        end.add(currentPos);
                    }

                    segments.add(new QuadraticBezier(currentPos, control, end));
                    currentPos = end;
                    break;
                case "T":
                    if ("QT".contains(lastCommand))
                        control = currentPos;
                    else {
                        QuadraticBezier l = (QuadraticBezier) segments.getLast();
                        control = ComplexNumber.add(currentPos, ComplexNumber.subtract(currentPos, l.getControl()));
                    }
                    end = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));

                    if (!absolute) end.add(currentPos);
                    segments.add(new QuadraticBezier(currentPos, control, end));
                    currentPos = end;
                    break;
                case "A":
                    ComplexNumber radius = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));
                    double rotation = Double.parseDouble(elements.remove(0));
                    boolean arc = Boolean.parseBoolean(elements.remove(0));
                    boolean sweep = Boolean.parseBoolean(elements.remove(0));
                    end = new ComplexNumber(Double.parseDouble(elements.remove(0)),
                            Double.parseDouble(elements.remove(0)));

                    if (!absolute) end.add(currentPos);
                    segments.add(new Arc(currentPos, radius, rotation, arc, sweep, end));
                    currentPos = end;
                    break;
            }
        }

        return segments;
    }

}
