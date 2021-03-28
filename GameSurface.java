import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * A simple panel with a space invaders "game" in it. This is just to
 * demonstrate the bare minimum of stuff than can be done drawing on a panel.
 * This is by no means good code, but rather a short demonstration on
 * some things one can do to make a very simple Swing based game.
 * 
 * If you really want to make a good game there are several toolkits for
 * game making out there which are much more suitable for this.
 * 
 */
public class GameSurface extends JPanel implements KeyListener {
    private static final long serialVersionUID = 6260582674762246325L;
    private static Logger logger = Logger.getLogger(GameSurface.class.getName());

    // make some transient to get past boring serialization demands...
    private static final double ALIEN_PIXELS_PER_MS = 0.12;
    private transient FrameUpdater updater;
    private boolean gameOver;
    private List<Alien> aliens;
    private Rectangle spaceShip;
    private transient BufferedImage shipImageSprite;
    private int shipImageSpriteCount;

    public GameSurface(final int width) {
        try {
            this.shipImageSprite = ImageIO.read(new File("ship.png"));
            this.shipImageSpriteCount = 0;
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Unable to load image", ex);
        }

        this.gameOver = false;
        this.aliens = new ArrayList<>();
        this.spaceShip = new Rectangle(20, width / 2 - 15, 46, 20);

        this.updater = new FrameUpdater(this, 60);
        this.updater.setDaemon(true); // it should not keep the app running
        this.updater.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        drawSurface(g2d);
    }

    /**
     * Call this method when the graphics needs to be repainted on the graphics
     * surface.
     * 
     * @param g the graphics to paint on
     */
    private void drawSurface(Graphics2D g) {
        final Dimension d = this.getSize();

        if (gameOver) {
            g.setColor(Color.red);
            g.fillRect(0, 0, d.width, d.height);
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Game over!", 20, d.width / 2 - 24);
            return;
        }

        // fill the background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, d.width, d.height);

        // draw the aliens
        for (Alien alien : aliens) {
            g.setColor(Color.GREEN);
            g.fillRect(alien.bounds.x, alien.bounds.y, alien.bounds.width, alien.bounds.height);
        }

        // draw the space ship, as a cool image if it did load properly
        if (shipImageSprite != null) {
            int offset = 46 * shipImageSpriteCount;
            g.drawImage(shipImageSprite, spaceShip.x, spaceShip.y, spaceShip.x + spaceShip.width,
                    spaceShip.y + spaceShip.height, offset, 0, offset + 46, 20, null);
        } else {
            g.setColor(Color.black);
            g.fillRect(spaceShip.x, spaceShip.y, spaceShip.width, spaceShip.height);
        }
    }

    public void update(int time) {
        if (gameOver) {
            updater.interrupt();
            return;
        }

        final Dimension d = getSize();
        if (d.height <= 0 || d.width <= 0) {
            // if the panel has not been placed properly in the frame yet
            // just return without updating any state
            return;
        }

        // fill up with some aliens if we have none (at start of game)
        if (aliens.isEmpty()) {
            for (int i = 0; i < 10; ++i) {
                addAlien(time, d.height, true);
            }
        }

        // update ship sprite
        shipImageSpriteCount = (time / 100) % 3;

        final List<Alien> toRemove = new ArrayList<>();

        for (Alien alien : aliens) {
            // movement is based on elapsed time to make it smoother and
            // more consistent over different computers
            int timeElapsed = time - alien.created;
            alien.bounds.x = (int) (d.width - (timeElapsed * ALIEN_PIXELS_PER_MS));
            if (alien.bounds.x + alien.bounds.width < 0) {
                // we add to another list and remove later
                // to avoid concurrent modification in a for-each loop
                toRemove.add(alien);
            }

            if (alien.bounds.intersects(spaceShip)) {
                gameOver = true;
            }
        }

        // remove all aliens that are out of frame
        // we can't remove things from the aliens list while we're
        // iterating over it.
        aliens.removeAll(toRemove);

        // add new aliens for every one that was removed
        for (int i = 0; i < toRemove.size(); ++i) {
            addAlien(time, d.height, false);
        }
    }

    private void addAlien(final int time, final int height, boolean randomX) {
        int newTime = time;
        if (randomX) {
            // make sure they start randomly somewhere on the screen
            // by adjusting the create time, making it seem like they
            // have traveled on the screen for some time already
            final int MIN_PIXELS_FROM_LEFT = 180;
            final int MS_TO_TRAVEL_MIN_PIXELS = (int)(MIN_PIXELS_FROM_LEFT / ALIEN_PIXELS_PER_MS);
            newTime = time - ThreadLocalRandom.current().nextInt(MS_TO_TRAVEL_MIN_PIXELS);
        }

        final int FAR_OFFSCREEN = 10000;
        int y = ThreadLocalRandom.current().nextInt(20, height - 30);
        aliens.add(new Alien(newTime, FAR_OFFSCREEN, y));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // this event triggers when we release a key and then
        // we will move the space ship if the game is not over yet

        if (gameOver) {
            return;
        }

        final int minHeight = 10;
        final int maxHeight = this.getSize().height - spaceShip.height - 10;
        final int kc = e.getKeyCode();

        if (kc == KeyEvent.VK_UP && spaceShip.y > minHeight) {
            spaceShip.translate(0, -10);
        } else if (kc == KeyEvent.VK_DOWN && spaceShip.y < maxHeight) {
            spaceShip.translate(0, 10);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // do nothing
    }
}
