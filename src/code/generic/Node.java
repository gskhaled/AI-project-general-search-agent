package generic;

import mission.Operator;

import java.util.Arrays;

public abstract class Node implements Comparable {
    String state;
    Node parent;
    Operator operator;
    int depth;
    int pathCost;

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

    public abstract String formulateNodeToString();

}
