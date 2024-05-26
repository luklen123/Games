import javax.swing.*;

public class SnakeApp {
    public static void main(String[] args) {
        int BoardWidth = 600;
        int BoardHeight = 600;

        JFrame frame = new JFrame("Snake");
        frame.setSize(BoardWidth, BoardHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        SnakeGame game = new SnakeGame(BoardWidth, BoardHeight);
        frame.add(game);
        frame.pack();
        game.requestFocus();
    }
}
