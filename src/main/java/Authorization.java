import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Authorization {
    private static final String CLIENT_ID = "cb18dd90851c4cdbb12c12905aa51e30";
    private static final String CLIENT_SECRET = "b6ebc17b8a454eb3b5f2cf75fb48f5e1";
    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";

    protected static String requestAuthorizationCode() throws Exception {
            String url = AUTH_URL + "?client_id=" + CLIENT_ID +
                    "&response_type=code" +
                    "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8) +
                    "&scope=user-read-private%20user-read-email%20playlist-read-private%20" +
                    "user-library-read%20playlist-modify-public%20playlist-modify-private";
            Desktop d = Desktop.getDesktop();
            d.browse(new URI(url));
            System.out.println("Click on the button in the window that opens and then paste it here (CTRL + SHIT + V):");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return validateURL(reader.readLine().trim());
    }

    protected static String[] requestAccessAndRefreshTokens(String authCode) throws Exception {
        String encodedCredentials = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

        URL url = new URL(TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        String postData = "grant_type=authorization_code" +
                "&code=" + authCode +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8);
        connection.getOutputStream().write(postData.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        String jsonResponse = response.toString();
        String accessToken = jsonResponse.substring(jsonResponse.indexOf("\"access_token\":\"") + 16, jsonResponse.indexOf("\",\"token_type\""));
        String refreshToken = jsonResponse.substring(jsonResponse.indexOf("\"refresh_token\":\"") + 17, jsonResponse.indexOf("\",\"scope\""));

        return new String[]{accessToken, refreshToken};
    }

    public static String validateURL(String url) {
        return url.substring(url.indexOf("?code=") + 6);
    }
}
