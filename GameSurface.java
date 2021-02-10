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
 * demonstrate the bare minimum of stuff than can be done drawing on
 * a panel. This is by no means very good code.
 * 
 */
public class GameSurface extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 6260582674762246325L;
    private boolean gameOver;
    private Timer timer;
    private List<WarpPipes> pipeList; // used to be: private List<Rectangle> aliens;
    private Rectangle spaceShip;
    private int jumpRemaining;
    private int frames;
    private int gravity;

    public GameSurface(final int width, final int height) {
        this.gameOver = false;
        this.pipeList = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            pipeList.add(new WarpPipes(width, height));
        }
        this.spaceShip = new Rectangle(20, width/2-15, 30, 20);
        this.timer = new Timer(2, this);
        this.timer.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaint(g);
    }

    /**
     * Call this method when the graphics needs to be repainted
     * on the graphics surface.
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
            g.drawString("Game over!", 20, d.width/2-24);
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
        g.fillRect(spaceShip.x, spaceShip.y, spaceShip.width, spaceShip.height);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // We count the frames because we want to trigger
        // certain events after a specific amount of frames
        frames++;

        // This will make the game end when you hit a 
        // warp pipe.

        //TODO: Show score and highscore when game is over
        //TODO: Make it possible to start a new round again

        if (gameOver) {
            timer.stop();
            return;
        }
        final List<WarpPipes> toRemove = new ArrayList<>();
        //Removed by Tommi: for (Rectangle alien : aliens) {
        for (WarpPipes pipe : pipeList) {
            pipe.translate(-1, 0);
            
            if (pipe.noLongerOnScreen()) {
                // we add to another list and remove later
                // to avoid concurrent modification in a for-each loop
                toRemove.add(pipe);
            }

            // Space ship has crashed into pipe
            if (pipe.intersects(spaceShip)) {
                gameOver = true;
            }
        }
        
        pipeList.removeAll(toRemove);
        // add a new pair of Warp Pipes for every one that was removed.
        for (int i = 0; i < toRemove.size(); ++i) {
            Dimension d = getSize();
            pipeList.add(new WarpPipes(d.width, d.height));
            //addWarpPipe(d.width, d.height);
        }

        // jumpRemaining is an instance variable, everytime
        // the bird jumps its set to 30. Every frame/actionPerformed
        // the bird jumps 5px in Y-direction until jumpRemaining
        // becomes 0. This is because we want the bird to jump
        // in a smooth motion.
        
        if (jumpRemaining > 0) {
            spaceShip.translate(0, -2);
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
            spaceShip.translate(0, gravity);
            if(frames % 7 == 0) {
                frames = 0;
                gravity++;
            }
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

        if (gameOver) {
            return;
        }
        final int minHeight = 10;
        final int maxHeight = this.getSize().height - spaceShip.height - 10;
        final int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_SPACE && spaceShip.y > minHeight && spaceShip.y < maxHeight) {
                jumpRemaining = 30;
        }
    }
}