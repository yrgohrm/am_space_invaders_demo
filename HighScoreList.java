import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HighScoreList {
    static final int MAX_SIZE = 10;
    private static List<HighscoreItem> highscoreList;

    public HighScoreList() {
        highscoreList = new ArrayList<>();
        loadHighscore();
    }

    public int getLowestHighscore() {
        if (highscoreList.size() < MAX_SIZE) {
            return 0;
        }

        if (highscoreList.size() <= MAX_SIZE) {
            return highscoreList.get(highscoreList.size()-1).getScore();
        } else {
            return highscoreList.get(MAX_SIZE-1).getScore();
        }
    }

    public int getHighestHighscore() {
        if (highscoreList.size() == 0)
            return 0;
        else return highscoreList.get(0).getScore();
    }

    public void addHighscore(HighscoreItem newHighscore) throws IllegalArgumentException {
        // Check to see if this score is enough to make the leaderboard.
        if (newHighscore.getScore() < getLowestHighscore()) {
            throw new IllegalArgumentException("Score of" + newHighscore.getScore() + "(" + newHighscore.getName() + ") is not enough to make it to the highscore list");
        }

        // Is this person already on the leaderboard?
        boolean scoreSaved = false;
        for (int i=0; i<highscoreList.size(); i++) {
            // This person already has a previous score on the leaderboard.
            if ( highscoreList.get(i).getName() == newHighscore.getName() ) {
                if ( highscoreList.get(i).getScore() > newHighscore.getScore() ) {
                    // The previous score is better than this new one. Keep the old score.
                    scoreSaved=true;
                    break;    
                } else {
                    // New score is better than old score. Replace old score with new.
                    highscoreList.set(i, newHighscore);
                    scoreSaved=true;
                    break;
                }
            }
        }
        
        // Save score to the leaderboard (if this has not yet been done in previous for-loop).
        if (scoreSaved == false) {
            highscoreList.add(newHighscore);
        }
        
        Collections.sort(highscoreList);
        
        // If list has overflowed in size: remove last element.
        if (highscoreList.size() > MAX_SIZE) {
            highscoreList.remove(highscoreList.size()-1);
        }
    }

    public static List<HighscoreItem> getList() {
        return highscoreList;
    }
    
    private void loadHighscore() {
        try {        
            addHighscore(new HighscoreItem(10, "Thomas"));
            addHighscore(new HighscoreItem(3, "Tommi"));
            addHighscore(new HighscoreItem(3, "Tommi"));
            addHighscore(new HighscoreItem(2, "Tommi"));
            addHighscore(new HighscoreItem(4, "Tommi"));
            addHighscore(new HighscoreItem(7, "Merra"));
            addHighscore(new HighscoreItem(5, "Bo"));
            addHighscore(new HighscoreItem(15, "Aku Ankka"));
            addHighscore(new HighscoreItem(5, "Thomas"));
            addHighscore(new HighscoreItem(15, "Aku Ankka"));
        } catch (Exception e) {
            System.err.println("Error:" + e.getMessage());
        }

    }

}
