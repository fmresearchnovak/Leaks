package edu.fandm.enovak.leaks;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;

import java.net.HttpURLConnection;

public class HTMLGetter {

    public static void main(String[] args) {

        String site = "https://www.ednovak.net";

        try {
            URL url = new URL(site);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5 seconds
            connection.setReadTimeout(5000); // 5 seconds

            //int responseCode = connection.getResponseCode();
            int responseCode = Wrapping.getResponseCode(connection);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder html = new StringBuilder();
                    //while ((line = reader.readLine()) != null) {
                    while ((line = Wrapping.readLine(reader)) != null) {
                        html.append(line).append("\n"); // Append each line
                    }
                    System.out.println("Downloaded HTML from: " + site);
                    //System.out.println(html.toString()); // Print the HTML
                }
            } else {
                System.out.println("Failed to download HTML. Response code: " + responseCode);
            }

            connection.disconnect();

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }
}
