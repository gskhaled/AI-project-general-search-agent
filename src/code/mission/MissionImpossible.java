package mission;

import generic.QueuingFunction;
import generic.SearchProblem;

import javax.sound.midi.Soundbank;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
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

    public Node getInitialState() {
        return initialState;
    }

    public static int generateNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
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

    @Override
    public generic.Node stateSpace(generic.Node genericNode, Operator operator) {
        Node node = (mission.Node) genericNode;
        String state = node.getState();
        String[] currentStates = state.split(";");
        String[] ethanPosCarryDeaths = currentStates[0].split(",");
        short x = Short.valueOf(ethanPosCarryDeaths[0]);
        short y = Short.valueOf(ethanPosCarryDeaths[1]);
        short c = Short.valueOf(ethanPosCarryDeaths[2]);
        short deaths = Short.valueOf(ethanPosCarryDeaths[3]);

        short[] xPoss = copyArray(currentStates[1].split(","));
        short[] yPoss = copyArray(currentStates[2].split(","));
        short[] damages = copyArray(currentStates[3].split(","));
        short[] IMFstates = copyArray(currentStates[4].split(","));

        switch (operator) {
            case UP: {
                if (x == 0)
                    return null;
                if (node.getOperator() != null && node.getOperator() == Operator.DOWN)
                    return null;
                int pathCost = node.getPathCost();
                for (int i = 0; i < damages.length; i++) {
                    if (IMFstates[i] == 0) { // an IMF i am not carrying
                        if (damages[i] == 99 || damages[i] == 98) {
                            damages[i] = 100;
                            deaths += 1;
                            pathCost += IMFstates.length * 100;
                        } else if (damages[i] + 2 < 100) {
                            damages[i] += 2;
                            pathCost += 2;
                        }
                    } else if (IMFstates[i] == 1) // an IMF i am carrying
                        xPoss[i] = (short) (x - 1);
                }
                Node result = new Node((short) (x - 1), y, c, deaths, xPoss,
                        yPoss, damages, IMFstates, node, Operator.UP, node.getDepth() + 1, pathCost);
                return result;
            }
            case DOWN: {
                if ((x == grid.getRows() - 1))
                    return null;
                if (node.getOperator() != null && node.getOperator() == Operator.UP)
                    return null;
                int pathCost = node.getPathCost();
                for (int i = 0; i < damages.length; i++) {
                    if (IMFstates[i] == 0) { // an IMF i am not carrying
                        if (damages[i] == 99 || damages[i] == 98) {
                            damages[i] = 100;
                            deaths += 1;
                            pathCost += IMFstates.length * 100;
                        } else if (damages[i] + 2 < 100) {
                            damages[i] += 2;
                            pathCost += 2;
                        }
                    } else if (IMFstates[i] == 1) // an IMF i am carrying
                        xPoss[i] = (short) (x + 1);
                }
                Node result = new Node((short) (x + 1), y, c, deaths, xPoss,
                        yPoss, damages, IMFstates, node, Operator.DOWN, node.getDepth() + 1, pathCost);
                return result;
            }
            case RIGHT: {
                if (y == grid.getColumns() - 1 || (node.getOperator() != null && node.getOperator() == Operator.LEFT))
                    return null;
                else {
                    int pathCost = node.getPathCost();
                    for (int i = 0; i < damages.length; i++) {
                        if (IMFstates[i] == 0) { // an IMF i am not carrying
                            if (damages[i] == 99 || damages[i] == 98) {
                                damages[i] = 100;
                                deaths += 1;
                                pathCost += IMFstates.length * 100;
                            } else if (damages[i] + 2 < 100) {
                                damages[i] += 2;
                                pathCost += 2;
                            }
                        } else if (IMFstates[i] == 1) // an IMF i am carrying
                            yPoss[i] = (short) (y + 1);
                    }
                    Node result = new Node(x, (short) (y + 1), c, deaths, xPoss,
                            yPoss, damages, IMFstates, node, Operator.RIGHT, node.getDepth() + 1, pathCost);
                    return result;
                }
            }
            case LEFT: {
                if (y == 0 || (node.getOperator() != null && node.getOperator() == Operator.RIGHT))
                    return null;
                else {
                    int pathCost = node.getPathCost();
                    for (int i = 0; i < damages.length; i++) {
                        if (IMFstates[i] == 0) { // an IMF i am not carrying
                            if (damages[i] == 99 || damages[i] == 98) {
                                damages[i] = 100;
                                deaths += 1;
                                pathCost += IMFstates.length * 100;
                            } else if (damages[i] + 2 < 100) {
                                damages[i] += 2;
                                pathCost += 2;
                            }
                        } else if (IMFstates[i] == 1) // an IMF i am carrying
                            yPoss[i] = (short) (y - 1);
                    }
                    Node result = new Node(x, (short) (y - 1), c, deaths, xPoss,
                            yPoss, damages, IMFstates, node, Operator.LEFT, node.getDepth() + 1, pathCost);
                    return result;
                }
            }

            case CARRY: {
                boolean imfCarried = false;
                if (node.getOperator() != null && node.getOperator() == Operator.DROP)
                    return null;
                for (int i = 0; i < xPoss.length; i++) {
                    if (xPoss[i] == x && yPoss[i] == y && IMFstates[i] == 0 && c > 0) {
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
                            pathCost += IMFstates.length * 100;
                        } else if (damages[i] + 2 < 100) {
                            damages[i] += 2;
                            pathCost += 2;
                        }
                    }
                if (imfCarried)
                    return new Node(x, y, c, deaths, xPoss,
                            yPoss, damages, IMFstates, node, Operator.CARRY, node.getDepth() + 1, pathCost);
                else
                    return null;
            }
            case DROP: {
                if (node.getOperator() != null && node.getOperator() == Operator.CARRY)
                    return null;
                if (c == grid.getC() || !(x == grid.getxSub() && y == grid.getySub()))
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
                            pathCost += IMFstates.length * 100;
                        } else if (damages[i] + 2 < 100) {
                            damages[i] += 2;
                            pathCost += 2;
                        }
                    }
                return new Node(x, y, c, deaths, xPoss, yPoss, damages, IMFstates, node,
                        Operator.DROP, node.getDepth() + 1, pathCost);
            }
            default:
                return null;
        }
    }

    @Override
    public Boolean goalTest(generic.Node genericNode) {
        Node node = (mission.Node) genericNode;
        String state = node.getState();
        String[] currentStates = state.split(";");
        String[] ethanPosCarryDamages = currentStates[0].split(",");
        short x = Short.valueOf(ethanPosCarryDamages[0]);
        short y = Short.valueOf(ethanPosCarryDamages[1]);
        short[] IMFstates = copyArray(currentStates[4].split(","));
        boolean allAreInSub = true;
        for (int i = 0; i < IMFstates.length; i++)
            if (IMFstates[i] != 2) {
                allAreInSub = false;
                break;
            }
        if (x == grid.getxSub() && y == grid.getySub() && allAreInSub)
            return true;
        return false;
    }

    @Override
    public String returnPath(generic.Node n) {
        Node curr = (Node) n;
        String state = curr.getState();
        String[] currentStates = state.split(";");
        String deaths = (currentStates[0].split(","))[3];
        String damages = currentStates[3];
        String res = "";
        Stack<String> stack = new Stack<String>();
        stack.push(";" + deaths + ";" + damages + ";" + SearchProblem.expandedNodes);
        boolean firstTime = true;
        while (curr.getParent() != null && curr.getParent().getOperator() != null) {
            if (firstTime) {
                stack.push(curr.getOperator().toString().toLowerCase());
                firstTime = false;
            } else
                stack.push(curr.getOperator().toString().toLowerCase() /*+ "(" + curr.getPathCost() + ") " + "(" + calculateSecondHeuristic(curr, true) + ")" */+ ",");
            curr = curr.getParent();
        }
        stack.push(curr.getOperator().toString().toLowerCase() /*+ "(" + curr.getPathCost() + ") " + "(" + calculateSecondHeuristic(curr, true) + ")" */+ ",");
        while (!stack.isEmpty())
            res += stack.pop();
        int s =0;
        for(int i = 0; i < damages.split(",").length; i++)
            s+= Integer.parseInt(damages.split(",")[i]);
        System.out.println("TOTAL DAMAGES " + s);
        return res;
    }

    @Override
    public int calculateFirstHeuristic(generic.Node n) {
        int cost = 0;
        String[] currentStates = n.getState().split(";");
        String[] ethanPosCarryDamages = currentStates[0].split(",");
        short xEthan = Short.valueOf(ethanPosCarryDamages[0]);
        short yEthan = Short.valueOf(ethanPosCarryDamages[1]);
        String IMFstates = currentStates[4];
        short[] xPoss = grid.getxPoss();
        short[] yPoss = grid.getyPoss();
        short[] damages = grid.getDamages();
        for (int i = 0; i < xPoss.length; i++) {
            int xDiff = Math.abs(xPoss[i] - xEthan);
            int yDiff = Math.abs(yPoss[i] - yEthan);
            int distance = xDiff + yDiff + 1; // manhattan distance + 1 for carry
            if (Integer.parseInt(IMFstates.split(",")[i]) == 0)
                if (damages[i] + (distance * 2) >= 100)
                    cost += 100;
                else
                    cost += damages[i] + (distance * 2);
        }
        return cost;
    }

    @Override
    public int calculateSecondHeuristic(generic.Node n, boolean print) {
        if(print) {
            int cost = 0;
            String[] currentStates = n.getState().split(";");
            String[] ethanPosCarryDamages = currentStates[0].split(",");
            short xEthan = Short.valueOf(ethanPosCarryDamages[0]);
            short yEthan = Short.valueOf(ethanPosCarryDamages[1]);
            short xSub = grid.getxSub();
            short ySub = grid.getySub();
            short c = Short.valueOf(ethanPosCarryDamages[2]);
            String IMFstates = currentStates[4];
            short[] xPoss = grid.getxPoss();
            short[] yPoss = grid.getyPoss();
            System.out.println(Arrays.asList(IMFstates.split(",")));
            int wasaltohom = Collections.frequency(Arrays.asList(IMFstates.split(",")), "1") + Collections.frequency(Arrays.asList(IMFstates.split(",")), "2");
            System.out.println("wasaltohom before loop " + wasaltohom + " and c is " + c);
            for(int i = wasaltohom; i < xPoss.length;) {
                int ontheway = 0;
                int damage = 0;
                while(ontheway <= c && i < xPoss.length) {
                    int xDiff = Math.abs(xPoss[i] - xEthan);
                    int yDiff = Math.abs(yPoss[i] - yEthan);
                    int distance = xDiff + yDiff + 1; // manhattan distance + 1 for carry
                    damage = distance * 2;
                    System.out.println("at i " + i + " damage is " + damage + " distance is " + distance + " & ontheway is " + ontheway);
                    cost += damage;
                    ontheway++;
                    if(i + 1 == xPoss.length)
                        break;
                    i++;
                }
                wasaltohom += ontheway;
                xEthan = xPoss[i];
                yEthan = yPoss[i];
                int xDiff = Math.abs(xEthan - xSub);
                int yDiff = Math.abs(yEthan - ySub);
                int distance = xDiff + yDiff + 1; // manhattan distance + 1 for carry
                damage = distance * 2 * (xPoss.length - wasaltohom);
                cost += damage;
                System.out.println("at i AFTER " + i + " damage is " + damage + " cost is " + cost);
                if(i + 1 == xPoss.length)
                    break;
                xEthan = xSub;
                yEthan = ySub;
            }
            return cost;
        }
        int cost = 0;
        String[] currentStates = n.getState().split(";");
        String[] ethanPosCarryDamages = currentStates[0].split(",");
        short xEthan = Short.valueOf(ethanPosCarryDamages[0]);
        short yEthan = Short.valueOf(ethanPosCarryDamages[1]);
        short xSub = grid.getxSub();
        short ySub = grid.getySub();
        short c = Short.valueOf(ethanPosCarryDamages[2]);
        String IMFstates = currentStates[4];
        short[] xPoss = grid.getxPoss();
        short[] yPoss = grid.getyPoss();
        int wasaltohom = Collections.frequency(Arrays.asList(IMFstates.split(",")), "1") + Collections.frequency(Arrays.asList(IMFstates.split(",")), "2");
        for(int i = wasaltohom; i < xPoss.length;) {
            int ontheway = 0;
            int damage = 0;
            while(ontheway <= c && i < xPoss.length) {
                int xDiff = Math.abs(xPoss[i] - xEthan);
                int yDiff = Math.abs(yPoss[i] - yEthan);
                int distance = xDiff + yDiff + 1; // manhattan distance + 1 for carry
                damage = distance * 2;
                cost += damage;
                ontheway++;
                if(i + 1 == xPoss.length)
                    break;
                i++;
            }
            wasaltohom += ontheway;
            xEthan = xPoss[i];
            yEthan = yPoss[i];
            int xDiff = Math.abs(xEthan - xSub);
            int yDiff = Math.abs(yEthan - ySub);
            int distance = xDiff + yDiff + 1; // manhattan distance + 1 for carry
            damage = distance * 2 * (xPoss.length - wasaltohom);
            cost += damage;
            if(i + 1 == xPoss.length)
                break;
            xEthan = xSub;
            yEthan = ySub;
        }
        return cost;
    }

    public short[] copyArray(String[] arr) {
        short[] res = new short[arr.length];
        for (int i = 0; i < arr.length; i++)
            res[i] = Short.valueOf(arr[i]);
        return res;
    }

    public Operator[] getOperators() {
        return operators;
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

    public static void main(String[] args) {
        //String grid = genGrid();
        //System.out.println(grid);
        Grid grid1 = new Grid("5,5;2,1;1,0;1,3,4,2,4,1,3,1;54,31,39,98;1");
        //Grid grid1 = new Grid(grid);
        System.out.println(grid1.toString());
        //"5,5;2,1;1,0;1,3,4,2,4,1,3,1;54,31,39,98;2"
        //"15,15;5,10;14,14;0,0,0,1,0,2,0,3,0,4,0,5,0,6,0,7,0,8;81,13,40,38,52,63,66,36,13;1"
        System.out.println(solve("5,5;2,1;1,0;1,3,4,2,4,1,3,1;54,31,39,98;1", "ID", false));

    }
}
