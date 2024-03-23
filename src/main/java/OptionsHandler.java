import java.io.IOException;
import java.net.URI;

public class OptionsHandler {

    private static final String PLAYLISTS_ENDPOINT = "https://api.spotify.com/v1/me/playlists"; //TODO CHANGE NAME TO CORRECT!
    private static final String PLAYLIST_ITEMS_ENDPOINT = "https://api.spotify.com/v1/playlists/"+ "PLAYLIST_ID" + "/tracks"; //TODO CHANGE NAME TO CORRECT!
    private static final String ADD_ITEM_ENDPOINT = "https://api.spotify.com/v1/playlists/"+ "PLAYLIST_ID" +"/tracks"; //TODO CHANGE NAME TO CORRECT!
    private static final String DELETE_ITEM_ENDPOINT = "https://api.spotify.com/v1/playlists/"+ "PLAYLIST_ID" +"/tracks"; //TODO CHANGE NAME TO CORRECT!
    private static final String CREATE_PLAYLIST_ENDPOINT = "https://api.spotify.com/v1/users/"+ "USER_ID" +"/playlists"; //TODO CHANGE NAME TO CORRECT!
    public static String USER_ID_ENDPOINT = "https://api.spotify.com/v1/me"; //TODO CHANGE NAME TO CORRECT!
    private static final String PLAYLIST_ID_ENDPOINT = "https://api.spotify.com/v1/users/"+ "USER_ID" +"/playlists"; //TODO CHANGE NAME TO CORRECT!
    private static final String ALL_FAV_SONGS_ENDPOINT = "https://api.spotify.com/v1/me/tracks"; //TODO CHANGE NAME TO CORRECT!
    private static final String ALL_CLONED_SONGS_ENDPOINT = "https://api.spotify.com/v1/playlists/" + "PLAYLIST_ID" + "/tracks"; //TODO CHANGE NAME TO CORRECT!
    private static final String GET_FAV_SONGS_ENDPOINT = "https://api.spotify.com/v1/me/tracks"; //TODO CHANGE NAME TO CORRECT!
    public static URI handle(RequestOption option, int offset, int limit) throws IOException, InterruptedException { //TODO CHANGE NAME TO CORRECT!
        return switch (option) {
            case ALL_PLAYLISTS_COUNT -> makeURI(PLAYLISTS_ENDPOINT + "?limit=" + limit + "&offset=" + offset);
            case PLAYLIST_ITEMS -> makeURI(PLAYLIST_ITEMS_ENDPOINT + "?limit=" + limit + "&offset=" + offset);
            case ADD_ITEM -> makeURI(ADD_ITEM_ENDPOINT + "?limit=" + limit + "&offset=" + offset);
            case DELETE_ITEM -> makeURI(DELETE_ITEM_ENDPOINT + "?limit=" + limit + "&offset=" + offset);
            case CREATE_PLAYLIST -> makeURI(CREATE_PLAYLIST_ENDPOINT);
            case USER_ID -> makeURI(USER_ID_ENDPOINT);
            case PLAYLIST_ID -> makeURI(PLAYLIST_ID_ENDPOINT);
            case ALL_FAV_SONGS_COUNT -> makeURI(ALL_FAV_SONGS_ENDPOINT);
            case ALL_CLONED_SONGS_COUNT -> makeURI(ALL_CLONED_SONGS_ENDPOINT);
            case FAV_PLAYLIST_ITEMS -> makeURI(GET_FAV_SONGS_ENDPOINT + "?limit=" + limit + "&offset=" + offset);
        };
    }

    public static URI handle(RequestOption option) throws IOException, InterruptedException { //TODO CHANGE NAME TO CORRECT!
        return switch (option) {
            case ALL_PLAYLISTS_COUNT -> makeURI(PLAYLISTS_ENDPOINT);
            case PLAYLIST_ITEMS -> makeURI(PLAYLIST_ITEMS_ENDPOINT);
            case ADD_ITEM -> makeURI(ADD_ITEM_ENDPOINT);
            case DELETE_ITEM -> makeURI(DELETE_ITEM_ENDPOINT);
            case CREATE_PLAYLIST -> makeURI(CREATE_PLAYLIST_ENDPOINT);
            case USER_ID -> makeURI(USER_ID_ENDPOINT);
            case PLAYLIST_ID -> makeURI(PLAYLIST_ID_ENDPOINT);
            case ALL_FAV_SONGS_COUNT -> makeURI(ALL_FAV_SONGS_ENDPOINT);
            case ALL_CLONED_SONGS_COUNT -> makeURI(ALL_CLONED_SONGS_ENDPOINT);
            case FAV_PLAYLIST_ITEMS -> makeURI(GET_FAV_SONGS_ENDPOINT);
        };
    }

    private static URI makeURI(String url) throws IOException, InterruptedException { //TODO Optimize requests
        if (url.contains("USER_ID")) {
            return URI.create(url.replace("USER_ID", User.getId()));
        }
        if (url.contains("PLAYLIST_ID")) {
            return URI.create(url
                    .replace("PLAYLIST_ID", User.getPlaylistID()));
        }
        return URI.create(url);
    }
}
