package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileWriter {

    public static void main(String[] args) {
        run(args[0]);
    }

    /*
     * Launcher
     */
    public static void run(String input) {
        Scanner sc = new Scanner(System.in);

        String s = "";
        while (!s.toLowerCase().equals("exit")) {

            System.out.println("Filename? ");
            s = sc.next();

            String string = input;

            File file = new File(s);
            writeFile(file, string);

        }

    }

    /**
     * Take input and a file and write to it
     *
     * @param file
     * @param lines
     */
    private static void writeFile(File file, String lines) {
        try {

            boolean result = file.createNewFile();

            if (result == false) {System.out.println("File exists already!"); return;}

            BufferedWriter bufferedWriter = new BufferedWriter(new java.io.FileWriter(file));

            bufferedWriter.write(lines);

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
