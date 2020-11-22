package mission;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import generic.QueuingFunction;
import generic.SearchProblem;

public class MissionImpossible extends SearchProblem {

	private Grid grid;
	private Node initialState;
	private static String[] operators = { "UP", "DOWN", "RIGHT", "LEFT", "CARRY", "DROP" };
	private static boolean vis = false;

	public MissionImpossible(Grid grid, Node initialState) {
		this.grid = grid;
		this.initialState = initialState;
	}

	@Override
	public Node getInitialState() {
		return this.initialState;
	}

	public static int generateNumber(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	public static String solve(String grid, String strategy, boolean visualize) {
		Grid g = new Grid(grid);
		short[] IMFstates = new short[g.getDamages().length];
		Node initialState = new Node(g.getxEthan(), g.getyEthan(), g.getC(), (short) 0, g.getxPoss(), g.getyPoss(),
				g.getDamages(), IMFstates, null, null, 0, 0);
		initialState.setPriority(0);

		vis = visualize;
		SearchProblem MI = new MissionImpossible(g, initialState);
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
			if (xy.contains(xPos + "," + yPos) || xPos == xEthan && yPos == yEthan || xPos == xSub && yPos == ySub) {
				i--;
			} else {
				xy.add(xPos + "," + yPos);
				res += xPos + "," + yPos;
				xPoss[i - 1] = xPos;
				yPoss[i - 1] = yPos;
				if (i == agents) {
					res += ";";
				} else {
					res += ",";
				}
			}
		}

		int[] damages = new int[agents];
		for (int i = 1; i <= agents; i++) {
			int damage = generateNumber(1, 99);
			res += damage;
			damages[i - 1] = damage;
			if (i == agents) {
				res += ";";
			} else {
				res += ",";
			}
		}

		int c = generateNumber(1, agents);
		res += c;
		// print(visualise(height, width, xEthan, yEthan, xSub, ySub, imfs));
		return res;
	}

	@Override
	public String[] getOperators() {
		return operators;
	}

	@Override
	public generic.Node stateSpace(generic.Node genericNode, String operator) {
		Node node = (mission.Node) genericNode;
		String state = node.getState();
		String[] currentStates = state.split(";");
		String[] ethanPosCarryDeaths = currentStates[0].split(",");
		short x = Short.valueOf(ethanPosCarryDeaths[0]);
		short y = Short.valueOf(ethanPosCarryDeaths[1]);

		switch (operator) {
		case "UP": {
			if (x == 0 || node.getOperator() != null && node.getOperator().equals("DOWN"))
				return null;
			short c = Short.valueOf(ethanPosCarryDeaths[2]);
			short deaths = Short.valueOf(ethanPosCarryDeaths[3]);

			short[] xPoss = copyArray(currentStates[1].split(","));
			short[] yPoss = copyArray(currentStates[2].split(","));
			short[] damages = copyArray(currentStates[3].split(","));
			short[] IMFstates = copyArray(currentStates[4].split(","));
			int pathCost = node.getPathCost();
			for (int i = 0; i < damages.length; i++) {
				if (IMFstates[i] == 0) { // an IMF i am not carrying
					if (damages[i] == 99 || damages[i] == 98) {
						damages[i] = 100;
						deaths += 1;
						pathCost += IMFstates.length * 100;
					} else if (damages[i] != 100) {
						damages[i] += 2;
						pathCost += 2;
					}
				} else if (IMFstates[i] == 1) {
					xPoss[i] = (short) (x - 1);
				}
			}
			Node result = new Node((short) (x - 1), y, c, deaths, xPoss, yPoss, damages, IMFstates, node, "UP",
					node.getDepth() + 1, pathCost);
			return result;
		}
		case "DOWN": {
			if (x == this.grid.getRows() - 1 || node.getOperator() != null && node.getOperator().equals("UP"))
				return null;
			short c = Short.valueOf(ethanPosCarryDeaths[2]);
			short deaths = Short.valueOf(ethanPosCarryDeaths[3]);

			short[] xPoss = copyArray(currentStates[1].split(","));
			short[] yPoss = copyArray(currentStates[2].split(","));
			short[] damages = copyArray(currentStates[3].split(","));
			short[] IMFstates = copyArray(currentStates[4].split(","));
			int pathCost = node.getPathCost();
			for (int i = 0; i < damages.length; i++) {
				if (IMFstates[i] == 0) { // an IMF i am not carrying
					if (damages[i] == 99 || damages[i] == 98) {
						damages[i] = 100;
						deaths += 1;
						pathCost += IMFstates.length * 100;
					} else if (damages[i] != 100) {
						damages[i] += 2;
						pathCost += 2;
					}
				} else if (IMFstates[i] == 1) {
					xPoss[i] = (short) (x + 1);
				}
			}
			Node result = new Node((short) (x + 1), y, c, deaths, xPoss, yPoss, damages, IMFstates, node, "DOWN",
					node.getDepth() + 1, pathCost);
			return result;
		}
		case "RIGHT": {
			if (y == this.grid.getColumns() - 1 || node.getOperator() != null && node.getOperator().equals("LEFT"))
				return null;
			else {
				short c = Short.valueOf(ethanPosCarryDeaths[2]);
				short deaths = Short.valueOf(ethanPosCarryDeaths[3]);

				short[] xPoss = copyArray(currentStates[1].split(","));
				short[] yPoss = copyArray(currentStates[2].split(","));
				short[] damages = copyArray(currentStates[3].split(","));
				short[] IMFstates = copyArray(currentStates[4].split(","));
				int pathCost = node.getPathCost();
				for (int i = 0; i < damages.length; i++) {
					if (IMFstates[i] == 0) { // an IMF i am not carrying
						if (damages[i] == 99 || damages[i] == 98) {
							damages[i] = 100;
							deaths += 1;
							pathCost += IMFstates.length * 100;
						} else if (damages[i] != 100) {
							damages[i] += 2;
							pathCost += 2;
						}
					} else if (IMFstates[i] == 1) {
						yPoss[i] = (short) (y + 1);
					}
				}
				Node result = new Node(x, (short) (y + 1), c, deaths, xPoss, yPoss, damages, IMFstates, node, "RIGHT",
						node.getDepth() + 1, pathCost);
				return result;
			}
		}
		case "LEFT": {
			if (y == 0 || node.getOperator() != null && node.getOperator().equals("RIGHT"))
				return null;
			else {
				short c = Short.valueOf(ethanPosCarryDeaths[2]);
				short deaths = Short.valueOf(ethanPosCarryDeaths[3]);

				short[] xPoss = copyArray(currentStates[1].split(","));
				short[] yPoss = copyArray(currentStates[2].split(","));
				short[] damages = copyArray(currentStates[3].split(","));
				short[] IMFstates = copyArray(currentStates[4].split(","));
				int pathCost = node.getPathCost();
				for (int i = 0; i < damages.length; i++) {
					if (IMFstates[i] == 0) { // an IMF i am not carrying
						if (damages[i] == 99 || damages[i] == 98) {
							damages[i] = 100;
							deaths += 1;
							pathCost += IMFstates.length * 100;
						} else if (damages[i] != 100) {
							damages[i] += 2;
							pathCost += 2;
						}
					} else if (IMFstates[i] == 1) {
						yPoss[i] = (short) (y - 1);
					}
				}
				Node result = new Node(x, (short) (y - 1), c, deaths, xPoss, yPoss, damages, IMFstates, node, "LEFT",
						node.getDepth() + 1, pathCost);
				return result;
			}
		}

		case "CARRY": {
			if (node.getOperator() != null && node.getOperator().equals("DROP"))
				return null;
			short c = Short.valueOf(ethanPosCarryDeaths[2]);
			short deaths = Short.valueOf(ethanPosCarryDeaths[3]);
			boolean imfCarried = false;

			short[] xPoss = copyArray(currentStates[1].split(","));
			short[] yPoss = copyArray(currentStates[2].split(","));
			short[] damages = copyArray(currentStates[3].split(","));
			short[] IMFstates = copyArray(currentStates[4].split(","));
			for (int i = 0; i < xPoss.length; i++) {
				if (xPoss[i] == x && yPoss[i] == y && IMFstates[i] == 0 && c > 0) {
					c -= 1;
					IMFstates[i] = 1;
					imfCarried = true;
				}
			}
			if (!imfCarried)
				return null;
			int pathCost = node.getPathCost();
			for (int i = 0; i < damages.length; i++)
				if (IMFstates[i] == 0) { // an IMF i am not carrying
					if (damages[i] == 99 || damages[i] == 98) {
						damages[i] = 100;
						deaths += 1;
						pathCost += IMFstates.length * 100;
					} else if (damages[i] != 100) {
						damages[i] += 2;
						pathCost += 2;
					}
				}
			
				return new Node(x, y, c, deaths, xPoss, yPoss, damages, IMFstates, node, "CARRY", node.getDepth() + 1,
						pathCost);
		}
		case "DROP": {
			if (node.getOperator() != null && node.getOperator().equals("CARRY"))
				return null;
			short c = Short.valueOf(ethanPosCarryDeaths[2]);
			short deaths = Short.valueOf(ethanPosCarryDeaths[3]);

			short[] xPoss = copyArray(currentStates[1].split(","));
			short[] yPoss = copyArray(currentStates[2].split(","));
			short[] damages = copyArray(currentStates[3].split(","));
			short[] IMFstates = copyArray(currentStates[4].split(","));
			if (c == this.grid.getC() || !(x == this.grid.getxSub() && y == this.grid.getySub()))
				return null;
			for (int i = 0; i < xPoss.length; i++)
				if (xPoss[i] == this.grid.getxSub() && yPoss[i] == this.grid.getySub() && IMFstates[i] == 1) {
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
					} else if (damages[i] != 100) {
						damages[i] += 2;
						pathCost += 2;
					}
				}
			return new Node(x, y, c, deaths, xPoss, yPoss, damages, IMFstates, node, "DROP", node.getDepth() + 1,
					pathCost);
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
		if (x == this.grid.getxSub() && y == this.grid.getySub() && allAreInSub)
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
		Stack<Node> nodesStack = new Stack<Node>();
		stack.push(";" + deaths + ";" + damages + ";" + SearchProblem.expandedNodes);
		stack.push(curr.getOperator().toLowerCase());
		curr = curr.getParent();
		while (curr.getParent() != null && curr.getParent().getOperator() != null) {
			stack.push(curr.getOperator().toLowerCase() + ",");
			curr = curr.getParent();
		}
		stack.push(curr.getOperator().toLowerCase() /*+ "(" + curr.getPathCost() + ") " + "(" + calculateSecondHeuristic(curr, true) + ")" */ + ",");
		while (!stack.isEmpty())
			res += stack.pop();

		if(vis) {
			Node curr2 = (Node) n;
			while (curr2.getParent() != null && curr2.getParent().getOperator() != null) {
				nodesStack.push(curr2);
				curr2 = curr2.getParent();
			}
			nodesStack.push(curr);
			while (!nodesStack.isEmpty())
				visualize(nodesStack.pop());
		}
		int s = 0;
		for (int i = 0; i < damages.split(",").length; i++)
			s += Integer.parseInt(damages.split(",")[i]);
		System.out.println("TOTAL DAMAGES " + s);
		return res;
	}

	@Override
	public int calculateFirstHeuristicOld(generic.Node n) {
		int cost = 0;
		String[] currentStates = n.getState().split(";");
		String[] ethanPosCarryDamages = currentStates[0].split(",");
		short xEthan = Short.valueOf(ethanPosCarryDamages[0]);
		short yEthan = Short.valueOf(ethanPosCarryDamages[1]);
		String IMFstates = currentStates[4];
		short[] xPoss = this.grid.getxPoss();
		short[] yPoss = this.grid.getyPoss();
		short[] damages = this.grid.getDamages();
		for (int i = 0; i < xPoss.length; i++) {
			int xDiff = Math.abs(xPoss[i] - xEthan);
			int yDiff = Math.abs(yPoss[i] - yEthan);
			int distance = xDiff + yDiff + 1; // manhattan distance + 1 for carry
			if (Integer.parseInt(IMFstates.split(",")[i]) == 0)
				if (damages[i] + distance * 2 >= 100) {
					cost += 100;
				} else {
					cost += damages[i] + distance * 2;
				}
		}
		return cost;
	}

	@Override
	public int calculateFirstHeuristic(generic.Node n) {
		int cost = 0;
		boolean foundSomeone = false;
		String[] currentStates = n.getState().split(";");
		String[] ethanPosCarryDamages = currentStates[0].split(",");
		short xEthan = Short.valueOf(ethanPosCarryDamages[0]);
		short yEthan = Short.valueOf(ethanPosCarryDamages[1]);
		String[] IMFstates = currentStates[4].split(",");
		short[] xPoss = copyArray(currentStates[1].split(","));
		short[] yPoss = copyArray(currentStates[2].split(","));
		String[] damages = currentStates[3].split(",");
		int indexOfOneToSave = 0;
		int bestSoFar = Integer.MAX_VALUE;
		for (int i = 0; i < damages.length; i++)
			if (IMFstates[i].equals("0")) {
				foundSomeone = true;
				int xDiff = Math.abs(xPoss[i] - xEthan);
				int yDiff = Math.abs(yPoss[i] - yEthan);
				int damage = 2 * (xDiff + yDiff);
				int diffInDamage = 100 - (Short.parseShort(damages[i]) + damage);
				if (diffInDamage > 0 && diffInDamage < bestSoFar) {
					bestSoFar = diffInDamage;
					indexOfOneToSave = i;
				}
			}
		if (foundSomeone) {
			cost = 2 * (Math.abs(xPoss[indexOfOneToSave] - xEthan) + Math.abs(yPoss[indexOfOneToSave] - yEthan));
			cost += 2 * (Math.abs(xPoss[indexOfOneToSave] - this.grid.getxSub())
					+ Math.abs(yPoss[indexOfOneToSave] - this.grid.getySub()) + 2);
		}
		return cost;
	}

	@Override
	public int calculateSecondHeuristic(generic.Node n) {
		int cost = 0;
		boolean foundSomeone = false;
		String[] currentStates = n.getState().split(";");
		String[] ethanPosCarryDamages = currentStates[0].split(",");
		short xEthan = Short.valueOf(ethanPosCarryDamages[0]);
		short yEthan = Short.valueOf(ethanPosCarryDamages[1]);
		String[] IMFstates = currentStates[4].split(",");
		short[] xPoss = copyArray(currentStates[1].split(","));
		short[] yPoss = copyArray(currentStates[2].split(","));
		String[] damages = currentStates[3].split(",");
		int closestDistance = Integer.MAX_VALUE;
		int countOfFreeIMFs = 0;
		for (int i = 0; i < damages.length; i++)
			if (IMFstates[i].equals("0")) {
				countOfFreeIMFs += 1;
				foundSomeone = true;
				int xDiff = Math.abs(xPoss[i] - xEthan);
				int yDiff = Math.abs(yPoss[i] - yEthan);
				int distance = xDiff + yDiff;
				if (distance < closestDistance) {
					closestDistance = distance;
				}
			}
		if (foundSomeone) {
			cost += closestDistance * countOfFreeIMFs * 2;
		}
		return cost;
	}

	@Override
	public int calculateSecondHeuristicOld(generic.Node n, boolean print) {
		if (print) {
			int cost = 0;
			String[] currentStates = n.getState().split(";");
			String[] ethanPosCarryDamages = currentStates[0].split(",");
			short xEthan = Short.valueOf(ethanPosCarryDamages[0]);
			short yEthan = Short.valueOf(ethanPosCarryDamages[1]);
			short xSub = this.grid.getxSub();
			short ySub = this.grid.getySub();
			short c = Short.valueOf(ethanPosCarryDamages[2]);
			String IMFstates = currentStates[4];
			short[] xPoss = this.grid.getxPoss();
			short[] yPoss = this.grid.getyPoss();
			System.out.println(Arrays.asList(IMFstates.split(",")));
			int wasaltohom = Collections.frequency(Arrays.asList(IMFstates.split(",")), "1")
					+ Collections.frequency(Arrays.asList(IMFstates.split(",")), "2");
			System.out.println("wasaltohom before loop " + wasaltohom + " and c is " + c);
			for (int i = wasaltohom; i < xPoss.length;) {
				int ontheway = 0;
				int damage = 0;
				while (ontheway <= c && i < xPoss.length) {
					int xDiff = Math.abs(xPoss[i] - xEthan);
					int yDiff = Math.abs(yPoss[i] - yEthan);
					int distance = xDiff + yDiff + 1; // manhattan distance + 1 for carry
					damage = distance * 2;
					System.out.println("at i " + i + " damage is " + damage + " distance is " + distance
							+ " & ontheway is " + ontheway);
					cost += damage;
					ontheway++;
					if (i + 1 == xPoss.length) {
						break;
					}
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
				if (i + 1 == xPoss.length) {
					break;
				}
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
		short xSub = this.grid.getxSub();
		short ySub = this.grid.getySub();
		short c = Short.valueOf(ethanPosCarryDamages[2]);
		String IMFstates = currentStates[4];
		short[] xPoss = this.grid.getxPoss();
		short[] yPoss = this.grid.getyPoss();
		int wasaltohom = Collections.frequency(Arrays.asList(IMFstates.split(",")), "1")
				+ Collections.frequency(Arrays.asList(IMFstates.split(",")), "2");
		for (int i = wasaltohom; i < xPoss.length;) {
			int ontheway = 0;
			int damage = 0;
			while (ontheway <= c && i < xPoss.length) {
				int xDiff = Math.abs(xPoss[i] - xEthan);
				int yDiff = Math.abs(yPoss[i] - yEthan);
				int distance = xDiff + yDiff + 1; // manhattan distance + 1 for carry
				damage = distance * 2;
				cost += damage;
				ontheway++;
				if (i + 1 == xPoss.length) {
					break;
				}
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
			if (i + 1 == xPoss.length) {
				break;
			}
			xEthan = xSub;
			yEthan = ySub;
		}
		return cost;
	}

	public short[] copyArray(String[] arr) {
		short[] res = new short[arr.length];
		for (int i = 0; i < arr.length; i++) {
			res[i] = Short.valueOf(arr[i]);
		}
		return res;
	}

	public void visualize(Node n) {
		File file = new File("visualize.txt");
		int rows = grid.getRows();
		int columns = grid.getColumns();
		short xSub = grid.getxSub();
		short ySub = grid.getySub();
		String[] currentStates = n.getState().split(";");
		String[] ethanPosCarryDamages = currentStates[0].split(",");
		short xEthan = Short.valueOf(ethanPosCarryDamages[0]);
		short yEthan = Short.valueOf(ethanPosCarryDamages[1]);
		short[] xPoss = copyArray(currentStates[1].split(","));
		short[] yPoss = copyArray(currentStates[2].split(","));
		boolean emptyCell;
		String res = "";
		for (int i = 0; i < rows; i++) {
			res+="|";
			for (int j = 0; j < columns; j++) {
				emptyCell = true;
				if (xEthan == i && yEthan == j) {
					res+="E|";
					emptyCell = false;
				} else {
					if (xSub == i && ySub == j) {
						res+="S|";
						emptyCell = false;
					} else {
						for (int k = 0; k < xPoss.length; k++) {
							if (xPoss[k] == i && yPoss[k] == j) {
								res+="I|";
								emptyCell = false;
							}
						}
					}
				}
				if (emptyCell)
					res+="_|";
			}
			res+="\n";
		}
		res+=n.getOperator().toString();
		res+="\n";
		res+="\n";
		res+="\n";
		res+="\n";
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(res);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// String grid = genGrid();
		// System.out.println(grid);
		Grid grid1 = new Grid("14,14;13,9;1,13;5,3,9,7,11,10,8,3,10,7,13,6,11,1,5,2;76,30,2,49,63,43,72,1;6");
		// Grid grid1 = new Grid(grid);
		System.out.println(grid1.toString());
		// "5,5;2,1;1,0;1,3,4,2,4,1,3,1;54,31,39,98;2"
		// "15,15;5,10;14,14;0,0,0,1,0,2,0,3,0,4,0,5,0,6,0,7,0,8;81,13,40,38,52,63,66,36,13;1"
		System.out.println(
				solve("14,14;13,9;1,13;5,3,9,7,11,10,8,3,10,7,13,6,11,1,5,2;76,30,2,49,63,43,72,1;6", "AS2", true));

	}
}
