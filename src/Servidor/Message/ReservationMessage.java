package Servidor.Message;

import Entidades.Localizacao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReservationMessage {
    private String reservationCode;
    private Localizacao location;

    public ReservationMessage(String reservationCode, Localizacao location) throws IOException {
        this.reservationCode = reservationCode;
        this.location = location;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(reservationCode);
        out.writeFloat(location.getX());
        out.writeFloat(location.getY());
        out.flush();
    }

    public static ReservationMessage deserialize(DataInputStream in) throws IOException {
        return new ReservationMessage(in.readUTF(), Localizacao.deserialize(in));

    }
}
