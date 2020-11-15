package generic;

import mission.Node;
import mission.Operator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class SearchProblem {
    private static HashMap<String, String> nodesPassed =  new HashMap<String, String>();;
    private static int expandedNodes = 0;

    // returns state given grid, current node and operator to perform
    public abstract Node stateSpace(Node node, Operator operator);

    //returns true if a given node passes the goal test, otherwise false
    public abstract Boolean goalTest(Node node);

    public abstract Node getInitialState();

    public abstract Operator[] getOperators();

    public static String returnPath(Node n) {
        if (n.getParent() == null || n.getParent().getOperator() == null)
            return n.getOperator() + "";
        else
            return returnPath(n.getParent()) + "," + n.getOperator().toString();
    }

    public static String formulateNodeToString(Node state) {
        return state.getX() + "," + state.getY() + ";" + Arrays.toString(state.getIMFstates()) + ";" + state.getC();
    }

    public static String ConvertArrayToString(short [] arr) {
        String res = "";
        for (int i = 0; i < arr.length; i++){
            if(i < arr.length - 1)
                res += arr[i] + ",";
            else
                res += arr[i];
        }
        return res;
    }

    public static String printArray(short[] arr) {
        String s = "[";
        for (int i = 0; i < arr.length; i++)
            s += arr[i] + ", ";
        s += "]";
        return s;
    }
    public static String search(SearchProblem problem, QueuingFunction queuingFunction) {

        String res = "";
        LinkedList<Node> queue = new LinkedList<Node>();
        queue.addLast(problem.getInitialState());
        while (!queue.isEmpty()) {
            Node curr = queue.removeFirst();
            String stringCurr = formulateNodeToString(curr);
            if (nodesPassed.get(stringCurr) == null) {
                nodesPassed.put(stringCurr, stringCurr);
                expandedNodes++;
                if (curr != null) {
                    if (problem.goalTest((curr)))
                        return returnPath(curr);
                    String s = "";
                    if (curr.getOperator() != null) {
                        s += expandedNodes + " " + curr.getOperator().toString() + " ";
                        s += "X: " + curr.getParent().getX() + "->" + curr.getX() + " ";
                        s += "Y: " + curr.getParent().getY() + "->" + curr.getY() + " ";
                        s += "c: " + curr.getC();
                        s += " xPos: " + printArray(curr.getxPoss());
                        s += " yPos: " + printArray(curr.getyPoss());
                        s += " health: " + printArray(curr.getDamages());
                        s += " depth: " + curr.getDepth();
                        s += " my parent: " + (curr.getParent().getOperator() == null ? " " : curr.getParent().getOperator().toString());
                        s += " my toString: " + stringCurr;
                    }
                    try {
                        FileWriter myWriter = new FileWriter("output.txt");
                        myWriter.write(s + '\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                  System.out.println(s);
//                  res += curr.getOperator()!=null ? curr.getOperator().toString() : "";
                    switch (queuingFunction) {
                        case ENQUEUE_AT_END: {
                           for(int i=0; i< problem.getOperators().length; i++){
                               Node node = problem.stateSpace(curr,problem.getOperators()[i]);
                               if(node != null)
                                   queue.addLast(node);

                           }
                        }
                        break;
                        case ENQUEUE_AT_FRONT: {
                            for(int i=0; i< problem.getOperators().length; i++){
                                Node node = problem.stateSpace(curr,problem.getOperators()[i]);
                                if(node != null)
                                    queue.addFirst(node);

                            }
                        }
                        case ENQUEUE_AT_FRONT_IDS:{

                        } break;
                        default:
                            return "";
                    }
                }
            }
        }
        return res;
    }
}
