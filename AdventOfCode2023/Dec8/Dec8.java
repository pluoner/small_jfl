package AdventOfCode2023.Dec8;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.html.HTMLDocument.Iterator;

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
        ArrayList<String> input2 = Helpers.imp("Dec8/res/inptest3.txt");
        Network net2 = new Network(input2.get(0));
        input2.remove(0);
        input2.remove(0);
        net2.parseNodes(input2);
        GhostNetwork ghostNet = new GhostNetwork(net2);
        ghostNet.moveUntilzNodeAtPopulated();
        ghostNet.compareZPaths();
        // ghostNet.findBestZPath();
        for (Network n : ghostNet.networks) {
            System.out.println("Node " + n.startNode.id() + " reaches Z-node at: " + n.zNodesAt);
        }
    }
}

class GhostNetwork {
    ArrayList<Network> networks;
    Long bestZPath;
    GhostNetwork(Network net) {
        networks = new ArrayList<>();
        bestZPath = 0L;
        for(Node n : net.net.values()) {
            if (n.aNode()) {
                networks.add(new Network(net, n));
            }
        }
    }
    public void compareZPaths() {
        for (Network n : networks) {
            ArrayList<Integer> slimmedZNodeAt = new ArrayList<>();
            ArrayList<Node> slimmedZNodeAtNode = new ArrayList<>();
            for (Integer i : n.zNodesAt) {
                for (Integer j = 0; j < slimmedZNodeAt.size(); j++) {
                    if (j % i == 0 && n.zNodesAtNode.get(i) == slimmedZNodeAtNode.get(j)) {
                        continue;
                    } else {
                        slimmedZNodeAt.add(i);
                        slimmedZNodeAtNode.add(n.zNodesAtNode.get(i));
                    }
                }
            }
            n.zNodesAt = slimmedZNodeAt;
            n.zNodesAtNode = slimmedZNodeAtNode;
        }
    }
    void moveUntilzNodeAtPopulated() {
        while (!allZNodeAtPopulated()) {
            move();
        }
    }
    private boolean allZNodeAtPopulated() {
        for (Network n : networks) {
            if (n.zNodesAt.size() < 9) {
                return false;
            }
        }
        return true;
    }
    void move() {
        for (Network n : networks) {
            n.move();
        }
    }
}

class Network {
    HashMap<String, Node> net;
    Node startNode;
    Node currentNode;
    Instructions instructions;
    Integer stepCount;
    ArrayList<Integer> zNodesAt;
    ArrayList<Node> zNodesAtNode;
    Integer getStepCount() {
        return stepCount;
    }
    void moveToNode(Node n) {
        while (currentNode != n) {
            move();
        }
    }

    void move() {
        currentNode = net.get(currentNode.get(instructions.getNextDir()));
        stepCount++;
        if (zNodesAt.size() < 10 && currentNode.zNode()) {
            zNodesAt.add(stepCount);
            zNodesAtNode.add(currentNode);
        }
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
        zNodesAt = new ArrayList<>();
        zNodesAtNode = new ArrayList<>();
    }
    Network(Network n, Node startNode) {
        net = n.net;
        instructions = new Instructions(n.instructions);
        currentNode = startNode;
        this.startNode = startNode;
        stepCount = 0;
        zNodesAt = new ArrayList<>();
        zNodesAtNode = new ArrayList<>();
    }
}

class Instructions {
    final String inst;
    private Integer instIx;

    Instructions(String s) {
        this.inst = s;
        instIx = 0;
    }
    Instructions(Instructions i) {
        this.inst = i.inst;
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
    boolean aNode() {
        return id.substring(2, 3).equals("A");
    }
    boolean zNode() {
        return id.substring(2, 3).equals("Z");
    }
};

enum Direction {
    LEFT,
    RIGHT
}