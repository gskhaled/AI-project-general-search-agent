package mission;

public class Node extends generic.Node {
    private short x;
    private short y;
    private short [] xPoss;
    private short [] yPoss;
    private short [] damages;
    private short [] IMFstates;
    private short c;
    private short deaths;
    private Node parent;
    private Operator operator;
    private int depth;
    private int pathCost;

    public Node(short x, short y, short c, short deaths, short [] xPoss, short [] yPoss, short [] damages, short [] IMFstates, Node parent, Operator operator, int depth, int pathCost) {
        this.x = x;
        this.y = y;
        this.c = c;
        this.deaths = deaths;
        this.xPoss = xPoss;
        this.yPoss = yPoss;
        this.damages = damages;
        this.IMFstates  = IMFstates;
        this.parent = parent;
        this.operator = operator;
        this.depth = depth;
        this.pathCost = pathCost;
    }

    public Node getParent() {
        return parent;
    }

    public Operator getOperator() {
        return operator;
    }

    public int getDepth() {
        return depth;
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public short getC() {
        return c;
    }

    public void setC(short c) {
        this.c = c;
    }

    public short[] getIMFstates() {
        return IMFstates;
    }

    public void setIMFstates(short[] IMFstates) {
        this.IMFstates = IMFstates;
    }

    public short[] getxPoss() {
        return xPoss;
    }

    public short[] getyPoss() {
        return yPoss;
    }

    public short[] getDamages() {
        return damages;
    }

    public short getDeaths() {
        return deaths;
    }

    public void setxPoss(short xPos, int i) {
        this.xPoss[i] = xPos;
    }

    public void setyPoss(short yPos, int i) {
        this.yPoss[i] = yPos;
    }

    public void setDamages(short damage, int i) {
        this.damages[i] = damage;
    }

    public static void main(String[] args) {

    }
}
