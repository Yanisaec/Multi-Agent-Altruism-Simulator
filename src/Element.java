public class Element {
    protected double x;
    protected double y;

    public Element(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double distanceToElement(Element element) {
        return Math.sqrt(Math.pow((this.x - element.x),2) + Math.pow((this.y - element.y),2));
    }

    public double[] getDirectionNormedToward(Element element) {
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

    public double[] getRandomDirectionCanva(double height, double width) {
        double random_x = Math.random()*width;
        double random_y = Math.random()*height;
        Element point = new Element(random_x, random_y);
        double[] random_direction = this.getDirectionNormedToward(point);
        // double[] random_direction = new double[]{Math.random()*2 - 1.0, Math.random()*2 - 1.0};
        return random_direction;
    }

    public double[] getRandomDirection() {
        double[] random_direction = new double[]{Math.random()*2 - 1.0, Math.random()*2 - 1.0};
        return random_direction;
    }
}
