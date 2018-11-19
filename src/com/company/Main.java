package com.company;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("\nFile location:");
        Scanner scanner = new Scanner(System.in);
        String file = scanner.nextLine();
        int lastIndexOf = file.lastIndexOf(".");

        if(lastIndexOf == -1) {
            System.out.println("No extension");
        }

        String ext = file.substring(lastIndexOf);

        if(ext.equals(".hf")){
            System.out.println("Enter dictionary location:");
            String dictLocation = scanner.nextLine();
            byte[] bytes = readFile(file);
            Dictionary dictionary = readDictionary(dictLocation);
            int[] bytesInt = new int[bytes.length];
            for(int i = 0; i < bytes.length; i++) bytesInt[i] = bytes[i] & 0xFF;
            Decompressor decompressor = new Decompressor(bytesInt, dictionary);
            System.out.println("Enter decompressed.txt file location:");
            String decFileLocation = scanner.nextLine();
            writeDecompressedFile(decompressor.decompress(), decFileLocation);
        } else {
            Compressor compressor = new Compressor(readFile(file));
            CompressionResult compressionResult = compressor.compress();
            System.out.println("\nEnter compressed file path");
            String filePath = scanner.nextLine();
            System.out.println("\nEnter dictionary file path");
            String dictPath = scanner.nextLine();
            writeCompressedFile(compressionResult.getLength(), compressionResult.getBytes(compressionResult.getBytesInt()), compressor.getDictionary(), filePath, dictPath);
        }
    }

    private static void writeDecompressedFile(byte[] content, String filePath) {
        try (FileOutputStream decompressedFos = new FileOutputStream(filePath)) {
            decompressedFos.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void writeCompressedFile(int size, byte[] content, Dictionary dictionary, String filePath, String dictPath) {
        try (FileOutputStream compressedFos = new FileOutputStream(filePath);
             FileOutputStream dict = new FileOutputStream(dictPath)) {

            ObjectOutputStream oos = new ObjectOutputStream(dict);
            oos.writeObject(dictionary);
            oos.close();

            compressedFos.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] content = fis.readAllBytes();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dictionary readDictionary(String dictPath) {
        try (FileInputStream fis = new FileInputStream(dictPath)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (Dictionary) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cne) {
            cne.printStackTrace();
            return null;
        }
    }
}
