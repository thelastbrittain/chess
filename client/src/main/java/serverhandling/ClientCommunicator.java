package serverhandling;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ClientCommunicator {

    public String doPost(String urlString, String body, String authToken) throws IOException, IOException {
        System.out.println("In do post method");
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set HTTP request headers, if necessary
        // connection.addRequestProperty("Accept", "text/html");
        if (authToken != null){
            connection.addRequestProperty("Authorization", authToken);
            System.out.println("Added header");
        }

        connection.connect();
        System.out.println("Connection made");

        try (OutputStream requestBody = connection.getOutputStream();) {
            byte[] input = body.getBytes("utf-8");
            requestBody.write(input, 0, input.length);
            System.out.println("Body sent to server");
        }

        // SERVER RETURNED AN HTTP ERROR
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            System.out.println("Connection success 200");
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
                return response.toString();
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
            return "Post Method Failed";
        }
    }
}