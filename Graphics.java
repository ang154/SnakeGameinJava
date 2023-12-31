import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Graphics extends JPanel implements ActionListener {

    static final int WIDTH = 800;
    static final int HEIGHT = 800;
    static final int TICK_SIZE = 50;
    static final int BOARD_SIZE = (WIDTH * HEIGHT) / (TICK_SIZE * TICK_SIZE);

    final Font font = new Font("TimesRoman", Font.BOLD, 30);

    int[] snakePosX;
    int[] snakePosY;
    int snakeLength;

    Food food;
    int foodEaten;

    private long lastInputTime = 0;
    private long inputCooldownMillis = 300;

    char direction = 'R';
    boolean isMoving = false;
    final Timer timer = new Timer(150, this);

    public Graphics() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isMoving) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastInputTime >= inputCooldownMillis) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_LEFT:
                                if (direction != 'R') {
                                    direction = 'L';
                                }
                                break;
                            case KeyEvent.VK_RIGHT:
                                if (direction != 'L') {
                                    direction = 'R';
                                }
                                break;
                            case KeyEvent.VK_UP:
                                if (direction != 'D') {
                                    direction = 'U';
                                }
                                break;
                            case KeyEvent.VK_DOWN:
                                if (direction != 'U') {
                                    direction = 'D';
                                }
                                break;
                        }
                    }

                } else {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        start();
                    }
                }
            }
        });

        start();
    }

    protected void start() {
        snakePosX = new int[BOARD_SIZE];
        snakePosY = new int[BOARD_SIZE];
        snakeLength = 5;
        direction = 'R';
        foodEaten = 0;
        isMoving = true;
        spawnFood();
        timer.start();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        if (isMoving) {
            g.setColor(Color.RED);
            g.fillOval(food.getPosX(), food.getPosY(), TICK_SIZE, TICK_SIZE);

            
            for (int i = 0; i < snakeLength; i++) {
                if(i == 0){
                    g.setColor(Color.YELLOW);
                    g.fillRect(snakePosX[i], snakePosY[i], TICK_SIZE, TICK_SIZE);
                    continue;
                }
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(snakePosX[i], snakePosY[i], TICK_SIZE, TICK_SIZE);
            }

            g.setColor(Color.GREEN);
            g.setFont(new Font("TimesRoman", Font.BOLD, 25));
            g.drawString("Score:" + foodEaten, (WIDTH / 2) - 20, 30);
        } else {
            String scoreText = String.format("Score : %d... Press Space to Restart", foodEaten);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString(scoreText, (WIDTH - getFontMetrics(g.getFont()).stringWidth(scoreText)) / 2, HEIGHT / 2);
        }
    }

    protected void move() {
        for (int i = snakeLength; i > 0; i--) {
            snakePosX[i] = snakePosX[i - 1];
            snakePosY[i] = snakePosY[i - 1];
        }

        switch (direction) {
            case 'U' -> snakePosY[0] -= TICK_SIZE;
            case 'D' -> snakePosY[0] += TICK_SIZE;
            case 'L' -> snakePosX[0] -= TICK_SIZE;
            case 'R' -> snakePosX[0] += TICK_SIZE;
        }
    }

    protected void spawnFood() {
        food = new Food(snakePosX, snakePosY);
    }

    protected void eatFood() {
        if (snakePosX[0] == food.getPosX() && snakePosY[0] == food.getPosY()) {
            snakeLength++;
            foodEaten++;
            spawnFood();
        }
    }

    protected void collisionTest() {
        for (int i = snakeLength; i > 0; i--) {
            if ((snakePosX[0] == snakePosX[i]) && (snakePosY[0] == snakePosY[i])) {
                isMoving = false;
                break;
            }

            if (snakePosX[0] < 0 || snakePosX[0] > WIDTH - TICK_SIZE || snakePosY[0] < 0
                    || snakePosY[0] > HEIGHT - TICK_SIZE) {
                isMoving = false;
            }

            if (!isMoving) {
                timer.stop();
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isMoving) {
            move();
            collisionTest();
            eatFood();
        }
        repaint();
    }
}
