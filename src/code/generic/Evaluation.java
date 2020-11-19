package generic;

import java.util.Comparator;

public class Evaluation implements Comparator<Node> {
    @Override
    public int compare(Node a, Node b) {
        return (a.getHeuristicCost() + a.getPathCost()) < (b.getHeuristicCost() + b.getPathCost()) ? -1
                : (a.getHeuristicCost() + a.getPathCost()) == (b.getHeuristicCost() + b.getPathCost()) ? 0 : 1;
    }
}
