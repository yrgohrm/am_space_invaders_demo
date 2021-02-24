import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * This is a very small "game" just to show the absolute basics of
 * how to draw on a surface in a frame using Swing/AWT.
 * 
 */
public class App {
    public static void main(String[] args) {
             
        JFrame main = new JFrame("Jumpy Birb");

        GameSurface gs = new GameSurface();

        main.setSize(400, 400);
        main.setResizable(false);
        main.add(gs);
        main.addKeyListener(gs);
        main.setDefaultCloseOperation(EXIT_ON_CLOSE);
        main.setVisible(true);
    }
}