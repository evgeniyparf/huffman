package com.company;

public class Node {

    private int value;
    private int weight;

    private Node left;
    private Node right;

    public Node(int value) {
        this.value = value;
        this.weight = 1;
    }

    public Node(int value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    public Node(int value, int weight, Node left, Node right) {
        this.value = value;
        this.weight = weight;
        this.left = left;
        this.right = right;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    public Node getLeft() {
        return left;
    }


    public Node getRight() {
        return right;
    }

    public boolean isLeaf() {
        if (left == null && right == null) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", weight=" + weight +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    public static class Builder {
        private int value;
        private int weight;

        private Node left;
        private Node right;

        public Builder setWeight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder setValue(int value) {
            this.value = value;
            return this;
        }

        public Builder setLeftNode(Node left) {
            this.left = left;
            return this;
        }

        public Builder setRightNode(Node right) {
            this.right = right;
            return this;
        }

        public Node build() {
            return new Node(value, weight, left, right);
        }
    }
}
