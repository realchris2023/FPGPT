package searchengine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
    static final int PORT = 8080;
    static final int BACKLOG = 0;
    static final Charset CHARSET = StandardCharsets.UTF_8;

    HttpServer server;
    private PageLoader pageLoader;

    public WebServer(int port, PageLoader pageLoader) throws IOException {
        this.pageLoader = pageLoader;
        server = HttpServer.create(new InetSocketAddress(port), BACKLOG);
        setupRoutes();
    }

    public static void respond(HttpExchange io, int code, String mime, byte[] response) {
        try {
            io.getResponseHeaders()
                    .set("Content-Type", String.format("%s; charset=%s", mime, CHARSET.name()));
            io.sendResponseHeaders(200, response.length);
            io.getResponseBody().write(response);
        } catch (Exception e) {
        } finally {
            io.close();
        }
    }

    private void setupRoutes() {
        server.createContext("/", io -> respond(io, 200, "text/html", FileHandler.getFile("web/index.html")));
        server.createContext("/search", io -> SearchHandler.searchResults(io, pageLoader));
        server.createContext(
                "/favicon.ico", io -> respond(io, 200, "image/x-icon", FileHandler.getFile("web/favicon.ico")));
        server.createContext(
                "/code.js", io -> respond(io, 200, "application/javascript", FileHandler.getFile("web/code.js")));
        server.createContext(
                "/style.css", io -> respond(io, 200, "text/css", FileHandler.getFile("web/style.css")));
    }

    public void startServer() {
        server.start();
        String msg = "WebServer running on http://localhost:" + server.getAddress().getPort();
        System.out.println("╭" + "─".repeat(msg.length()) + "╮");
        System.out.println("│" + msg + "│");
        System.out.println("╰" + "─".repeat(msg.length()) + "╯");
    }

    public static void main(final String... args) throws IOException {
        String filename = Files.readString(Paths.get("config.txt")).strip();
        PageLoader pageLoader = new PageLoader(filename);

        pageLoader.printPages();

        WebServer webServer = new WebServer(PORT, pageLoader);
        webServer.startServer();
    }
}
