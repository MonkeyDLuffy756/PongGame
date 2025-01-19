import javax.swing.JFrame;

public class PongGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an instance of GamePanel and add it to the frame
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        // Instead of frame.setSize(...),
        // let the GamePanel's preferred size dictate the window size
        frame.pack(); // packs the frame to fit the preferred size
        frame.setLocationRelativeTo(null); // centers the window
        frame.setVisible(true);
    }
}
