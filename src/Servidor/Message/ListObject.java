package Servidor.Message;

import Entidades.Localizacao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListObject {
    private int size;
    private List<Localizacao> objects;

    public ListObject(int size, List<Localizacao> objects) throws IOException {
        this.size = size;
        this.objects = objects;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(size);
        for (Localizacao loc : objects) {
            out.writeFloat(loc.getX());
            out.writeFloat(loc.getY());
        }
        out.flush();
    }

    public static List<Localizacao> deserialize(DataInputStream in) throws IOException {
        int size = in.readInt();
        List<Localizacao> objects = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            Localizacao c = Localizacao.deserialize(in);
            objects.add(c);
        }
        return objects;

    }
}
