package generic;

import mission.Operator;

public abstract class Node {
    String state;
    Node parent;
    Operator operator;
    int depth;
    int pathCost;
}
