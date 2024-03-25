import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class User {
    public static final String PLAYLIST_CONFIG_PATH = "configuration/playlistConfig.json";
    public static final String USER_INFO_PATH = "configuration/userInfo.json";
    public static final String DELETE_PATH = "configuration/delete.json";
    public static final String ADD_PATH = "configuration/add.json";

    public static void createConfig() throws Exception { //TODO After reset token, recreate only TOKEN! NOT WHOLE FILE!
        if (!isCreatedConfig()) {
            createInfoFile();
            createPlaylistConfig(true);
        }
        if (!isValidToken()) {
            LocalServer.start();

            String authCode = Authorization.requestAuthorizationCode();
            String[] tokens = Authorization.requestAccessAndRefreshTokens(authCode);
            writeAuthorizationInfo(tokens[0], tokens[1]); // [0] -> accessToken [1] -> refreshToken

            LocalServer.close();
        }
    }

    private static void createInfoFile() throws IOException {
        Path folderPath = Paths.get("configuration");
        Path userInfo = folderPath.resolve("userInfo.json");
        JSONObject user = new JSONObject();
        Files.createDirectory(folderPath);
        Files.createFile(userInfo);

        FileWriter writer = new FileWriter(USER_INFO_PATH);

        user.put("accessToken", "");
        user.put("refreshToken", "");
        user.put("tokenTimestamp", 0);
        user.put("playlistID", "");

        writer.write(user.toString(4));
        writer.flush();
        writer.close();
    }

    private static void writeAuthorizationInfo(String accessToken, String refreshToken) throws IOException, InterruptedException {
        JSONObject userInfo = new JSONObject();
        FileWriter writer = new FileWriter(USER_INFO_PATH);
        userInfo.put("accessToken", accessToken);
        userInfo.put("refreshToken", refreshToken);
        userInfo.put("tokenTimestamp", System.currentTimeMillis());
        userInfo.put("userID", Request.get(accessToken, RequestOption.USER_ID));

        writer.write(userInfo.toString(4));
        writer.flush();
        writer.close();
    }

    public static void createPlaylistConfig(boolean isDeleted) throws IOException {
        if (reader(USER_INFO_PATH, "playlistID").isEmpty() || isDeleted) {
            Path playlistConfigPath = Paths.get(PLAYLIST_CONFIG_PATH);
            JSONObject playlist = new JSONObject();

            System.out.print("Type playlist name (Tap \"Enter\" for default name): ");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();

            if (name.isEmpty() || name.isBlank()) {
                name = "SpotyClone";
            }

            playlist.put("name", name);
            playlist.put("description", "SpotyClone (made by redeN1k)");
            playlist.put("public", false);

            if (!Files.exists(playlistConfigPath)) {
                Files.createFile(playlistConfigPath);
            }
            FileWriter writer = new FileWriter(PLAYLIST_CONFIG_PATH);
            writer.write(playlist.toString(4));
            writer.flush();
            writer.close();
        }
    }

    public static boolean isValidToken() throws IOException {
        File userInfo = new File(USER_INFO_PATH);

        FileReader fileReader = new FileReader(userInfo);
        BufferedReader br = new BufferedReader(fileReader);

        StringBuilder builder = new StringBuilder();
        String line;
        while (( line = br.readLine()) != null) {
            builder.append(line).append("\n");
        }
        JSONObject user = new JSONObject(builder.toString());

        return (System.currentTimeMillis() - user.optLong("tokenTimestamp")) < 3600000;
    }

    private static boolean isCreatedConfig() throws IOException {
        Path folderPath = Paths.get("configuration");
        Path userInfo = folderPath.resolve("userInfo.json");
        return Files.exists(folderPath) && Files.isDirectory(folderPath) && Files.exists(userInfo);
    }

    public static String getToken() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(USER_INFO_PATH));

        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return new JSONObject(builder.toString()).optString("accessToken");
    }

    public static String getId() throws IOException, InterruptedException {
         return reader(USER_INFO_PATH, "userID");
    }

    public static String getPlaylistName() throws IOException {
        return reader(PLAYLIST_CONFIG_PATH, "name");
    }

    public static String getPlaylistID() throws IOException {
        return reader(USER_INFO_PATH, "playlistID");
    }

    private static String reader(String path, String what) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));

            StringBuilder builder = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                builder.append(line).append("\n");
            }

            return new JSONObject(builder.toString()).optString(what);
    }

    public static void deleteTemporaryFiles() throws IOException {
        Path[] paths = {Paths.get(DELETE_PATH), Paths.get(ADD_PATH)};
        if (Files.exists(paths[0])) {
            Files.delete(paths[0]);
        }
        if (Files.exists(paths[1])) {
            Files.delete(paths[1]);
        }
    }
}
