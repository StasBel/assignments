package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

class Vertex {
    boolean isTerminal;
    static int charNum = 128;
    int[] go = new int[charNum];
    int terminalSonsNumber;
    Vertex() {
        isTerminal = false;
        for(int i = 0; i < charNum; i++) {
            go[i] = -1;
        }
        terminalSonsNumber = 0;
    }
}

public class StringSetImpl implements StringSet, StreamSerializable {
    private Vector<Vertex> Tree = new Vector<Vertex>();
    private int Size;

    StringSetImpl() {
        Tree.add(new Vertex());
        Size = 0;
    }

    public boolean add(String element) {
        Vertex currentVertex = Tree.get(0);
        boolean isCont = contains(element);
        if (!isCont) {
            for(int i = 0; i < element.length(); i++) {
                int charNum = (int)element.charAt(i);
                if (currentVertex.go[charNum] == -1) {
                    Tree.add(new Vertex());
                    currentVertex.go[charNum] = Tree.size() - 1;
                }
                currentVertex.terminalSonsNumber++;
                currentVertex = Tree.get(currentVertex.go[charNum]);
            }
            Size++;
            currentVertex.isTerminal = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean contains(String element) {
        Vertex currentVertex = Tree.get(0);
        for(int i = 0; i < element.length(); i++) {
            int charNum = (int)element.charAt(i);
            if (currentVertex.go[charNum] == -1) {
                return false;
            }
            currentVertex = Tree.get(currentVertex.go[charNum]);
        }
        return currentVertex.isTerminal;
    }

    public boolean remove(String element) {
        Vertex currentVertex = Tree.get(0);
        boolean isCont = contains(element);
        if (isCont) {
            for(int i = 0; i < element.length(); i++) {
                int charNum = (int)element.charAt(i);
                if (currentVertex.go[charNum] == -1) {
                    return false;
                }
                currentVertex.terminalSonsNumber--;
                currentVertex = Tree.get(currentVertex.go[charNum]);
            }
            Size--;
            currentVertex.isTerminal = false;
            return true;
        } else {
            return false;
        }
    }

    public int size(){
        return Size;
    }

    public int howManyStartsWithPrefix(String prefix){
        Vertex currentVertex = Tree.get(0);
        for(int i = 0; i < prefix.length(); i++) {
            int charNum = (int)prefix.charAt(i);
            if (currentVertex.go[charNum] == -1) {
                return 0;
            }
            currentVertex = Tree.get(currentVertex.go[charNum]);
        }
        return currentVertex.terminalSonsNumber + (currentVertex.isTerminal ? 1 : 0);
    }

    private static int byteArrayToInt(byte[] b, int start, int length) {
        int dt = 0;
        if ((b[start] & 0x80) != 0)
            dt = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++)
            dt = (dt << 8) + (b[start++] & 255);
        return dt;
    }

    private static byte[] intToByteArray(int n, int byteCount) {
        byte[] res = new byte[byteCount];
        for (int i = 0; i < byteCount; i++)
            res[byteCount - i - 1] = (byte) ((n >> i * 8) & 255);
        return res;
    }

    public void serialize(OutputStream out) throws SerializationException {
        try {
            out.write(intToByteArray(Size, 4));
            for(int i = 0; i < Tree.size(); i++) {
                Vertex v = Tree.get(i);
                out.write(v.isTerminal ? 1 : 0);
                for(int j = 0; j < Vertex.charNum; j++) {
                    out.write(intToByteArray(v.go[j], 4));
                }
                out.write(intToByteArray(v.terminalSonsNumber, 4));
            }
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    public void deserialize(InputStream in) {
        try {
            Tree.clear();
            Size = 0;
            byte[] SizeIn = new byte[4];
            int res = in.read(SizeIn);
            if (res < 4) {
                return;
            }
            Size = byteArrayToInt(SizeIn, 0, 4);
            byte[] VertexIn = new byte[1 + 4 * Vertex.charNum + 4];
            while (in.read(VertexIn) > 0) {
                Vertex vertex = new Vertex();
                vertex.isTerminal = byteArrayToInt(VertexIn, 0, 1) == 1;
                for(int j = 0; j < Vertex.charNum; j++) {
                    vertex.go[j] = byteArrayToInt(VertexIn, 1 + j * 4, 4);
                }
                vertex.terminalSonsNumber = byteArrayToInt(VertexIn, 1 + 4 * Vertex.charNum, 4);
                Tree.add(vertex);
            }
        } catch (IOException e) {
            throw new SerializationException();
        }
    }
}
