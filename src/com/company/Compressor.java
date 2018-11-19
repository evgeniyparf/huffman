package com.company;

import java.util.*;

public class Compressor {

    private int[] fileBytes;
    private Dictionary dictionary;

    public Compressor(byte[] fileBytes) {
        this.fileBytes = byteArrayToIntArray(fileBytes);
    }

    public Compressor(int[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public CompressionResult compress() {
        CompressionResult.Builder compressionResultBuilder = CompressionResult.getBuilder();
        dictionary = new Dictionary();
        dictionary.setDictionary(buildDictionaryTable());
        for(int i : fileBytes) {
            for(Bit bit : dictionary.getDictionary().get(i)) {
                compressionResultBuilder.addBit(bit);
            }
        }
        CompressionResult compressionResult = compressionResultBuilder.build();
        dictionary.setLength(compressionResult.getLength());
        return compressionResult;
    }

    private int[] getRepeats() {
        int[] repeats = new int[256];
        for(int i : fileBytes) {
            repeats[i]++;
        }
        return repeats;
    }

    private PriorityQueue<Node> getRepeatsSorted() {
        int[] repeats = getRepeats();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getWeight));
        for(int i = 0; i < repeats.length; i++) {
            if(repeats[i] != 0) pq.offer(new Node(i, repeats[i]));
        }
        return pq;
    }

    private PriorityQueue<Node> buildHuffmanTree() {
        PriorityQueue<Node> tree = getRepeatsSorted();
        while(tree.size() > 1) {
            Node node1 = tree.poll();
            Node node2 = tree.poll();
            tree.offer(Node.builder()
                            .setValue(node1.getValue() + node2.getValue())
                            .setWeight(node1.getWeight() + node2.getWeight())
                            .setLeftNode(node1)
                            .setRightNode(node2)
                            .build());
        }
        return tree;
    }

    private HashMap<Integer, Bit[]> buildDictionaryTable() {
        HashMap<Integer, Bit[]> dict = new HashMap<>();
        PriorityQueue<Node> tree = buildHuffmanTree();
        getBits(dict, tree.peek(), "");
        return dict;
    }

    public void getBits(Map<Integer, Bit[]> map, Node root, String path) {
        if(root.isLeaf()) map.put(root.getValue(), stringToBitsArray(path));
        else {
            getBits(map, root.getLeft(), path + "0");
            getBits(map, root.getRight(), path + "1");
        }
    }

    private static int[] byteArrayToIntArray(byte[] array) {
        int[] intArray = new int[array.length];
        for(int i = 0; i < array.length; i++) {
            intArray[i] = array[i] & 0xFF;
        }
        return intArray;
    }

    private static String bitsArrayListToString(ArrayList<Bit> bits) {
        String res = "";
        for(Bit bit : bits) {
            if(bit == Bit.ZERO) res += "0";
            if(bit == Bit.ONE) res += "1";
        }
        return res;
    }

    private static Bit[] stringToBitsArray(String path) {
        Bit[] bits = new Bit[path.length()];
        int counter = 0;
        for(char c : path.toCharArray()) {
            if(c == '0') bits[counter] = Bit.ZERO;
            if(c == '1') bits[counter] = Bit.ONE;
            counter++;
        }
        return bits;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }
}
