import java.util.Random;

public class Food {

    private final int posX;
    private final int posY;
    int[] snakePosX;
    int[] snakePosY;

    public Food(int[] snakePosX, int[] snakePosY) {
        int[] xy = new int[2];
        xy = generatePosXY(Graphics.WIDTH, snakePosX, snakePosY);
        posX = xy[0];
        posY = xy[1];
        this.snakePosX = snakePosX;
        this.snakePosY = snakePosY;
    }

    private int[] generatePosXY(int size, int[] snakePosX, int[] snakePosY) {

        Random random = new Random();
        int[] r = new int[2];
        r[0] = random.nextInt(size / Graphics.TICK_SIZE) * Graphics.TICK_SIZE;
        r[1] = random.nextInt(size / Graphics.TICK_SIZE) * Graphics.TICK_SIZE;

        for (int i = 0; i < snakePosX.length; i++) {
            if (snakePosX[i] == r[0] && snakePosY[i] == r[1]) {
                r = generatePosXY(size, snakePosX, snakePosY);
                break;
            }
        }

        return r;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
