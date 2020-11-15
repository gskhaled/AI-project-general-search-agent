package generic;

import mission.Node;
import mission.Operator;

public abstract class SearchProblem {
    // returns state given grid, current node and operator to perform
    public abstract Node stateSpace(Node node, Operator operator);
    //returns true if a given node passes the goal test, otherwise false
    public abstract Boolean goalTest(Node node);
    // returns the solution to the search problem as a sequence of actions given the grid and queueing function
    public abstract String search(QueuingFunction queuingFunction);
}
