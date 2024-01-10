package businessdirt.svgHandler.svg.parser;

import businessdirt.svgHandler.svg.path.*;
import com.vm.jcomplex.Complex;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final String COMMANDS = "MmZzLlHhVvCcSsQqTtAa";
    private static final String UPPERCASE = "MZLHVCSQTA";
    private static final Pattern COMMAND_PATTERN = Pattern.compile("((?=[" + Parser.COMMANDS + "])|(?<=[" + Parser.COMMANDS + "]))");
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?[0-9]*[.]?[0-9]+(?:[eE][-+]?[0-9]+)?");

    private final String path;
    private final List<String> tokenized;
    private final Path segments;


    public Parser(String path) {
        this.path = path;

        // split the path into its components
        // every command and every double value gets its own array slot
        this.tokenized = new ArrayList<>();
        Parser.COMMAND_PATTERN.splitAsStream(this.path).forEach(command -> {
            if (Parser.COMMANDS.contains(command)) this.tokenized.add(command);
            else {
                Matcher doubleMatcher = Parser.DOUBLE_PATTERN.matcher(command);
                while (doubleMatcher.find()) this.tokenized.add(doubleMatcher.group());
            }
        });

        this.segments = new Path();
        this.parsePath();
    }

    private Parser(String path, List<String> tokenized, Path segments) {
        this.path = path;
        this.tokenized = tokenized.subList(0, tokenized.size());
        this.segments = segments.clone();
    }

    public static Path[] parsePaths(String[] paths) {
        Path[] parsed = new Path[paths.length];
        for (int i = 0; i < paths.length; i++) {
            parsed[i] = new Parser(paths[i]).path();
        }
        return parsed;
    }

    private void parsePath() {
        Complex pos, startPos = null, currentPos = new Complex(0, 0);
        Complex control1, control2, end;
        String command = "", lastCommand;
        boolean absolute = false;

        while (!this.tokenized.isEmpty()) {
            if (Parser.COMMANDS.contains(this.tokenized.get(0))) {
                // new command
                lastCommand = command;
                command = this.tokenized.remove(0);
                absolute = Parser.UPPERCASE.contains(command);
                command = command.toUpperCase(Locale.ROOT);
            } else {
                if (command.isEmpty()) throw new RuntimeException(
                        "Unallowed implicit command in " + this.path + ", position " + (this.path.split("").length - this.tokenized.size())
                );
                lastCommand = command;
            }

            switch (command) {
                case "M":
                    // Moveto command
                    pos = this.fromTokenized();
                    currentPos = absolute ? pos : currentPos.add(pos);
                    this.segments.add(new Move(currentPos));
                    startPos = currentPos;
                    command = "L";
                    break;

                case "Z":
                    // close path
                    this.segments.add(new Close(currentPos, startPos));
                    currentPos = startPos;
                    startPos = null;
                    command = "";
                    break;

                case "L":
                    pos = this.fromTokenized();
                    if (!absolute) pos = pos.add(currentPos);
                    this.segments.add(new Line(currentPos, pos));
                    currentPos = pos;
                    break;

                case "H":
                    pos = new Complex(Double.parseDouble(this.tokenized.remove(0)), currentPos.getImaginary());
                    if (!absolute) pos = pos.add(currentPos.getReal());
                    this.segments.add(new Line(currentPos, pos));
                    currentPos = pos;
                    break;

                case "V":
                    pos = new Complex(currentPos.getReal(), Double.parseDouble(this.tokenized.remove(0)));
                    if (!absolute) pos = pos.add(new Complex(0, currentPos.getImaginary()));
                    this.segments.add(new Line(currentPos, pos));
                    currentPos = pos;
                    break;

                case "C":
                    control1 = this.fromTokenized();
                    control2 = this.fromTokenized();
                    end = this.fromTokenized();

                    if (!absolute) {
                        control1 = control1.add(currentPos);
                        control2 = control2.add(currentPos);
                        end = end.add(currentPos);
                    }

                    this.segments.add(new CubicBezier(currentPos, control1, control2, end));
                    currentPos = end;
                    break;

                case "S":
                    // smooth curve
                    if ("CS".contains(lastCommand)) {
                        control1 = currentPos;
                    } else {
                        CubicBezier l = (CubicBezier) segments.getLast();
                        control1 = currentPos.add(currentPos.subtract(l.getControl2()));
                    }
                    control2 = this.fromTokenized();
                    end = this.fromTokenized();

                    if (!absolute) {
                        control2 = control2.add(currentPos);
                        end = end.add(currentPos);
                    }

                    this.segments.add(new CubicBezier(currentPos, control1, control2, end));
                    currentPos = end;
                    break;

                case "Q":
                    control1 = this.fromTokenized();
                    end = this.fromTokenized();

                    if (!absolute) {
                        control1 = control1.add(currentPos);
                        end = end.add(currentPos);
                    }

                    this.segments.add(new QuadraticBezier(currentPos, control1, end));
                    currentPos = end;
                    break;

                case "T":
                    if ("QT".contains(lastCommand))
                        control1 = currentPos;
                    else {
                        QuadraticBezier l = (QuadraticBezier) segments.getLast();
                        control1 = currentPos.add(currentPos.subtract(l.getControl()));
                    }
                    end = this.fromTokenized();

                    if (!absolute) end = end.add(currentPos);
                    this.segments.add(new QuadraticBezier(currentPos, control1, end));
                    currentPos = end;
                    break;

                case "A":
                    Complex radius = this.fromTokenized();
                    double rotation = Double.parseDouble(this.tokenized.remove(0));
                    boolean arc = Boolean.parseBoolean(this.tokenized.remove(0));
                    boolean sweep = Boolean.parseBoolean(this.tokenized.remove(0));
                    end = this.fromTokenized();

                    if (!absolute) end = end.add(currentPos);
                    this.segments.add(new Arc(currentPos, radius, rotation, arc, sweep, end));
                    currentPos = end;
                    break;
            }
        }
    }

    private Complex fromTokenized() {
        return new Complex(Double.parseDouble(this.tokenized.remove(0)),
                Double.parseDouble(this.tokenized.remove(0)));
    }

    public Path path() {
        return this.segments;
    }

    @Override
    public Parser clone() {
        return new Parser(this.path, this.tokenized, this.segments.clone());
    }
}
