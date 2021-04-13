package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileWriter {

    /*
     * Launcher
     */
    public static void main(String[] args) {

        run(args[0]);

    }

    /**
     * Initialize a file-writer via console input
     *
     * @param input
     */
    public static void run(String input) {
        Scanner sc = new Scanner(System.in);

        String s = prompt(sc); // get file to write to

        if ( writeFile(new File(s), input) )
            System.out.println("WRITING DATA: <-----" + input + "----->");
        else
            System.out.println("ERROR WRITING");
    }

    /**
     * Take input and a file and write to it
     *
     * @param file
     * @param lines
     */
    private static boolean writeFile(File file, String lines) {
        try {

            boolean result = file.createNewFile();

            if (result == false) {System.out.println("File exists already!"); return false;}

            BufferedWriter bufferedWriter = new BufferedWriter(new java.io.FileWriter(file));

            bufferedWriter.write(lines);

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Prompt the user to input a filename
     *
     * @param sc
     * @return
     */
    private static String prompt(Scanner sc) {
        System.out.println("Filename to Write: ");
        return sc.nextLine();
    }

}
