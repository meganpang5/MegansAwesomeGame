import java.awt.*;

public class CakeRect {
    public int xpos;
    public int ypos;
    public int width;
    public int height;
    public Rectangle rect;
    public boolean hasCake;

    public CakeRect(int pXpos, int pYpos) {
        xpos = pXpos;
        ypos = pYpos;
        width = 30;
        height = 30;
        hasCake = false;

        rect = new Rectangle(xpos, ypos, width, height);
    }
}
