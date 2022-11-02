package models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileLog {
    private BufferedWriter bufferedWriter;

    public FileLog(String nameFile) {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(nameFile, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String data) {
        try {
            bufferedWriter.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void close() {
        if (bufferedWriter != null) try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
