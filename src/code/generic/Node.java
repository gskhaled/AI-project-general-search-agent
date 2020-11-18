package generic;

import mission.Operator;

import java.util.Arrays;

public abstract class Node {
    String state;
    Node parent;
    Operator operator;
    int depth;
    int pathCost;
    int heuristicCost;

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

    public String getState() {
        return state;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(int cost) {
        this.heuristicCost = cost;
    }

    public abstract String formulateNodeToString();

}
