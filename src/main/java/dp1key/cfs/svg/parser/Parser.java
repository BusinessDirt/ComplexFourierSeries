package dp1key.cfs.svg.parser;

import dp1key.cfs.svg.path.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        double startPos = 0;
        String command = null, lastCommand = null;
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
                    break;
                case "Z":
                    break;
                case "L":
                    break;
                case "H":
                    break;
                case "V":
                    break;
                case "C":
                    break;
                case "S":
                    break;
                case "Q":
                    break;
                case "T":
                    break;
                case "A":
                    break;
            }
        }

        return null;
    }

}
