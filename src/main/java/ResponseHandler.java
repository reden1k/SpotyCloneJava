import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ResponseHandler {
    public static String status(HttpResponse<String> response, String successReq, String badReq) {
        return switch (response.statusCode()) {
            case 200, 201 ->
                    "\u001B[32m" + successReq + "\u001B[0m" + '\n';
            case 403, 400 ->
                    throw new RuntimeException("\u001B[31m" + badReq + "\u001B[0m" + '\n');
            default -> "";
        };
    }

    public static String status(HttpResponse<String> response) {
        return switch (response.statusCode()) {
            case 200 -> "\u001B[32mSuccessful request! HTTP " + response.statusCode() + " OK " + "\u001B[0m" + '\n';
            case 201 ->
                    "\u001B[32mSuccessful request! HTTP " + response.statusCode() + " Created playlist!" + "\u001B[0m" + '\n';
            case 403 ->
                    throw new RuntimeException("\u001B[31mBad request: HTTP " + response.statusCode() + "\nIf you from blocked region, try again with VPN!" + "\u001B[0m" + '\n');
            case 400 ->
                    throw new RuntimeException("\u001B[31mBad request: HTTP " + response.statusCode() + "\nSomething wrong with template of request." + "\u001B[0m" + '\n');
            default -> "";
        };
    }

    public static String executeEnumActionString(HttpResponse<String> response, RequestOption option) throws IOException {
        switch (option) {
            case PLAYLIST_ID:
                return getPlaylistID(new JSONObject(response.body()).getJSONArray("items"));
            case USER_ID:
                return new JSONObject(response.body()).optString("id");
            case ALL_PLAYLISTS_COUNT, ALL_FAV_SONGS_COUNT, ALL_CLONED_SONGS_COUNT:
                return new JSONObject(response.body()).optString("total");
            case FAV_PLAYLIST_ITEMS:
                createAddFilesJSON(new JSONObject(response.body()).getJSONArray("items"));
            case PLAYLIST_ITEMS:
                createDeleteFilesJSON(new JSONObject(response.body()).getJSONArray("items"));
            default:
                return null;
        }
    }

    private static String getPlaylistID(JSONArray array) throws IOException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject playlist = array.getJSONObject(i);
            if (playlist.optString("name").equals(User.getPlaylistName())) {
                 return playlist.optString("id");
            }
        }
        return null;
    }

    private static void createAddFilesJSON(JSONArray array) throws IOException {
        JSONObject add = new JSONObject();
        List<String> uris = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject song = array.getJSONObject(i);
            uris.add(song.getJSONObject("track").optString("uri"));
        }
        add.put("uris", uris);
        FileWriter writer = new FileWriter("configuration/add.json");
        writer.write(add.toString(4));
        writer.flush();
        writer.close();
    }

    private static void createDeleteFilesJSON(JSONArray array) throws IOException {
        JSONObject delete = new JSONObject();
        List<JSONObject> tracks = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject song = array.getJSONObject(i);
            JSONObject uri = new JSONObject();
            uri.put("uri", song.getJSONObject("track").optString("uri"));
            tracks.add(uri);
        }
        delete.put("tracks", tracks);

        FileWriter writer = new FileWriter("configuration/delete.json");
        writer.write(delete.toString(4));
        writer.flush();
        writer.close();
    }
}
