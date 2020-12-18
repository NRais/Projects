package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public void connect(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        System.out.println("Starting server'");

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {

            String inputLine, outputLine;

            // Initiate conversation with client
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));

            out.println("--- Conversation Begins ---");

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client: " + inputLine);

                if (inputLine.equals("Bye."))
                    break;

                outputLine = stdIn.readLine();

                if (outputLine != null) {
                    //System.out.println("Server: " + outputLine);
                    out.println(outputLine);

                    if (outputLine.equals("Bye."))
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
