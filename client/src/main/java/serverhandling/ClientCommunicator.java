package serverhandling;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ClientCommunicator {

    public void doPost(String urlString, String body, String authToken) throws IOException, IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set HTTP request headers, if necessary
        // connection.addRequestProperty("Accept", "text/html");
        if (authToken != null){
            connection.addRequestProperty("Authorization", authToken);
        }

        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream();) {
            byte[] input = body.getBytes("utf-8");
            requestBody.write(input, 0, input.length);
        }

        // SERVER RETURNED AN HTTP ERROR
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            //connection.getHeaderField("Content-Length");

            try (InputStream responseBody = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                // Process the response
                System.out.println("Response: " + response.toString());
            }
        } else try (InputStream responseBody = connection.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            // Process the error response
            System.err.println("Error Response: " + response.toString());
        }
    }
}