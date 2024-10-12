public class Element {
    protected double x;
    protected double y;
    protected double simulation_height;
    protected double simulation_width;

    public Element(double x, double y, double height, double width) {
        this.x = x;
        this.y = y;
        this.simulation_height = height;
        this.simulation_width = width;
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

    public double[] getDirectionNormedToward(double[] old_direction, Element element) {
        double direction_norm;
        double[] direction = new double[2];
        direction[0] = element.x - this.x;
        direction[1] = element.y - this.y;
        if (direction[0] == 0 && direction[1] == 0) {
            return getRandomDirection();
        }
        direction_norm = Math.sqrt(Math.pow(direction[0],2) + Math.pow(direction[1],2)) / 2;
        direction[0] /= direction_norm;
        direction[1] /= direction_norm;
        return direction;
    }

    public double[] getDirectionNormedOpposite(double[] old_direction, Element element) {
        double direction_norm;
        double[] direction = new double[2];
        direction[0] = -(element.x - this.x);
        direction[1] = -(element.y - this.y);
        if (direction[0] == 0 && direction[1] == 0) {
            return getRandomDirection();
        }
        direction_norm = Math.sqrt(Math.pow(direction[0],2) + Math.pow(direction[1],2)) / 2;
        direction[0] /= direction_norm;
        direction[1] /= direction_norm;
        return direction;
    }

    public double[] getRandomDirectionCanva(double height, double width) {
        double[] base_direction = new double[]{0,0};
        double random_x = Math.random()*width;
        double random_y = Math.random()*height;
        Element point = new Element(random_x, random_y, height, width);
        double[] random_direction = this.getDirectionNormedToward(base_direction, point);
        // double[] random_direction = new double[]{Math.random()*2 - 1.0, Math.random()*2 - 1.0};
        return random_direction;
    }

    public double[] getRandomDirection() {
        double[] random_direction = new double[]{Math.random()*2 - 1.0, Math.random()*2 - 1.0};
        return random_direction;
    }

    public double getDirectionNorm(double[] direction) {
        double direction_norm;
        direction_norm = Math.sqrt(Math.pow(direction[0],2) + Math.pow(direction[1],2)) / 2;
        return direction_norm;
    }

    public double[] normDirection(double[] direction) {
        double direction_norm = getDirectionNorm(direction);
        direction[0] /= direction_norm;
        direction[1] /= direction_norm;
        return direction;
    }

    public double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }   
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
