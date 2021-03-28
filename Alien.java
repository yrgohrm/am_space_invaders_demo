import java.awt.Rectangle;
import java.io.Serializable;

// this class can be much improved, better encapsulation
// draw itself, update itself etc. etc.
public class Alien implements Serializable {
    private static final long serialVersionUID = -6385903094117229614L;

    public final int created;
    public final Rectangle bounds;

    public Alien(int created, int x, int y) {
        this.created = created;
        this.bounds = new Rectangle(x, y, 10, 10);
    }
}
