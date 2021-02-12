import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
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
    private List<Rectangle> aliens;
    private Rectangle spaceShip;
    private int yMotion;
    private final int width = 400;
    private final int height = 400;

    public GameSurface() {
        this.gameOver = false;
        this.aliens = new ArrayList<>();

        addAlien(width, height);

        this.spaceShip = new Rectangle(width/3, width/2, 40, 28);

        this.timer = new Timer(20, this);
        this.timer.start();
    }
    
    private void restart() {
        this.gameOver = false;
        this.aliens = new ArrayList<>();

        addAlien(width, height);

        this.spaceShip = new Rectangle(width/3, width/2, 40, 28);

        this.timer = new Timer(20, this);
        this.timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            repaint(g);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void addAlien(final int width, final int height) {
        int randomHeight = ThreadLocalRandom.current().nextInt(height/4, height/2);
        int gap = 150;

        // top alien
        aliens.add(new Rectangle(width, 0, 50, (height - (randomHeight + gap))));
        // console log for bugfixes
        System.out.println("top alien:\t" + "x: " + width + "\ty: " + 0 + "\theight: " + (height - (randomHeight + gap)));
        
        
        // bottom alien
        aliens.add(new Rectangle(width, (height-randomHeight), 50, randomHeight));
        // console log for bugfixes
        System.out.println("bottom alien:\t" + "x:" + width + "\ty:" + (height-randomHeight) + "\theight:" + randomHeight);

    }

    /**
     * Call this method when the graphics needs to be repainted on the graphics
     * surface.
     * 
     * @param g the graphics to paint on
     * @throws IOException
     */
    private void repaint(Graphics g) throws IOException {
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
        g.setColor(Color.cyan);
        g.fillRect(0, 0, d.width, d.height);

        // draw the aliens
        for (Rectangle alien : aliens) {
            g.setColor(Color.green);
            g.fillRect(alien.x, alien.y, alien.width, alien.height);
        }

        // draw the space ship
        Image img = ImageIO.read(Path.of("images/Bird2.png").toFile());
        g.drawImage(img, spaceShip.x, spaceShip.y, null);
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(bimage, 40, 28, null);
        bGr.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this will trigger on the timer event
        // if the game is not over yet it will
        // update the positions of all aliens
        // and check for collision with the space ship

        if (gameOver) {
            timer.stop();
            return;
        }

        final List<Rectangle> toRemove = new ArrayList<>();

        for (Rectangle alien : aliens) {
            alien.translate(-1, 0);
            if (alien.x + alien.width < 0) {
                // we add to another list and remove later
                // to avoid concurrent modification in a for-each loop
                toRemove.add(alien);
            }

            if (alien.intersects(spaceShip)) {
                gameOver = true;
            }
        }

        aliens.removeAll(toRemove);

        // add new aliens for every one that was removed
        for (int i = 0; i < toRemove.size(); ++i) {
            Dimension d = getSize();
            addAlien(d.width, d.height);
        }

        this.repaint();

        spaceShip.y -= yMotion;
        
        if (spaceShip.y < 0 || spaceShip.y  > height) {
            gameOver = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // this event triggers when we release a key and then
        // we will move the space ship if the game is not over yet

        final int maxHeight = this.getSize().height - spaceShip.height - 10;
        final int kc = e.getKeyCode();

        if (!gameOver && kc == KeyEvent.VK_SPACE && spaceShip.y < maxHeight) {
            jump();
        } 
        else if (gameOver && kc == KeyEvent.VK_SPACE) {
            restart();
        }
    }

    public void jump() {
        spaceShip.translate(0, -50);
        if (yMotion < 0) {
            yMotion = 0;
        }
        yMotion -= 3;

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
