public class HighscoreItem implements Comparable<HighscoreItem>{
    private int score;
    private String name;

    public HighscoreItem(int score, String name) {
        this.score = score;
        this.name = name;
    }

    @Override
    public int compareTo(HighscoreItem item) {
        if (this.getScore() > item.getScore()) {
            return -1;
        }

        if (this.getScore() < item.getScore()) {
            return 1;
        } else {
            return 0;
        }   
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "name=" + name + " / score=" + score;
    }

    
}
