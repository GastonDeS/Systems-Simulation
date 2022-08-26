package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    public static List<String> parse (String path) throws FileNotFoundException {
        List<String> tokens = new ArrayList<>();
        File file = new File(path);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\n");
        while (sc.hasNext()) {
            tokens.add(sc.next().split("=")[1].trim());
        }
        return tokens;
    }
}
