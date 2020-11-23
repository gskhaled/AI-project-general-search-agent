package generic;

import java.util.HashMap;
import java.util.PriorityQueue;

public abstract class SearchProblem {
    private static HashMap<String, String> repeatedStates = new HashMap<String, String>();
    private String[] operators;
    public static int expandedNodes = 0;
    public Node initialState;
    /**
     * @param node a node object which the operator will be applied to
     * @param operator one of pre-set operators representing the operation done to the node
     * @return returns a node which is equivelent to the node after the operation is done to it
     */
    public abstract Node transitionFunction(Node node, String operator);

    // returns true if a given node passes the goal test, otherwise false

    /**
     * @param node a node object which is checked upon if it satisfies the goal state
     * @return true if this node satisfies the goalTest, false otherwise
     */
    public abstract Boolean goalTest(Node node);

    /**
     * @return returns the initial state of our search problem
     */
    public abstract Node getInitialState();

    /**
     * @return returns the set of operators of our search problems
     */
    public abstract String[] getOperators();

    /**
     * @param n node object which passes the goalTest
     * @return String representing the path from the initial state to that node
     */
    public abstract String returnPath(Node n);

    /**
     * @param problem Instance of search problem which is needed to search upon
     * @param queuingFunction The queueing operation done in that search criteria
     * @return the operations done to reach the goal state
     */
    public static String search(SearchProblem problem, QueuingFunction queuingFunction) {
        String res = "";
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Node initialState = problem.getInitialState();
        queue.add(initialState);
        switch (queuingFunction) {
            case ENQUEUE_AT_END: {
                int priority = 0;
                while (!queue.isEmpty()) {
                    Node curr = queue.remove();
                    if (problem.goalTest(curr)) {
                        repeatedStates.clear();
                        return problem.returnPath(curr);
                    }
                    expandedNodes++;
                    for (int i = 0; i < problem.getOperators().length; i++) {
                        Node node = problem.transitionFunction(curr, problem.getOperators()[i]);

                        if (node != null) {
                            String nodeString = node.formulateNodeToString();
                            if (repeatedStates.get(nodeString) == null) {
                                repeatedStates.put(nodeString, nodeString);
                                node.setPriority(priority);
                                queue.add(node);
                                priority += 1;
                            }
                        }
                    }
                }
            }
            break;
            case ENQUEUE_AT_FRONT: {
                int priority = Integer.MAX_VALUE;
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    if (problem.goalTest(curr)) {
                        repeatedStates.clear();
                        return problem.returnPath(curr);
                    }
                    expandedNodes++;
                    for (int i = problem.getOperators().length - 1; i >= 0; i--) {
                        Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
                        if (node != null) {
                            String nodeString = node.formulateNodeToString();
                            if (repeatedStates.get(nodeString) == null) {
                                repeatedStates.put(nodeString, nodeString);
                                node.setPriority(priority);
                                queue.add(node);
                                priority -= 1;
                            }
                        }
                    }
                }
            }
            break;
            case ENQUEUE_AT_FRONT_IDS: {
                int priority;
                int currentDepth = 0;
                Node node;
                Node curr;
                String nodeString;
                while (true) {
                    priority = Integer.MAX_VALUE;
                    while (!queue.isEmpty()) {
                        curr = queue.poll();
                        if (problem.goalTest(curr)) {
                            repeatedStates.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        if (curr.getDepth() < currentDepth) {
                            for (int i = problem.getOperators().length - 1; i >= 0; i--) {
                                node = problem.transitionFunction(curr, problem.getOperators()[i]);

                                if (node != null) {
                                    nodeString = node.formulateNodeToString();
                                    if (repeatedStates.get(nodeString) == null) {
                                        repeatedStates.put(nodeString, nodeString);
                                        node.setPriority(priority);
                                        queue.add(node);
                                        priority -= 1;
                                    }
                                }
                            }
                        }

                    }
                    currentDepth += 10;
                    queue.add(problem.getInitialState());
                    repeatedStates.clear();
                }
            }
            case ORDERED_INSERT: {
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (repeatedStates.get(currString) == null) {
                        repeatedStates.put(currString, currString);
                        if (problem.goalTest(curr)) {
                            repeatedStates.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.transitionFunction(curr, problem.getOperators()[i]);

                            if (node != null) {
                                node.setPriority(node.getPathCost());
                                queue.add(node);
                            }
                        }
                    }
                }
            }

            break;
            case HEURISTIC_FN1: {
                int heuristic = problem.calculateFirstHeuristic(initialState);
                initialState.setHeuristicCost(heuristic);
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (repeatedStates.get(currString) == null) {
                        repeatedStates.put(currString, currString);
                        if (problem.goalTest(curr)) {
                            repeatedStates.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
                            if (node != null) {
                                heuristic = problem.calculateFirstHeuristic(node);
                                node.setHeuristicCost(heuristic);
                                node.setPriority(node.getHeuristicCost());
                                queue.add(node);
                            }
                        }

                    }
                }
            }
            break;
            case HEURISTIC_FN2: {
                int heuristic = problem.calculateSecondHeuristic(initialState);
                initialState.setHeuristicCost(heuristic);
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (repeatedStates.get(currString) == null) {
                        repeatedStates.put(currString, currString);
                        if (problem.goalTest(curr)) {
                            repeatedStates.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
                            if (node != null) {
                                heuristic = problem.calculateSecondHeuristic(node);
                                node.setHeuristicCost(heuristic);
                                node.setPriority(node.getHeuristicCost());
                                queue.add(node);

                            }
                        }

                    }
                }
            }
            break;
            case EVALUATION_FN1: {
                int heuristic = problem.calculateFirstHeuristic(initialState);
                initialState.setHeuristicCost(heuristic);
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (repeatedStates.get(currString) == null) {
                        repeatedStates.put(currString, currString);
                        if (problem.goalTest(curr)) {
                            repeatedStates.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
                            if (node != null) {
                                heuristic = problem.calculateFirstHeuristic(node);
                                node.setHeuristicCost(heuristic);
                                node.setPriority(node.getPathCost() + node.getHeuristicCost());
                                queue.add(node);

                            }
                        }
                    }
                }
            }
            break;
            case EVALUATION_FN2: {
                int heuristic = problem.calculateSecondHeuristic(initialState);
                initialState.setHeuristicCost(heuristic);
                while (!queue.isEmpty()) {
                    Node curr = queue.poll();
                    String currString = curr.formulateNodeToString();
                    if (repeatedStates.get(currString) == null) {
                        repeatedStates.put(currString, currString);
                        if (problem.goalTest(curr)) {
                            repeatedStates.clear();
                            return problem.returnPath(curr);
                        }
                        expandedNodes++;
                        for (int i = 0; i < problem.getOperators().length; i++) {
                            Node node = problem.transitionFunction(curr, problem.getOperators()[i]);
                            if (node != null) {
                                heuristic = problem.calculateSecondHeuristic(node);
                                node.setHeuristicCost(heuristic);
                                node.setPriority(node.getPathCost() + node.getHeuristicCost());
                                queue.add(node);

                            }
                        }
                    }
                }
            }
            break;
            default:
                return "";
        }
        return res;

    }


    public abstract int calculateFirstHeuristicOld(Node node);

    /**
     * @param node Node object which we are going to calculate its heuristic cost
     * @return heuristic cost of that node using first heuristic function
     */
    public abstract int calculateFirstHeuristic(Node node);
    /**
     * @param node Node object which we are going to calculate its heuristic cost
     * @return heuristic cost of that node using second heuristic function
     */
    public abstract int calculateSecondHeuristic(Node node);

    public abstract int calculateSecondHeuristicOld(Node node, boolean print);

}
