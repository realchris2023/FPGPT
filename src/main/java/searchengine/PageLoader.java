package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;


public class PageLoader {
    private List<Page> pages = new ArrayList<>();

    public PageLoader(String filename) throws IOException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            var lastIndex = lines.size();
            for (var i = lines.size() - 1; i >= 0; --i) {
                if (lines.get(i).startsWith("*PAGE")) {
                    
                    if (isPageValid(lines, i, lastIndex)) {
                        
                        Page page = new Page(lines.subList(i, lastIndex));
                        pages.add(page);

                    }
                    lastIndex = i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Collections.reverse(pages);
    }
    
    private boolean isPageValid(List<String> lines, int startIndex, int endIndex) {
        boolean titleSet = false;
        boolean hasWords = false;

        for (int i = startIndex + 1; i < endIndex; i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                if (!titleSet) {
                    titleSet = true; // First non-empty line is the title
                } else {
                    hasWords = true; // Subsequent non-empty lines are content
                    break;
                }
            }
        }
        return titleSet && hasWords;
    }
    
    public List<Page> getPages() {
        return pages;
    }

    public void printPages() {
        for (Page page : pages) {
            if (!page.getLines().isEmpty()) {
                // Print the title (the first line of the page)
                System.out.println("Page Title: " + page.getLines().get(0));

                // Print the content (lines after the title)
                System.out.println("Content:");
                for (int i = 1; i < page.getLines().size(); i++) {
                    System.out.println(page.getLines().get(i));
                }

                System.out.println("-----------------------------------");
            }
        }
    }

}
