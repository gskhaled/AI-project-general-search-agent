package mission;

import generic.QueuingFunction;
import generic.SearchProblem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MissionImpossible extends SearchProblem {

	private Grid grid;
	private Node initialState;
	private int expandedNodes = 0;
	FileWriter myWriter;
	private HashMap<String, String> nodesPassed;

	public MissionImpossible(Grid grid, Node initialState, int expandedNodes) {
		this.grid = grid;
		this.initialState = initialState;
		this.expandedNodes = expandedNodes;
		this.nodesPassed = new HashMap<String, String>();
		try {
			myWriter = new FileWriter("output.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String solve (String grid, String strategy, boolean visualize) {
		Grid g = new Grid(grid);
		short[] IMFstates = new short[g.getDamages().length];
		Node initialState = new Node(g.getxEthan(), g.getyEthan(), g.getC(), (short) 0,
				g.getxPoss(), g.getyPoss(), g.getDamages(), IMFstates, null,
				null, 0, 0);

		MissionImpossible MI = new MissionImpossible(g, initialState, 0);
		QueuingFunction qingFun;

		switch (strategy) {
			case "DF":
			case "ID":
				qingFun = QueuingFunction.ENQUEUE_AT_FRONT;
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
		String solution = MI.search(qingFun);
		return solution;
	}

	@Override
	public Node stateSpace(Node node, Operator operator) {
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
				for (int i = 0; i < damages.length; i++) {
					if (IMFstates[i] == 0) { // an IMF i am not carrying
						damages[i] += 2;
						if (damages[i] >= 100) { // IMF died
							damages[i] = 100;
							deaths += 1;
						}
					} else if (IMFstates[i] == 1) // an IMF i am carrying
						xPoss[i] = (short) (node.getX() - 1);
				}
				Node result = new Node((short) (node.getX() - 1), node.getY(), node.getC(), deaths, xPoss,
						yPoss, damages, IMFstates, node, Operator.UP, node.getDepth() + 1, 0);
				return result;
			}
			case DOWN: {
				if ((node.getX() == grid.getRows() - 1))
					return null;
				if (node.getOperator() != null && node.getOperator() == Operator.UP)
					return null;
				for (int i = 0; i < damages.length; i++) {
					if (IMFstates[i] == 0) { // an IMF i am not carrying
						damages[i] += 2;
						if (damages[i] >= 100) { // IMF died
							damages[i] = 100;
							deaths += 1;
						}
					} else if (IMFstates[i] == 1) // an IMF i am carrying
						xPoss[i] = (short) (node.getX() + 1);
				}
				Node result = new Node((short) (node.getX() + 1), node.getY(), node.getC(), deaths, xPoss,
						yPoss, damages, IMFstates, node, Operator.DOWN, node.getDepth() + 1, 0);
				return result;
			}
			case LEFT: {
				if (node.getY() == 0 || (node.getOperator() != null && node.getOperator() == Operator.RIGHT))
					return null;
				else {
					for (int i = 0; i < damages.length; i++) {
						if (IMFstates[i] == 0) { // an IMF i am not carrying
							damages[i] += 2;
							if (damages[i] >= 100) { // IMF died
								damages[i] = 100;
								deaths += 1;
							}
						} else if (IMFstates[i] == 1) // an IMF i am carrying
							yPoss[i] = (short) (node.getY() - 1);
					}
					Node result = new Node(node.getX(), (short) (node.getY() - 1), node.getC(), deaths, xPoss,
							yPoss, damages, IMFstates, node, Operator.LEFT, node.getDepth() + 1, 0);
					return result;
				}
			}
			case RIGHT: {
				if (node.getY() == grid.getColumns() || (node.getOperator() != null && node.getOperator() == Operator.LEFT))
					return null;
				else {
					for (int i = 0; i < damages.length; i++) {
						if (IMFstates[i] == 0) { // an IMF i am not carrying
							damages[i] += 2;
							if (damages[i] >= 100) { // IMF died
								damages[i] = 100;
								deaths += 1;
							}
						} else if (IMFstates[i] == 1) // an IMF i am carrying
							yPoss[i] = (short) (node.getY() + 1);
					}
					Node result = new Node(node.getX(), (short) (node.getY() + 1), node.getC(), deaths, xPoss,
							yPoss, damages, IMFstates, node, Operator.RIGHT, node.getDepth() + 1, 0);
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
				for (int i = 0; i < damages.length; i++)
					if (IMFstates[i] == 0) { // an IMF i am not carrying
						damages[i] += 2;
						if (damages[i] >= 100) { // IMF died
							damages[i] = 100;
							deaths += 1;
						}
					}
				if (imfCarried)
					return new Node(node.getX(), node.getY(), c, deaths, xPoss,
							yPoss, damages, IMFstates, node, Operator.CARRY, node.getDepth() + 1, 0);
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
				for (int i = 0; i < damages.length; i++)
					if (IMFstates[i] == 0) {
						damages[i] += 2;
						if (damages[i] >= 100) {
							damages[i] = 100;
							deaths += 1;
						}
					}
				return new Node(node.getX(), node.getY(), c, deaths, xPoss, yPoss, damages, IMFstates, node,
						Operator.DROP, node.getDepth() + 1, 0);
			}
			default:
				return null;
		}
	}

	@Override
	public Boolean goalTest(Node node) {
		boolean allAreInSub = true;
//        System.out.println(node.getIMFstates().length);
		for (int i = 0; i < node.getIMFstates().length; i++)
			if (node.getIMFstates()[i] != 2)
				allAreInSub = false;
		if (node.getX() == grid.getxSub() && node.getY() == grid.getySub() && allAreInSub)
			return true;
		return false;
	}

	@Override
	public String search(QueuingFunction queuingFunction) {
		if(queuingFunction == QueuingFunction.ENQUEUE_AT_FRONT_IDS)
			search(0);
		String res = "";
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addLast(getInitialState());
		while (!queue.isEmpty()) {
			Node curr = queue.removeFirst();
			String stringCurr = formulateNodeToString(curr);
			if (nodesPassed.get(stringCurr) == null) {
				nodesPassed.put(stringCurr, stringCurr);
				expandedNodes++;
				if (curr != null) {
					if (goalTest((curr)))
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
						myWriter.write(s + '\n');
					} catch (IOException e) {
						e.printStackTrace();
					}
//                  System.out.println(s);
//                  res += curr.getOperator()!=null ? curr.getOperator().toString() : "";
					switch (queuingFunction) {
						case ENQUEUE_AT_END: {
							Node up = stateSpace(curr, Operator.UP);
							Node down = stateSpace(curr, Operator.DOWN);
							Node left = stateSpace(curr, Operator.LEFT);
							Node right = stateSpace(curr, Operator.RIGHT);
							Node carry = stateSpace(curr, Operator.CARRY);
							Node drop = stateSpace(curr, Operator.DROP);
							if (up != null)
								queue.addLast(up);
							if (down != null)
								queue.addLast(down);
							if (left != null)
								queue.addLast(left);
							if (right != null)
								queue.addLast(right);
							if (carry != null)
								queue.addLast(carry);
							if (drop != null)
								queue.addLast(drop);
						}
						break;
						case ENQUEUE_AT_FRONT: {
							Node up = stateSpace(curr, Operator.UP);
							Node down = stateSpace(curr, Operator.DOWN);
							Node left = stateSpace(curr, Operator.LEFT);
							Node right = stateSpace(curr, Operator.RIGHT);
							Node carry = stateSpace(curr, Operator.CARRY);
							Node drop = stateSpace(curr, Operator.DROP);
							if (drop != null)
								queue.addFirst(drop);
							if (carry != null)
								queue.addFirst(carry);
							if (right != null)
								queue.addFirst(right);
							if (left != null)
								queue.addFirst(left);
							if (down != null)
								queue.addFirst(down);
							if (up != null)
								queue.addFirst(up);
						}
						break;
						default:
							return "";
					}
				}
			}
		}
		return res;
	}

	public String search(int depth) {
		String res = "";
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addLast(getInitialState());
		while (!queue.isEmpty()) {
			Node curr = queue.removeFirst();
			if (curr.getDepth() < depth) {
				String stringCurr = formulateNodeToString(curr);
				if (nodesPassed.get(stringCurr) == null) {
					nodesPassed.put(stringCurr, stringCurr);
					expandedNodes++;
					if (curr != null) {
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
							myWriter.write(s + '\n');
						} catch (IOException e) {
							e.printStackTrace();
						}
						Node up = stateSpace(curr, Operator.UP);
						Node down = stateSpace(curr, Operator.DOWN);
						Node left = stateSpace(curr, Operator.LEFT);
						Node right = stateSpace(curr, Operator.RIGHT);
						Node carry = stateSpace(curr, Operator.CARRY);
						Node drop = stateSpace(curr, Operator.DROP);
						if (drop != null)
							queue.addFirst(drop);
						if (carry != null)
							queue.addFirst(carry);
						if (right != null)
							queue.addFirst(right);
						if (left != null)
							queue.addFirst(left);
						if (down != null)
							queue.addFirst(down);
						if (up != null)
							queue.addFirst(up);
					}
				}
			}
		}
		return search(depth + 1);
	}

	public String printArray(short[] arr) {
		String s = "[";
		for (int i = 0; i < arr.length; i++)
			s += arr[i] + ", ";
		s += "]";
		return s;
	}

	public short[] copyArray(short[] arr) {
		short[] res = new short[arr.length];
		for (int i = 0; i < arr.length; i++)
			res[i] = arr[i];
		return res;
	}

	public String returnPath(Node n) {
		if (n.getParent() == null || n.getParent().getOperator() == null)
			return n.getOperator() + "";
		else
			return returnPath(n.getParent()) + "->" + n.getOperator().toString();
	}

	public String formulateNodeToString(Node state) {
		return state.getX() + "," + state.getY() + ";" + Arrays.toString(state.getIMFstates()) + ";" + state.getC();
	}

	public Grid getGrid() {
		return grid;
	}

	public Node getInitialState() {
		return initialState;
	}

	public int getExpandedNodes() {
		return expandedNodes;
	}

	public static int generateNumber(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	//    public static void print(String [][] arr) {
//        for (int i =0; i < arr.length; i++) {
//            for (int j = 0; j < arr[i].length; j++) {
//                System.out.print(" | " + arr[i][j]);
//            }
//            System.out.println(" ");
//        }
//    }
	public Stack<Node> getPath(Node node) {
		Stack<Node> stack = new Stack<>();
		stack.push(node);
		Node currNode = node.getParent();
		while (currNode != null) {
			stack.push(currNode);
			currNode = currNode.getParent();
		}
		return stack;
	}

	//    public void printNode(Node node){
//        String res = "";
//        boolean space=false;
//        for (int i =0; i < grid.getRows(); i++) {
//            res+="  |  ";
//            for (int j = 0; j < grid.getColumns(); j++) {
//                if (i == node.getX() && j == node.getY()) {
//                    res += "  E  ";
//                    res+="  |  ";
//                    space = true;
//                }
//                if (i == grid.getxSub() && j == grid.getySub()) {
//                    res += "  S  ";
//                    res+="  |  ";
//                    space = true;
//                }
//                for (int k = 0; k < node.xPoss.length; k++) {
//                    if (i == node.xPoss[k] && j == node.yPoss[k]) {
//                        res += "F(" + node.damages[k] + ")";
//                        res+="|";
//                        space= true;
//                    }
//                }
//
//              if(!space) res+="   |   ";
//              space = false;
//
//            }
//            res += " \n";
//
//        }
//        res += "Current carry: " +node.c + " ; Current deaths: " + node.deaths + " ; Expanded nodes: " + getExpandedNodes();
//        res += " \n ";
//        System.out.println(res);
//    }
	public void visualize(Node goalState) {
		Stack<Node> stack = getPath(goalState);
		Scanner sc = new Scanner(System.in);
		while (!stack.isEmpty()) {
			Node currNode = stack.pop();
			//printNode(currNode);
			System.out.println("Press ENTER");
			sc.nextLine();
		}
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

	public static void main(String[] args) {
		//String grid = genGrid();
		//System.out.println(grid);

		Grid grid1 = new Grid("3,3;0,0;1,0;1,1,2,2;85,22;1");
		//Grid grid1 = new Grid(grid);
		System.out.println(grid1.toString());

		System.out.println(solve("3,3;0,0;1,0;1,1,2,2;85,22;1", "DF", false));
	}
}
