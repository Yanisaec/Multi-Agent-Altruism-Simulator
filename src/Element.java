public class Element {
    private int x;
    private int y;

    public Element(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void moveTowardDirection(double[] direction, double speed) {
        int dx = (int)(direction[0] * speed);
        int dy = (int)(direction[1] * speed);
        this.move(dx, dy);
    }

    public double distanceToElement(Element element) {
        return Math.sqrt(Math.pow((this.x - element.x),2) + Math.pow((this.y - element.y),2));
    }

    public double[] directionNormed(Element element) {
        double direction_norm;
        double[] direction = new double[2];
        direction[0] = element.x - this.x;
        direction[1] = element.y - this.y;
        if (direction[0] + direction[1] != 0) {
            direction_norm = Math.sqrt(Math.pow(direction[0],2) + Math.pow(direction[1],2)) / 2;
        }
        else {
            direction_norm = 1;
        }
        direction[0] /= direction_norm;
        direction[1] /= direction_norm;
        return direction;
    }
}
