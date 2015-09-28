package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class StringSetImpl implements StringSet, StreamSerializable {

    private static class Vertex {
        private static int CHAR_NUM = 128;
        private static int NO_GO = -1;
        private static int BYTE_NUM = 1 + 4 * CHAR_NUM + 4;
        private static Vertex NULL_VERTEX = null;
        private boolean isTerminal;
        private int[] go = new int[CHAR_NUM];
        private int terminalSonsNumber;
        public Vertex() {
            isTerminal = false;
            for (int i = 0; i < CHAR_NUM; i++) {
                go[i] = NO_GO;
            }
            terminalSonsNumber = 0;
        }
    }

    private List<Vertex> tree;
    private int size;

    public StringSetImpl() {
        tree = new ArrayList<>();
        tree.add(new Vertex());
        size = 0;
    }

    public boolean add(String element) {
        Vertex currentVertex = tree.get(0);
        if (!contains(element)) {
            for (char nextChar : element.toCharArray()) {
                int nextCharNum = (int)nextChar;
                if (currentVertex.go[nextCharNum] == Vertex.NO_GO) {
                    tree.add(new Vertex());
                    currentVertex.go[nextCharNum] = tree.size() - 1;
                }
                currentVertex.terminalSonsNumber++;
                currentVertex = tree.get(currentVertex.go[nextCharNum]);
            }
            size++;
            currentVertex.isTerminal = true;
            return true;
        } else {
            return false;
        }
    }

    private Vertex findVertex(String element) {
        Vertex currentVertex = tree.get(0);
        for (char nextChar : element.toCharArray()) {
            int nextCharNum = (int)nextChar;
            if (currentVertex.go[nextCharNum] == Vertex.NO_GO) {
                return Vertex.NULL_VERTEX;
            }
            currentVertex = tree.get(currentVertex.go[nextCharNum]);
        }
        return currentVertex;
    }

    public boolean contains(String element) {
        Vertex vertex = findVertex(element);
        return (vertex != Vertex.NULL_VERTEX) && (vertex.isTerminal);
    }

    public boolean remove(String element) {
        Vertex currentVertex = tree.get(0);
        if (contains(element)) {
            for (char nextChar : element.toCharArray()) {
                int nextCharNum = (int)nextChar;
                currentVertex.terminalSonsNumber--;
                currentVertex = tree.get(currentVertex.go[nextCharNum]);
            }
            size--;
            currentVertex.isTerminal = false;
            return true;
        } else {
            return false;
        }
    }

    public int size(){
        return size;
    }

    public int howManyStartsWithPrefix(String prefix){
        Vertex vertex = findVertex(prefix);
        return vertex == Vertex.NULL_VERTEX ? 0 : vertex.terminalSonsNumber + (vertex.isTerminal ? 1 : 0);
    }

    private static int byteArrayToInt(byte[] b, int start, int length) {
        int dt = 0;
        if ((b[start] & 0x80) != 0)
            dt = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++)
            dt = (dt << 8) + (b[start++] & 255);
        return dt;
    }

    private static byte[] intToByteArray(int n) {
        byte[] res = new byte[4];
        for (int i = 0; i < 4; i++)
            res[3 - i] = (byte)((n >> i * 8) & 255);
        return res;
    }

    public void serialize(OutputStream out) throws SerializationException {
        try {
            for (Vertex nextVertex : tree) {
                out.write(nextVertex.isTerminal ? 1 : 0);
                for (int nextMove : nextVertex.go) {
                    out.write(intToByteArray(nextMove));
                }
                out.write(intToByteArray(nextVertex.terminalSonsNumber));
            }
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    public void deserialize(InputStream in) throws SerializationException {
        try {
            tree.clear();
            size = 0;
            byte[] vertexIn = new byte[Vertex.BYTE_NUM];
            while (in.read(vertexIn) > 0) {
                Vertex nextVertex = new Vertex();
                nextVertex.isTerminal = byteArrayToInt(vertexIn, 0, 1) == 1;
                size += nextVertex.isTerminal ? 1 : 0;
                for (int j = 0; j < Vertex.CHAR_NUM; j++) {
                    nextVertex.go[j] = byteArrayToInt(vertexIn, 1 + j * 4, 4);
                }
                nextVertex.terminalSonsNumber = byteArrayToInt(vertexIn, Vertex.BYTE_NUM - 4, 4);
                tree.add(nextVertex);
            }
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

}
