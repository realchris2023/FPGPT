package searchengine;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private List<String> lines;

    public Page(List<String> lines){
        this.lines = new ArrayList<>(lines);
    }

    public List<String> getLines() {
        return lines;
    }

}
