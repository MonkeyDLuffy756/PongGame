import java.awt.Color;
import java.awt.Graphics;

public class Paddle {
    
    // Position and size
    public int x;
    public int y;
    public int width = 10;
    public int height = 100;

    // Constructor: Initialize the paddle at (startX, startY)
    public Paddle(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // Draw the paddle
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }
}
