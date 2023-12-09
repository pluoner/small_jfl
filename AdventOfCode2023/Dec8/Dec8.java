package AdventOfCode2023.Dec8;

import java.util.ArrayList;
import java.util.HashMap;

import AdventOfCode2023.Common.Helpers;

public class Dec8 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec8/res/input.txt");
        //pt 1
        Network net = new Network(input.get(0));
        input.remove(0);
        input.remove(0);
        net.parseNodes(input);
        net.currentNode = net.net.get("AAA");
        net.moveToNode(net.net.get("ZZZ"));
        System.out.println("Steps to ZZZ: " + net.getStepCount());
        //pt 2

    }
}

class GhostNetwork extends Network {
    ArrayList<Node> currentNodes;
    GhostNetwork(String inst) {
        super(inst);
        
    }
    void initStartNodes() {

    }
}

class Network {
    HashMap<String, Node> net;
    Node currentNode;
    Instructions instructions;
    Integer stepCount;

    Integer getStepCount() {
        return stepCount;
    }
    void moveToNode(Node n) {
        while (currentNode != n) {
            move();
            stepCount++;
        }
    }

    void move() {
        currentNode = net.get(currentNode.get(instructions.getNextDir()));
    }

    void parseNodes(ArrayList<String> nodes) {
        for (String n : nodes) {
            addNode(n.substring(0,3), n.substring(7,10), n.substring(12,15));
        }
    }

    void addNode(String id, String left, String right) {
        net.put(id, new Node(id, left, right));
    }

    Network(String inst) {
        stepCount = 0;
        net = new HashMap<>();
        instructions = new Instructions(inst);
    }
}

class Instructions {
    final String inst;
    private Integer instIx;

    Instructions(String s) {
        this.inst = s;
        instIx = 0;
    }
    Direction getNextDir() {
        if (instIx == inst.length()) {
            instIx = 0;
        }
        if (inst.charAt(instIx) == 'L') {
            instIx++;
            return Direction.LEFT;
        }
        instIx++;
        return Direction.RIGHT;
    }
}

record Node(String id, String left, String right) {
    String get(Direction d) {
        if (d == Direction.LEFT) {
            return left;
        }
        return right;
    }
};

enum Direction {
    LEFT,
    RIGHT
}