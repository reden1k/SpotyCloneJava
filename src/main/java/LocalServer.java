import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class LocalServer {

    private static final String FOLDER_PATH = "frontend";
    static HttpServer LocalServer;

    public static void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String requestedPath = exchange.getRequestURI().getPath();

                if (requestedPath.equals("/")) {
                    requestedPath = "/index.html";
                }

                String filePath = FOLDER_PATH + requestedPath;

                File file = new File(filePath);
                if (file.exists() && file.isFile()) {
                    String contentType = getContentType(file.getName());
                    exchange.getResponseHeaders().set("Content-Type", contentType);

                    exchange.sendResponseHeaders(200, file.length());
                    OutputStream responseBody = exchange.getResponseBody();
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        responseBody.write(buffer, 0, bytesRead);
                    }
                    fileInputStream.close();
                    responseBody.close();
                } else {
                    String response = "File not found: " + requestedPath;
                    exchange.sendResponseHeaders(404, response.length());
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(response.getBytes());
                    responseBody.close();
                }
            }
        });

        server.start();

        System.out.println("Server is running on port 8080");
        LocalServer = server;
    }
    private static String getContentType(String fileName) {
        if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else {
            return "application/octet-stream";
        }
    }
    public static void close() {
        if (LocalServer != null) {
            LocalServer.stop(0);
        }
    }
}
