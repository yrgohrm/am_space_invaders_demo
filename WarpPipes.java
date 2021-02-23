import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

/**
 *  Represents a pair of warp pipes. One pipe is at the bottom of the screen and one pipe is at the top of the screen.
 */
public class WarpPipes {

    private Rectangle rectangle1;
    private Rectangle rectangle2;
    /**
     * 
     * @param width Width of one warp pipe.
     * @param height Height of one warp pipe.
     */
    public WarpPipes(int width, int height) {
        int x = width;
        int y = 0;
        int xx = width;
        int yy = height/2;

        rectangle1 = new Rectangle(x, y, 60, 300);
        rectangle2 = new Rectangle(xx, yy, 60, 400);
    }

    /**
     * Check if rectangle intersects with these Warp Pipes.
     * 
     * @param r Rectangle.
     * @return True if warp pipe intersects with rectangle r.
     */
    boolean intersects(Rectangle r) {
        return rectangle1.intersects(r) || rectangle2.intersects(r);
    }

    /**
     * Displace Warp Pipes.
     * 
     * @param dx Displacement on x-axis.
     * @param dy Displacement on y-axis.
     */
    void translate(int dx, int dy) {
        rectangle1.translate(dx, dy);
        rectangle2.translate(dx, dy);
    }

    /**
     * 
     * @return True if Warp Pipes have moved beyond left side of screen.
     */
    boolean noLongerOnScreen() {
        if (rectangle1.x + rectangle1.width < 0)
            return true;
        else 
            return false;
    }

    
    boolean halfwayAcrossScreen(int width) {
        if (rectangle1.x + rectangle1.width < width/2)
            return true;
        else 
            return false;
    }

    /**
     * 
     * Draws the pair of warp pipes.
     * 
     * @param g
     */
    void drawPipe(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height);
        g.fillRect(rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
    }

    
}
