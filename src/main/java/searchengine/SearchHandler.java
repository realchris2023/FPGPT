package searchengine;
import com.sun.net.httpserver.HttpExchange;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchHandler {
    static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void searchResults(HttpExchange io, PageLoader pageLoader) {
        String searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
        List<List<String>> searchResults = search(pageLoader.getPages(), searchTerm);

        var response = new ArrayList<String>();
        if (searchResults.isEmpty()) {
            response.add("{\"message\": \"No web page contains the query word.\"}");
        } else {
            for (var page : searchResults) {
                response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
                        page.get(0).substring(6), page.get(1)));
            }
        }

        var bytes = response.toString().getBytes(CHARSET);
        WebServer.respond(io, 200, "application/json", bytes);
    }

    private static List<List<String>> search(List<Page> pages, String searchTerm) {
        var result = new ArrayList<List<String>>();
        for (Page page : pages) {
            if (page.getLines().contains(searchTerm)) {
                result.add(page.getLines());
            }
        }
        return result;
    }
}
