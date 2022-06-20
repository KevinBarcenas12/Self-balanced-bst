package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {
    private final Scanner reader;

    public FileManager(String pathToFile) throws FileNotFoundException {
        File file = new File(pathToFile);
        reader = new Scanner(file);
    }

    public List<Integer> getInputs() {
        List<Integer> inputs = new ArrayList<>();
        String firstLine = reader.nextLine();
        while (firstLine.startsWith("#") || firstLine.length() < 1) firstLine = reader.nextLine();
        if (firstLine.contains(",")) {
            String[] elems = firstLine.split(",");
            for (String elem : elems) inputs.add(Integer.parseInt(elem.trim()));
        }
        else if (firstLine.contains(" ")) {
            String[] elems = firstLine.trim().split(" ");
            for (String elem : elems) inputs.add(Integer.parseInt(elem.trim()));
        }
        else {
            inputs.add(Integer.parseInt(firstLine.trim()));
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (!line.startsWith("#")) {
                    int num = Integer.parseInt(line.trim());
                    if (!inputs.contains(num)) inputs.add(num);
                }
            }
        }
        return inputs;
    }
}
