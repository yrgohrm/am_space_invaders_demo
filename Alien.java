import java.awt.Rectangle;

// this class can be much improved, better encapsulation
// draw itself, update itself etc. etc.
public class Alien {
    public final int created;
    public final Rectangle bounds;

    public Alien(int created, int x, int y) {
        this.created = created;
        this.bounds = new Rectangle(x, y, 10, 10);
    }
}
