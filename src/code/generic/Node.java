package generic;



public abstract class Node implements Comparable<Node> {
    String state;
    Node parent;
    String operator;
    int depth;
    int pathCost;
    int heuristicCost;
    int priority;

    public Node getParent() {
        return parent;
    }

    public String getOperator() {
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

    public int getPriority() { return priority; }

    public void setPriority(int priority){ this.priority = priority; }

    public abstract String formulateNodeToString();

    @Override
    public int compareTo(generic.Node o) {
        return this.getPriority() <  o.getPriority() ? -1 : o.getPriority() == this.getPriority() ? 0 : 1;
    }
}
