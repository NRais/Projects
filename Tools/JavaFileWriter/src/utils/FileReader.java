package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    public static void main(String[] args) {
        run();
    }

    /*
     * Launcher
     */
    public static ArrayList<String> run() {
        ArrayList<String> lines = new ArrayList<>();

        Scanner sc = new Scanner(System.in);

        String s = "";
        while (!s.toLowerCase().equals("exit")) {

            System.out.println("Filename? ");
            s = sc.next();

            File file = new File(s);
            lines = readFile(file);

        }

        return lines;
    }

    /**
     * Read file line by line return an arraylist to represent the file
     *
     * @param file
     * @return
     */
    private static ArrayList<String> readFile(File file) {
        ArrayList<String> theFilesContents = new ArrayList<>();

        try {

            BufferedReader reader = new BufferedReader(new java.io.FileReader(file));

            String line = reader.readLine();
            while (line != null) {
                theFilesContents.add(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return theFilesContents;
    }
}
