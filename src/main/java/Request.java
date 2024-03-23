import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class Request {
    public static void get(String token, RequestOption option, int offset, int limit) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(OptionsHandler.handle(option, offset, limit))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(ResponseHandler.status(response));
        ResponseHandler.executeEnumActionString(response, option);
    }

    public static String get(String token, RequestOption option) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(OptionsHandler.handle(option))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(ResponseHandler.status(response));
        return ResponseHandler.executeEnumActionString(response, option);
    }


    public static void post(String token, RequestOption option, Path file) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(OptionsHandler.handle(option))
                .POST(HttpRequest.BodyPublishers.ofFile(file))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(ResponseHandler.status(response, "Added 50 songs", "Error! Failed add"));
        ResponseHandler.executeEnumActionString(response, option);
    }

    public static void delete(String token, RequestOption option, Path path) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(OptionsHandler.handle(option))
                .method("DELETE", HttpRequest.BodyPublishers.ofFile(path))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(ResponseHandler.status(response, "Deleted 50 songs", "Error! Failed delete"));
        ResponseHandler.executeEnumActionString(response, option);
    }
}
