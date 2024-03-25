import org.json.JSONObject;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClonerSongs {

    public static void clonePlaylist() throws IOException, InterruptedException {
        if (!isCreatedPlaylist()) {
            if (!User.getPlaylistID().isEmpty()) {
                User.createPlaylistConfig(true);
            }
            createPlaylist();
            addSongs();
            return;
        }
        deleteSongs();
        addSongs();
    }

    public static void addSongs() throws IOException, InterruptedException {
        int offset = 0;
        int countFavSongs = Integer.parseInt(Request.get(User.getToken(), RequestOption.ALL_FAV_SONGS_COUNT));
        while (countFavSongs > offset) {
            Thread.sleep(500);
            Request.get(User.getToken(), RequestOption.FAV_PLAYLIST_ITEMS, offset, 50);
            Request.post(User.getToken(), RequestOption.ADD_ITEM, Paths.get(User.ADD_PATH));
            offset += 50;
        }
    }

    public static void deleteSongs() throws IOException, InterruptedException {
        int countCopiedSongs = Integer.parseInt(Request.get(User.getToken(), RequestOption.ALL_CLONED_SONGS_COUNT));
        while (countCopiedSongs > 0) {
            Thread.sleep(500);
            Request.get(User.getToken(), RequestOption.PLAYLIST_ITEMS, 0, 50);
            Request.delete(User.getToken(), RequestOption.DELETE_ITEM, Paths.get(User.DELETE_PATH));
            countCopiedSongs -= 50;
        }
    }

    public static void createPlaylist() throws IOException, InterruptedException {
        Path playlistConfigFile = Paths.get(User.PLAYLIST_CONFIG_PATH);

        Request.post(User.getToken(), RequestOption.CREATE_PLAYLIST, playlistConfigFile);

        BufferedReader br = new BufferedReader(new FileReader(User.USER_INFO_PATH));

        StringBuilder builder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            builder.append(line).append("\n");
        }
        JSONObject user = new JSONObject(builder.toString());
        user.put("playlistID", Request.get(User.getToken(), RequestOption.PLAYLIST_ID));

        FileWriter writer = new FileWriter(User.USER_INFO_PATH);
        writer.write(user.toString(4));
        writer.flush();
        writer.close();
    }

    public static boolean isCreatedPlaylist() throws IOException, InterruptedException {
        int offset = 0;
        int playlistsCount = Integer.parseInt(Request.get(User.getToken(), RequestOption.ALL_PLAYLISTS_COUNT));

        while(offset < playlistsCount) {
            System.out.println("Checking our playlist... " + offset + "/" + playlistsCount);
            if (Request.get(User.getToken(), RequestOption.PLAYLIST_ID) == null) {
                return false;
            }
            offset += 50;
        }
        System.out.print("Found playlist.\r\n");
        return true;
    }

}
