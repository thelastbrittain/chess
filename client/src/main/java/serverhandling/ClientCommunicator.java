package serverhandling;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ClientCommunicator {

    public String doPost(String urlString, String body, String authToken) throws IOException, IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString, authToken, "POST");

        try (OutputStream requestBody = connection.getOutputStream();) {
            byte[] input = body.getBytes("utf-8");
            requestBody.write(input, 0, input.length);
            System.out.println("Body sent to server");
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            System.out.println("Connection success 200");

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

    private void sendRequest(HttpURLConnection connect, String body){

    }

    public void doDelete(String urlString, String authToken) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString, authToken, "DELETE");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
        } else {
            try (InputStream responseBody = connection.getErrorStream();
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

    private static HttpURLConnection getHttpURLConnection(String urlString, String authToken, String requestMethod) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(true); // If you need to send a body with the DELETE request

        // Set HTTP request headers
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");

        // Add Authorization header if authToken is provided
        if (authToken != null) {
            connection.addRequestProperty("Authorization", authToken);
        }

        connection.connect();
        return connection;
    }
}