package se.mns;

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
import java.util.concurrent.ThreadLocalRandom;

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

    public GameSurface(final int width, final int height) {
        this.gameOver = false;
        this.aliens = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            addAlien(width, height);
        }

        this.spaceShip = new Rectangle(20, width / 2 - 15, 30, 20);

        this.timer = new Timer(200, this);
        this.timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaint(g);
    }

    private void addAlien(final int width, final int height) {
        int x = ThreadLocalRandom.current().nextInt(width / 2, width - 30);
        int y = ThreadLocalRandom.current().nextInt(20, height - 30);
        aliens.add(new Rectangle(x, y, 10, 10));
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
            g.drawString("Game over!", 20, d.width / 2 - 24);
            return;
        }

        // fill the background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, d.width, d.height);

        // draw the aliens
        for (Rectangle alien : aliens) {
            g.setColor(Color.red);
            g.fillRect(alien.x, alien.y, alien.width, alien.height);
        }

        // draw the space ship
        g.setColor(Color.black);
        g.fillRect(spaceShip.x, spaceShip.y, spaceShip.width, spaceShip.height);
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

        final int maxHeight = this.getSize().height - spaceShip.height - 10;
        if (spaceShip.y < maxHeight) {
            spaceShip.translate(0, 20);
        }

        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // this event triggers when we press a key and then
        // we will move the space ship up if the game is not over yet

        if (gameOver) {
            return;
        }

        final int minHeight = 10;
        final int kc = e.getKeyCode();

        if (kc == KeyEvent.VK_SPACE && spaceShip.y > minHeight) {
            spaceShip.translate(0, -10);
        }
    }
}
