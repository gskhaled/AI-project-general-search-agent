package mission;

import generic.QueuingFunction;
import generic.SearchProblem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MissionImpossible extends SearchProblem {

    private Grid grid;
    private Node initialState;
    private Operator[] operators;

    public MissionImpossible(Grid grid, Node initialState, Operator[] operators) {
        this.grid = grid;
        this.initialState = initialState;
        this.operators = operators;
    }

    @Override
    public generic.Node stateSpace(generic.Node genericNode, Operator operator) {
        Node node = (mission.Node) genericNode;
        short[] damages = copyArray(node.getDamages());
        short[] xPoss = copyArray(node.getxPoss());
        short[] yPoss = copyArray(node.getyPoss());
        short[] IMFstates = copyArray(node.getIMFstates());
        short deaths = node.getDeaths();
        short c = node.getC();

        switch (operator) {
            case UP: {
                if (node.getX() == 0)
                    return null;
                if (node.getOperator() != null && node.getOperator() == Operator.DOWN)
                    return null;
                int pathCost = node.getPathCost();
                for (int i = 0; i < damages.length; i++) {
                    if (IMFstates[i] == 0) { // an IMF i am not carrying
                        if (damages[i] == 99 || damages[i] == 98) {
                            damages[i] = 100;
                            deaths += 1;
                            pathCost += 100;
                        } else if (damages[i] + 2 < 100) {
                            damages[i] += 2;
                            pathCost += 2;
                        }
                    } else if (IMFstates[i] == 1) // an IMF i am carrying
                        xPoss[i] = (short) (node.getX() - 1);
                }
                Node result = new Node((short) (node.getX() - 1), node.getY(), node.getC(), deaths, xPoss,
                        yPoss, damages, IMFstates, node, Operator.UP, node.getDepth() + 1, pathCost);
                return result;
            }
            case DOWN: {
                if ((node.getX() == grid.getRows() - 1))
                    return null;
                if (node.getOperator() != null && node.getOperator() == Operator.UP)
                    return null;
                int pathCost = node.getPathCost();
                for (int i = 0; i < damages.length; i++) {
                    if (IMFstates[i] == 0) { // an IMF i am not carrying
                        if (damages[i] == 99 || damages[i] == 98) {
                            damages[i] = 100;
                            deaths += 1;
                            pathCost += 100;
                        } else if (damages[i] + 2 < 100) {
                            damages[i] += 2;
                            pathCost += 2;
                        }
                    } else if (IMFstates[i] == 1) // an IMF i am carrying
                        xPoss[i] = (short) (node.getX() + 1);
                }
                Node result = new Node((short) (node.getX() + 1), node.getY(), node.getC(), deaths, xPoss,
                        yPoss, damages, IMFstates, node, Operator.DOWN, node.getDepth() + 1, pathCost);
                return result;
            }
            case LEFT: {
                if (node.getY() == 0 || (node.getOperator() != null && node.getOperator() == Operator.RIGHT))
                    return null;
                else {
                    int pathCost = node.getPathCost();
                    for (int i = 0; i < damages.length; i++) {
                        if (IMFstates[i] == 0) { // an IMF i am not carrying
                            if (damages[i] == 99 || damages[i] == 98) {
                                damages[i] = 100;
                                deaths += 1;
                                pathCost += 100;
                            } else if (damages[i] + 2 < 100) {
                                damages[i] += 2;
                                pathCost += 2;
                            }
                        } else if (IMFstates[i] == 1) // an IMF i am carrying
                            yPoss[i] = (short) (node.getY() - 1);
                    }
                    Node result = new Node(node.getX(), (short) (node.getY() - 1), node.getC(), deaths, xPoss,
                            yPoss, damages, IMFstates, node, Operator.LEFT, node.getDepth() + 1, pathCost);
                    return result;
                }
            }
            case RIGHT: {
                if (node.getY() == grid.getColumns() || (node.getOperator() != null && node.getOperator() == Operator.LEFT))
                    return null;
                else {
                    int pathCost = node.getPathCost();
                    for (int i = 0; i < damages.length; i++) {
                        if (IMFstates[i] == 0) { // an IMF i am not carrying
                            if (damages[i] == 99 || damages[i] == 98) {
                                damages[i] = 100;
                                deaths += 1;
                                pathCost += 100;
                            } else if (damages[i] + 2 < 100) {
                                damages[i] += 2;
                                pathCost += 2;
                            }
                        } else if (IMFstates[i] == 1) // an IMF i am carrying
                            yPoss[i] = (short) (node.getY() + 1);
                    }
                    Node result = new Node(node.getX(), (short) (node.getY() + 1), node.getC(), deaths, xPoss,
                            yPoss, damages, IMFstates, node, Operator.RIGHT, node.getDepth() + 1, pathCost);
                    return result;
                }
            }
            case CARRY: {
                boolean imfCarried = false;
                if (node.getOperator() != null && node.getOperator() == Operator.DROP)
                    return null;
                for (int i = 0; i < xPoss.length; i++) {
                    if (xPoss[i] == node.getX() && yPoss[i] == node.getY() && IMFstates[i] == 0 && c > 0) {
                        c -= 1;
                        IMFstates[i] = 1;
                        imfCarried = true;
                    }
                }
                int pathCost = node.getPathCost();
                for (int i = 0; i < damages.length; i++)
                    if (IMFstates[i] == 0) { // an IMF i am not carrying
                        if (damages[i] == 99 || damages[i] == 98) {
                            damages[i] = 100;
                            deaths += 1;
                            pathCost += 100;
                        } else if (damages[i] + 2 < 100) {
                            damages[i] += 2;
                            pathCost += 2;
                        }
                    }
                if (imfCarried)
                    return new Node(node.getX(), node.getY(), c, deaths, xPoss,
                            yPoss, damages, IMFstates, node, Operator.CARRY, node.getDepth() + 1, pathCost);
                else
                    return null;
            }
            case DROP: {
                if (node.getOperator() != null && node.getOperator() == Operator.CARRY)
                    return null;
                if (node.getC() == grid.getC() || !(node.getX() == grid.getxSub() && node.getY() == grid.getySub()))
                    return null;
                for (int i = 0; i < xPoss.length; i++)
                    if (xPoss[i] == grid.getxSub() && yPoss[i] == grid.getySub() && IMFstates[i] == 1) {
                        IMFstates[i] = 2;
                        c += 1;
                    }
                int pathCost = node.getPathCost();
                for (int i = 0; i < damages.length; i++)
                    if (IMFstates[i] == 0) {
                        if (damages[i] == 99 || damages[i] == 98) {
                            damages[i] = 100;
                            deaths += 1;
                            pathCost += 100;
                        } else if (damages[i] + 2 < 100) {
                            damages[i] += 2;
                            pathCost += 2;
                        }
                    }
                return new Node(node.getX(), node.getY(), c, deaths, xPoss, yPoss, damages, IMFstates, node,
                        Operator.DROP, node.getDepth() + 1, pathCost);
            }
            default:
                return null;
        }
    }

    public short[] copyArray(short[] arr) {
        short[] res = new short[arr.length];
        for (int i = 0; i < arr.length; i++)
            res[i] = arr[i];
        return res;
    }

    public Grid getGrid() {
        return grid;
    }

    @Override
    public Boolean goalTest(generic.Node genericNode) {
        Node node = (mission.Node) genericNode;
        boolean allAreInSub = true;
        for (int i = 0; i < node.getIMFstates().length; i++)
            if (node.getIMFstates()[i] != 2)
                allAreInSub = false;
        if (node.getX() == grid.getxSub() && node.getY() == grid.getySub() && allAreInSub)
            return true;
        return false;
    }

    public Node getInitialState() {
        return initialState;
    }

    public Operator[] getOperators() {
        return operators;
    }

    @Override
    public String returnPath(generic.Node n) {
        Node curr = (Node) n;
        String res = "";
        Stack<String> stack = new Stack<String>();
        stack.push(";" + curr.getDeaths() + ";" + convertArrayToString(curr.getDamages()) + ";" + SearchProblem.expandedNodes);
        boolean firstTime = true;
        while (curr.getParent() != null && curr.getParent().getOperator() != null) {
            if (firstTime) {
                stack.push(curr.getOperator().toString().toLowerCase());
                firstTime = false;
            } else
                stack.push(curr.getOperator().toString().toLowerCase() + ",");
            curr = curr.getParent();
        }
        stack.push(curr.getOperator().toString().toLowerCase() +  ",");
        while (!stack.isEmpty())
            res += stack.pop();
        return res;
    }

    public static String convertArrayToString(short[] arr) {
        String res = "";
        for (int i = 0; i < arr.length; i++) {
            if (i < arr.length - 1)
                res += arr[i] + ",";
            else
                res += arr[i];
        }
        return res;
    }

    public static int generateNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static String genGrid() {
        String res = "";
        int height = generateNumber(5, 15);
        res += height + ",";
        int width = generateNumber(5, 15);
        res += width + ";";

        int xEthan = generateNumber(0, height - 1);
        res += xEthan + ",";
        int yEthan = generateNumber(0, width - 1);
        res += yEthan + ";";

        int xSub = generateNumber(0, height - 1);
        int ySub = generateNumber(0, width - 1);
        while (xSub == xEthan && ySub == yEthan) {
            xSub = generateNumber(0, height - 1);
            ySub = generateNumber(0, width - 1);
        }
        res += xSub + ",";
        res += ySub + ";";

        int agents = generateNumber(5, 10);
        ArrayList<String> xy = new ArrayList<String>();
        int[] xPoss = new int[agents];
        int[] yPoss = new int[agents];
        for (int i = 1; i <= agents; i++) {
            int xPos = generateNumber(0, height - 1);
            int yPos = generateNumber(0, width - 1);
            if (xy.contains(xPos + "," + yPos) || (xPos == xEthan && yPos == yEthan) || (xPos == xSub && yPos == ySub))
                i--;
            else {
                xy.add(xPos + "," + yPos);
                res += xPos + "," + yPos;
                xPoss[i - 1] = xPos;
                yPoss[i - 1] = yPos;
                if (i == agents)
                    res += ";";
                else
                    res += ",";
            }
        }

        int[] damages = new int[agents];
        for (int i = 1; i <= agents; i++) {
            int damage = generateNumber(1, 99);
            res += damage;
            damages[i - 1] = damage;
            if (i == agents)
                res += ";";
            else
                res += ",";
        }

        int c = generateNumber(1, agents);
        res += c;
//        print(visualise(height, width, xEthan, yEthan, xSub, ySub, imfs));
        return res;
    }

    public static String solve(String grid, String strategy, boolean visualize) {
        Grid g = new Grid(grid);
        short[] IMFstates = new short[g.getDamages().length];
        Node initialState = new Node(g.getxEthan(), g.getyEthan(), g.getC(), (short) 0,
                g.getxPoss(), g.getyPoss(), g.getDamages(), IMFstates, null,
                null, 0, 0);

        SearchProblem MI = new MissionImpossible(g, initialState, Operator.values());
        QueuingFunction qingFun;

        switch (strategy) {
            case "DF":
                qingFun = QueuingFunction.ENQUEUE_AT_FRONT;
                break;
            case "ID":
                qingFun = QueuingFunction.ENQUEUE_AT_FRONT_IDS;
                break;
            case "UC":
                qingFun = QueuingFunction.ORDERED_INSERT;
                break;
            case "GR1":
                qingFun = QueuingFunction.HEURISTIC_FN1;
                break;
            case "GR2":
                qingFun = QueuingFunction.HEURISTIC_FN2;
                break;
            case "AS1":
                qingFun = QueuingFunction.EVALUATION_FN1;
                break;
            case "AS2":
                qingFun = QueuingFunction.EVALUATION_FN2;
                break;
            case "BF":
            default:
                qingFun = QueuingFunction.ENQUEUE_AT_END;
        }
        String solution = search(MI, qingFun);
        return solution;
    }

    public static void main(String[] args) {
        //String grid = genGrid();
        //System.out.println(grid);
        Grid grid1 = new Grid("10,10;6,3;4,8;9,1,2,4,4,0,3,9,6,4,3,4,0,5,1,6,1,9;97,49,25,17,94,3,96,35,98;3");
        //Grid grid1 = new Grid(grid);
        System.out.println(grid1.toString());
        //"5,5;2,1;1,0;1,3,4,2,4,1,3,1;54,31,39,98;2"
        System.out.println(solve("10,10;6,3;4,8;9,1,2,4,4,0,3,9,6,4,3,4,0,5,1,6,1,9;97,49,25,17,94,3,96,35,98;3", "UC", false));
    }
}
