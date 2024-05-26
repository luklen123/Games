import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    int boardWidth;
    int boardHeight;
    int tileSize = 30;
    Tile snakeHead;
    Tile foodTile;
    Timer gameLoop;
    Random rand;
    int velocityX;
    int velocityY;
    ArrayList<Tile> snakeBody;
    int bonusCounter;
    int bonusLimit = -1;
    int bonusAppear;
    Tile bonusTile;
    boolean gameOver = false;

    private class Tile{
        int cor_X;
        int cor_Y;
        public Tile(int cor_X, int cor_Y) {
            this.cor_X = cor_X;
            this.cor_Y = cor_Y;
        }
    }
    public SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        rand = new Random();
        snakeHead = new Tile(7,7);
        snakeBody = new ArrayList<Tile>();
        foodTile = foodLocation();
        gameLoop = new Timer(100, this);
        gameLoop.start();
        addKeyListener(this);
        setFocusable(true);



    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        // food
        g.setColor(Color.RED);
        g.fillRect(foodTile.cor_X*tileSize, foodTile.cor_Y*tileSize, tileSize, tileSize);
        // bonusfood
        if(bonusAppear == 5){
            bonusTile = foodLocation();
            bonusAppear = 0;
            bonusLimit = 50;
        }
        if(bonusLimit >=0 ){
            if(bonusLimit%2==0) g.setColor(Color.PINK);
            else g.setColor(Color.RED);
            g.fillRect(bonusTile.cor_X*tileSize, bonusTile.cor_Y*tileSize, tileSize, tileSize);
        }
        // waz
        g.setColor(Color.GREEN);
        g.fillRect(snakeHead.cor_X*tileSize, snakeHead.cor_Y*tileSize, tileSize, tileSize);
        for(int i=0; i<snakeBody.size(); i++){
            Tile tile1 = snakeBody.get(i);
            g.fill3DRect(tile1.cor_X*tileSize, tile1.cor_Y*tileSize, tileSize, tileSize, true);
        }
        // siatka
        /*g.setColor(Color.GRAY);
        for(int i = 0; i <= boardWidth/tileSize; i++) {
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
        }
        for(int i = 0; i <= boardHeight/tileSize; i++) {
            g.drawLine(0,i*tileSize, boardWidth, i*tileSize);
        }*/
        // licznik wyniku
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.RED);
            g.drawString("Score: "+(snakeBody.size()+(bonusCounter*5)), tileSize-16, tileSize);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Score: "+(snakeBody.size()+(bonusCounter*5)), tileSize-16, tileSize);
        }

    }
    public Tile foodLocation(){
        boolean checked = false;
        Tile akt = null;
        while(!checked){
            checked = true;
            int x = rand.nextInt(boardWidth/tileSize);
            int y = rand.nextInt(boardHeight/tileSize);
            akt = new Tile(x,y);
            if(collision(snakeHead, akt)){
                checked = false;
            } else {
                for(int i=0; i<snakeBody.size(); i++){
                    if(collision(snakeBody.get(i), akt)){
                        checked = false;
                        break;
                    }
                }
            }
        }
        return akt;
    }
    public void move(){
        if(collision(snakeHead, foodTile)){
            snakeBody.add(foodTile);
            foodTile = foodLocation();
            bonusAppear++;
        }
        if(bonusTile != null && collision(snakeHead, bonusTile)){
            bonusCounter++;
            bonusLimit = 0;
        }
        for(int i=snakeBody.size()-1; i>=0; i--){
            Tile tile1 = snakeBody.get(i);
            if(i==0){
                tile1.cor_X = snakeHead.cor_X;
                tile1.cor_Y = snakeHead.cor_Y;
            } else {
                Tile tile2 = snakeBody.get(i-1);
                tile1.cor_X = tile2.cor_X;
                tile1.cor_Y = tile2.cor_Y;
            }
        }
        snakeHead.cor_X += velocityX;
        snakeHead.cor_Y += velocityY;
        if(snakeHead.cor_X>=boardWidth/tileSize) snakeHead.cor_X=0;
        if(snakeHead.cor_X<0) snakeHead.cor_X=boardWidth/tileSize;
        if(snakeHead.cor_Y>=boardHeight/tileSize) snakeHead.cor_Y=0;
        if(snakeHead.cor_Y<0) snakeHead.cor_Y=boardHeight/tileSize;
        bonusLimit--;
        for(int i=0; i<snakeBody.size(); i++){
            if(collision(snakeBody.get(i), snakeHead)){
                gameLoop.stop();
                gameOver = true;
            }
        }
    }
    public boolean collision(Tile tile1, Tile tile2){
        if(tile1.cor_Y == tile2.cor_Y && tile1.cor_X == tile2.cor_X){
            return true;
        }
        return false;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX !=1){
            velocityX = -1;
            velocityY = 0;
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX !=-1){
            velocityX = 1;
            velocityY = 0;
        } else if(e.getKeyCode() == KeyEvent.VK_UP && velocityY !=1){
            velocityX = 0;
            velocityY = -1;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY !=-1){
            velocityX = 0;
            velocityY = 1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }
}
