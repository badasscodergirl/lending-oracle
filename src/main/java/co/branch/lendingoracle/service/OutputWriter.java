package co.branch.lendingoracle.service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OutputWriter {

    public void writeOutput(List<Integer> output, String path) {
        try {
            Path filePath = Paths.get(path);
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);

            try(FileWriter fw = new FileWriter(path)) {
                for(int outValue: output) {
                    fw.write(outValue+"\n");
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while creating output file.");
            e.printStackTrace();
        }

    }
}
