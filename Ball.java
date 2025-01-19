import java.awt.Color;
import java.awt.Graphics;

public class Ball {

    // Position of the ball
    int x;
    int y;

    // Size of the ball (diameter)
    int diameter = 20;

    // Speed in the X and Y direction
    int xVelocity = 3;
    int yVelocity = 3;

    // Constructor
    public Ball(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // Move the ball by updating x and y
    public void move() {
        x += xVelocity;
        y += yVelocity;
    }

    // Draw the ball
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, diameter, diameter);
    }
}
