import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * A simple panel with a space invaders "game" in it. This is just to
 * demonstrate the bare minimum of stuff than can be done drawing on a panel.
 * This is by no means very good code.
 * 
 */
public class GameSurface extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 6260582674762246325L;
    private boolean gameOver;
    private Timer timer;
    private List<WarpPipes> pipeList; // used to be: private List<Rectangle> aliens;
    private Rectangle bird;
    private int jumpRemaining;
    private int frames;
    private int gravity;
    private int currentScore;
    private int width;
    private int height;
    private int highscore = 0;

    public GameSurface(final int width, final int height) {
        this.width = width;
        this.height = height;

        this.gameOver = false;
        this.pipeList = new ArrayList<>();

        pipeList.add(new WarpPipes(width, height));

        this.bird = new Rectangle(20, width / 2 - 15, 30, 20);
        this.timer = new Timer(2, this);
        this.timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaint(g);
    }

    /**
     * Call this method when the graphics needs to be repainted on the graphics
     * surface.
     * 
     * @param g the graphics to paint on
     */
    private void repaint(Graphics g) {
        final Dimension d = this.getSize();
        if (gameOver) {
            g.setColor(Color.red);
            g.fillRect(0, 0, d.width, d.height);
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Game over!" + " Your score: " + currentScore, 20, d.width / 2 - 24);
            g.drawString("Highscore: " + highscore, 20, d.width / 2 + 24);
            return;
        }
        // fill the background
        g.setColor(Color.blue);
        g.fillRect(0, 0, d.width, d.height);
        // draw the Warp Pipes.
        for (WarpPipes warpPipe : pipeList) {
            warpPipe.drawPipe(g);
        }
        // draw the space ship
        g.setColor(Color.yellow);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // We count the frames because we want to trigger
        // certain events after a specific amount of frames
        frames++;

        // This will make the game end when you hit a
        // warp pipe.

        // TODO: Show currentScore and highscore when game is over
        // TODO: Make it possible to start a new round again

        if (gameOver) {
            timer.stop();
            return;
        }
        final List<WarpPipes> toRemove = new ArrayList<>();
        // Removed by Tommi: for (Rectangle alien : aliens) {
        for (WarpPipes pipe : pipeList) {
            pipe.translate(-1, 0);

            if (pipe.noLongerOnScreen()) {
                // we add to another list and remove later
                // to avoid concurrent modification in a for-each loop
                toRemove.add(pipe);
            }

            // Space ship has crashed into pipe
            if (pipe.intersects(bird)) {
                gameOver = true;
            }
        }

        pipeList.removeAll(toRemove);
        // add a new pair of Warp Pipes for every one that was removed.
        for (int i = 0; i < toRemove.size(); ++i) {
            Dimension d = getSize();
            pipeList.add(new WarpPipes(d.width, d.height));

            // For every pipe that passes the screen, one currentScore is added.
            // If your current currentScore is higher than your highscore,
            // the highscore is updated.

            currentScore++;
            if (highscore < currentScore) {
                highscore = currentScore;
            }
            // addWarpPipe(d.width, d.height);
        }

        // jumpRemaining is an instance variable, everytime
        // the bird jumps its set to 30. Every frame/actionPerformed
        // the bird jumps 5px in Y-direction until jumpRemaining
        // becomes 0. This is because we want the bird to jump
        // in a smooth motion.

        if (jumpRemaining > 0) {
            bird.translate(0, -2);
            jumpRemaining = jumpRemaining - 2;
            gravity = 0;
        }

        // After every jump, the gravity starts affecting
        // the bird again. Variable gravity is also set to 0 after
        // every jump.
        // Currently the instance variable gravity increases by 1
        // every 7th frame. To have less or more gravity,
        // change the 7 to any other number. Higher number means
        // lower gravity and lower number means higher gravity.

        else {
            bird.translate(0, gravity);
            if (frames % 7 == 0) {
                frames = 0;
                gravity++;
            }
        }
        // if bird falls below gamesurface area = game over
        if (bird.y > 800) {
            gameOver = true;
        } 

        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // this event triggers when we press a key
        // in this case Space bar is for jumping
        final int minHeight = 10;
        final int maxHeight = this.getSize().height - bird.height - 10;
        final int kc = e.getKeyCode();

        if (gameOver && kc == KeyEvent.VK_SPACE) {
            // When game is over, gameover screen will show and timer will stop
            // To restart game, click space button. 

            // New bird is drawn and will start at the same position
            // as when the game started the first time.
            this.bird = new Rectangle(20, width / 2 - 15, 30, 20);

            // The List of pipes have to be cleared, otherwise
            // the old pipes from last round will still appear on the screen.
            // Then add a new pipe again.
            pipeList.clear();
            pipeList.add(new WarpPipes(width, height));

            // Clear the current current score
            currentScore = 0;

            // Set gravity to zero again after death.
            gravity = 0;
            // Set gameOver to false again so that the repaint method will
            // draw the game and then start the timer again.
            gameOver = false;
            timer.start();
        }

        else if (kc == KeyEvent.VK_SPACE && bird.y > minHeight && bird.y < maxHeight) {
            jumpRemaining = 30;
        }
    }
}