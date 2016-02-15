package eu.inginea.lambdacriteria.streamQuery;

public class Path implements Term {

    private final String path;
    
    public Path(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Path{"  + path + '}';
    }

}
