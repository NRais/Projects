package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    /*
     * Launcher
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String s = prompt(sc);
        while (!s.toLowerCase().equals("exit")) {

            System.out.println(" " + readFile(new File(s)));

            s = prompt(sc);
        }

    }

    /**
     * Initialize a file-reader via console input
     *
     * @return
     */
    public static ArrayList<String> run() {

        Scanner sc = new Scanner(System.in);

        String s = prompt(sc);
        // prompt until they give a valid filename
        while (!(new File(s).exists())) {

            System.out.println("Invalid filename");
            s = prompt(sc);

        }

        File file = new File(s);
        return readFile(file);
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

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return theFilesContents;
    }

    /**
     * Prompt the user and return input
     *
     * @param sc
     * @return
     */
    private static String prompt(Scanner sc) {
        System.out.println("Filename to Read: ");
        return sc.nextLine();
    }
}
