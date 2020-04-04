package com.sparrowwallet.drongo.protocol;

import com.sparrowwallet.drongo.Utils;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransactionWitness {
    public static final TransactionWitness EMPTY = new TransactionWitness(0);

    private final List<byte[]> pushes;

    public TransactionWitness(int pushCount) {
        pushes = new ArrayList<>(Math.min(pushCount, Utils.MAX_INITIAL_ARRAY_LENGTH));
    }

    public List<byte[]> getPushes() {
        return Collections.unmodifiableList(pushes);
    }

    public void setPush(int i, byte[] value) {
        while (i >= pushes.size()) {
            pushes.add(new byte[]{});
        }
        pushes.set(i, value);
    }

    public int getPushCount() {
        return pushes.size();
    }

    public int getLength() {
        int length = new VarInt(pushes.size()).getSizeInBytes();
        for (int i = 0; i < pushes.size(); i++) {
            byte[] push = pushes.get(i);
            length += new VarInt(push.length).getSizeInBytes();
            length += push.length;
        }

        return length;
    }

    protected void bitcoinSerializeToStream(OutputStream stream) throws IOException {
        stream.write(new VarInt(pushes.size()).encode());
        for (int i = 0; i < pushes.size(); i++) {
            byte[] push = pushes.get(i);
            stream.write(new VarInt(push.length).encode());
            stream.write(push);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (byte[] push : pushes) {
            if (push == null) {
                builder.append("NULL");
            } else if (push.length == 0) {
                builder.append("EMPTY");
            } else {
                builder.append(Hex.toHexString(push));
            }
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    public List<ScriptChunk> asScriptChunks() {
        List<ScriptChunk> scriptChunks = new ArrayList<>(pushes.size());
        for(byte[] push : pushes) {
           scriptChunks.add(new ScriptChunk(ScriptChunk.getOpcodeForLength(push.length), push));
        }

        return scriptChunks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionWitness other = (TransactionWitness) o;
        if (pushes.size() != other.pushes.size()) return false;
        for (int i = 0; i < pushes.size(); i++) {
            if (!Arrays.equals(pushes.get(i), other.pushes.get(i))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (byte[] push : pushes) {
            hashCode = 31 * hashCode + (push == null ? 0 : Arrays.hashCode(push));
        }
        return hashCode;
    }
}