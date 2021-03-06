package projects;

import utils.FileReader;
import utils.FileWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class CompA2StopLights {

    public static void main(String[] args) {
        new CompA2StopLights();
    }

    /**
     * Three stop process
     *
     * - Read the data file to get all ID's
     * - Search the graph for stoplight info about all ID's
     * - Parse the data together and output the file we created
     */
    public CompA2StopLights() {
        // open the file (presumably the data file)
        ArrayList<String> input = FileReader.run();

        input = parseData(input);

        System.out.println("LOADED FILE");

        // concat the file, inserting the stoplight data
        String text = "";
        for (String s : input) {
            text = text + s + " " + isThisAStopLight(s) + "\n";
        }

        FileWriter.run(text);

        System.out.println("CREATED OUTPUT DATA FILE");
    }


    /**
     * Takes in an array of Node ID's and locations and other values and just returns an array of Node ID's
     * @return
     */
    public static ArrayList<String> parseData(ArrayList<String> input) {

        ArrayList<String> newList = new ArrayList<>();

        // search the file
        for (String line : input) {
            Scanner scanner = new Scanner(line);

            // store only the ID's at the start of the lines
            newList.add("" + scanner.nextInt());
        }

        return newList;
    }

    /**
     * Takes an ID and finds it in the graph and returns whether it is a stoplight or not
     * @param id
     * @return
     */
    public static Integer isThisAStopLight(String id) {

        Integer numberID = Integer.parseInt(id);

        // TEST GRAPH //
        HashMap<Integer, Node> graph = new HashMap<>();
        graph.put(numberID, new Node());

        Node thisStop = graph.get(numberID);

        return thisStop.getRoadStopLightData();
    }

    /*
     * TEST TEMPLATE
     */
    static class Node {
        public Integer getRoadStopLightData() {
            Random r = new Random();
            return r.nextInt(3);
        }
    }
}
