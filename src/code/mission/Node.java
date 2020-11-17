package mission;

import java.util.Arrays;

public class Node extends generic.Node {
    private short x;
    private short y;
    private short[] xPoss;
    private short[] yPoss;
    private short[] damages;
    private short[] IMFstates;
    private short c;
    private short deaths;
    private Node parent;
    private Operator operator;
    private int depth;
    private int pathCost;
    private String state;

    public Node(short x, short y, short c, short deaths, short[] xPoss, short[] yPoss, short[] damages, short[] IMFstates, Node parent, Operator operator, int depth, int pathCost) {
        this.parent = parent;
        this.state = "";
        this.state += x + "," + y + "," + c + "," + deaths + ";";
        String xPositions = "";
        String yPositions = "";
        String damagesString = "";
        String IMFstatesString = "";
        int loopCounter = xPoss.length - 1;
        for (int i = 0; i < loopCounter; i++) {
            xPositions += xPoss[i] + ",";
            yPositions += yPoss[i] + ",";
            damagesString += damages[i] + ",";
            IMFstatesString += IMFstates[i] + ",";

        }
        xPositions += xPoss[loopCounter] + ";";
        yPositions += yPoss[loopCounter] + ";";
        damagesString += damages[loopCounter] + ";";
        IMFstatesString += IMFstates[loopCounter];
        this.operator = operator;
        this.depth = depth;
        this.pathCost = pathCost;
        this.state+=xPositions;
        this.state+=yPositions;
        this.state+=damagesString;
        this.state+=IMFstatesString;

    }

//    public Node(short x, short y, short c, short deaths, short[] xPoss, short[] yPoss, short[] damages, short[] IMFstates, Node parent, Operator operator, int depth, int pathCost) {
//        this.x = x;
//        this.y = y;
//        this.c = c;
//        this.deaths = deaths;
//        this.xPoss = xPoss;
//        this.yPoss = yPoss;
//        this.damages = damages;
//        this.IMFstates = IMFstates;
//        this.parent = parent;
//        this.operator = operator;
//        this.depth = depth;
//        this.pathCost = pathCost;
//    }

    public String formulateNodeToString() {
        String[] currentStates=this.getState().split(";");
        String[] ethanPosCarryDamages = currentStates[0].split(",");
        short x=Short.valueOf(ethanPosCarryDamages[0]);
        short y=Short.valueOf(ethanPosCarryDamages[1]);
        short c=Short.valueOf(ethanPosCarryDamages[2]);
        String IMFstates = currentStates[4];
        return x + "," + y + ";" + IMFstates + ";" + c;
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
    public String getState(){
        return this.state;
    }
    public static void main(String[] args) {

    }


    @Override
    public int compareTo(Object o) {
        return this.getPathCost()<((Node)o).getPathCost()?-1:((Node)o).getPathCost()==this.getPathCost()?0:1;    }
}