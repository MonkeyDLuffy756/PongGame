import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    static final int GAME_WIDTH = 800;
    static final int GAME_HEIGHT = 600;

    Timer timer;           // Fires events to update and redraw the game
    Ball ball;             // Our Ball object
    Paddle paddle1;        // Left paddle
    Paddle paddle2;        // Right paddle

    // Scores for each player
    int score1 = 0;
    int score2 = 0;

    // For tracking which keys are held down
    boolean wPressed, sPressed;
    boolean upPressed, downPressed;

    // Paddle movement speed
    int paddleSpeed = 5;

    // Game-over logic
    boolean gameOver = false;
    String winnerText = "";

    public GamePanel() {
        // Set up the panel size and background
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(Color.BLACK);

        // Create and start the Timer (updates ~100 times per second)
        timer = new Timer(10, this);
        timer.start();

        // Initialize the Ball near the center of the panel
        ball = new Ball(GAME_WIDTH / 2, GAME_HEIGHT / 2);

        // Initialize the paddles
        // Paddle 1 near the left edge, vertically centered
        paddle1 = new Paddle(50, GAME_HEIGHT / 2 - 50);
        // Paddle 2 near the right edge, vertically centered
        paddle2 = new Paddle(GAME_WIDTH - 60, GAME_HEIGHT / 2 - 50);

        // Key listener setup
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    // Draw everything on the screen
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameOver) {
            // Normal game view
            ball.draw(g);
            paddle1.draw(g);
            paddle2.draw(g);

            // Draw scores
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 36));
            g.drawString(String.valueOf(score1), GAME_WIDTH / 4, 50);
            g.drawString(String.valueOf(score2), GAME_WIDTH * 3 / 4, 50);
        } else {
            // Game Over screen
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 50));
            g.drawString("GAME OVER", GAME_WIDTH / 2 - 150, GAME_HEIGHT / 2 - 20);

            g.setFont(new Font("Consolas", Font.PLAIN, 30));
            g.drawString(winnerText, GAME_WIDTH / 2 - 150, GAME_HEIGHT / 2 + 30);
        }
    }

    // Called automatically by the Timer every 10ms
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Only update/move if the game isn't over
            ball.move();
            checkBallCollisions();
            movePaddles();
        }
        // Redraw
        repaint();
    }

    // Collision checks for walls, paddles, and scoring
    private void checkBallCollisions() {
        // Top edge
        if (ball.y <= 0) {
            ball.yVelocity = -ball.yVelocity;
        }
        // Bottom edge
        if (ball.y >= GAME_HEIGHT - ball.diameter) {
            ball.yVelocity = -ball.yVelocity;
        }

        // Left edge -> player 2 scores
        if (ball.x <= 0) {
            score2++;
            checkWinCondition();
            resetBall();
        }
        // Right edge -> player 1 scores
        if (ball.x >= GAME_WIDTH - ball.diameter) {
            score1++;
            checkWinCondition();
            resetBall();
        }

        // Paddle 1 collision
        if (ball.x < paddle1.x + paddle1.width &&
            ball.x + ball.diameter > paddle1.x &&
            ball.y < paddle1.y + paddle1.height &&
            ball.y + ball.diameter > paddle1.y) {
            
            ball.xVelocity = -ball.xVelocity;
            ball.x = paddle1.x + paddle1.width; // shift ball outside the paddle
        }

        // Paddle 2 collision
        if (ball.x + ball.diameter > paddle2.x &&
            ball.x < paddle2.x + paddle2.width &&
            ball.y + ball.diameter > paddle2.y &&
            ball.y < paddle2.y + paddle2.height) {

            ball.xVelocity = -ball.xVelocity;
            ball.x = paddle2.x - ball.diameter; // shift ball outside
        }
    }

    // Check if either player has reached 5 points
    private void checkWinCondition() {
        if (score1 >= 5) {
            gameOver = true;
            winnerText = "Player 1 wins!";
        } else if (score2 >= 5) {
            gameOver = true;
            winnerText = "Player 2 wins!";
        }
    }

    // Reset the ball to the center, maybe change its direction
    private void resetBall() {
        // If someone already won, don't reset
        if (gameOver) return;

        ball.x = GAME_WIDTH / 2;
        ball.y = GAME_HEIGHT / 2;

        // Reverse or randomize direction
        ball.xVelocity = -ball.xVelocity;
        ball.yVelocity = -ball.yVelocity;
    }

    // Move the paddles based on which keys are pressed
    private void movePaddles() {
        if (wPressed) {
            paddle1.y -= paddleSpeed;
        }
        if (sPressed) {
            paddle1.y += paddleSpeed;
        }

        if (upPressed) {
            paddle2.y -= paddleSpeed;
        }
        if (downPressed) {
            paddle2.y += paddleSpeed;
        }

        // Prevent paddles from going off the screen
        if (paddle1.y < 0) {
            paddle1.y = 0;
        }
        if (paddle1.y > GAME_HEIGHT - paddle1.height) {
            paddle1.y = GAME_HEIGHT - paddle1.height;
        }
        if (paddle2.y < 0) {
            paddle2.y = 0;
        }
        if (paddle2.y > GAME_HEIGHT - paddle2.height) {
            paddle2.y = GAME_HEIGHT - paddle2.height;
        }
    }

    // KeyListener methods
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            // Paddle 1 (W and S)
            case KeyEvent.VK_W:
                wPressed = true;
                break;
            case KeyEvent.VK_S:
                sPressed = true;
                break;

            // Paddle 2 (Up and Down arrows)
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            // Paddle 1
            case KeyEvent.VK_W:
                wPressed = false;
                break;
            case KeyEvent.VK_S:
                sPressed = false;
                break;

            // Paddle 2
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required by KeyListener
    }
}





