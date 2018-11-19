package com.company;

import java.util.ArrayList;
import java.util.List;

public class CompressionResult {

    private final List<Integer> bytes;
    private List<String> bytesString;
    private int length;

    private CompressionResult(List<Integer> bytes, List<String> bytesString, int length) {
        this.bytes = bytes;
        this.bytesString = bytesString;
        this.length = length;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public String getBytesStringOne() {
        String res = "";
        for (int i : bytes) {
            res += Integer.toString(i, 2);
        }
        return res;
    }

    public List<String> getBytesString() {
        return bytesString;
    }

    public byte[] getBytes(Integer[] array) {
        byte[] bytes = new byte[array.length];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = this.bytes.get(i).byteValue();
        }
        return bytes;
    }

    public Integer[] getBytesInt() {
        return bytes.toArray(new Integer[0]);
    }

    public int getLength() {
        return length;
    }

    public static class Builder {

        private List<Integer> bytes = new ArrayList<>();
        private List<String> bytesString = new ArrayList<>();
        private String stringByte = "";
        private int count = 0;

        private boolean over = false;
        private int length = 0;

        private Builder() {
        }

        public Builder addBit(Bit bit) {
            if(count < 8) {
                stringByte += bit == Bit.ZERO ? "0" : "1";
                if(!over) length++;
                count++;
            } else {
                bytes.add(Integer.parseInt(stringByte, 2));
                bytesString.add(stringByte);
                stringByte = "";
                count = 0;
                addBit(bit);
            }
            return this;
        }

        public void fillEmpty() {
            over = true;
            int size = 8 - stringByte.length();
            if(stringByte.length() < 8) {
                for(int i = 0; i < size; i++) {
                    addBit(Bit.ZERO);
                }
            }
            bytes.add(Integer.parseInt(stringByte, 2));
            bytesString.add(stringByte);
        }

        public CompressionResult build() {
            fillEmpty();
            return new CompressionResult(bytes, bytesString, length);
        }
    }
}

