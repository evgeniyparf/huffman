package com.company;

import java.util.ArrayList;
import java.util.HashMap;

public class Decompressor {

    private int[] fileBytes;
    private Dictionary dictionary;

    public Decompressor(int[] fileBytes, Dictionary dictionary) {
        this.fileBytes = fileBytes;
        this.dictionary = dictionary;
    }

    public byte[] decompress() {
        HashMap<Integer, Bit[]> dict = dictionary.getDictionary();
        ArrayList<Integer> bytesInt = new ArrayList<>();
        String bytes = "";
        for(String s : bytesToString(fileBytes)) {
            bytes += s;
        }

        int size = bytes.length() - dictionary.getLenght();

        top:
        while(bytes.length() > size) {
            for (int key : dict.keySet()) {
                if (bytes.startsWith(Dictionary.bitArrayToString(dict.get(key)))) {
                    bytesInt.add(key);
                    bytes = bytes.substring(dict.get(key).length);
                    continue top;
                }
            }
        }
        return intArrayToByte(bytesInt.toArray(new Integer[0]));
    }

    public static byte[] intArrayToByte(Integer[] arr) {
        byte[] bytes = new byte[arr.length];
        for(Integer i = 0; i < arr.length; i++) {
            bytes[i] = arr[i].byteValue();
        }
        return bytes;
    }

    public static String[] bytesToString(int[] bytes) {
        String[] bytesString = new String[bytes.length];
        for(int i = 0; i < bytesString.length; i++) {
            bytesString[i] = Integer.toString(bytes[i], 2);
            if(bytesString[i].length() < 8) {
                int size = 8 - bytesString[i].length();
                for(int y = 0; y < size; y++) {
                    bytesString[i] = "0" + bytesString[i];
                }
            }
        }
        return bytesString;
    }
}
