package com.philstar.app.sendmatrix;

import com.cosium.matrix_communication_client.MatrixResources;
import com.cosium.matrix_communication_client.Message;
import com.cosium.matrix_communication_client.RoomResource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;


public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();

        if (args.length != 2) {
            System.err.println("SendMatrix sendmatrix.config [room id]");
            System.exit(0);
        }

        String confFile = args[0];
        String roomId = args[1];

        String matrixServer;
        String matrixUsername;
        String matrixPassword;

        try (FileInputStream fis = new FileInputStream(confFile)) {
            properties.load(fis);

            matrixServer = properties.getProperty("matrix.server");
            matrixUsername = properties.getProperty("matrix.username");
            matrixPassword = properties.getProperty("matrix.password");

            MatrixResources matrix =
                    MatrixResources.factory()
                            .builder()
                            .https()
                            .hostname(matrixServer)
                            .defaultPort()
                            .usernamePassword(matrixUsername, matrixPassword)
                            .build();

            RoomResource room = matrix
                    .rooms()
                    .byId(roomId);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            List<String> lines = reader.lines().toList();

            String message = String.join("\n", lines);
//            String formattedMessage = String.join("<br />", lines); // Spaces too wide on Element X

            room.sendMessage(Message.builder().body(message).formattedBody(null).build());

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}