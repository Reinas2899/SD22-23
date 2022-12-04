package Entidades;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

public class Reservation {
    private String reservationCode;
    private Trotinete scooter;
    private Utilizador user;
    private Timestamp startTime;
    private Timestamp endTime;
    private Localizacao startLocation;
    private Localizacao endLocation;


    public Reservation(String reservationCode, Trotinete scooter, Utilizador user) {
        this.reservationCode = reservationCode;
        this.scooter = scooter;
        this.user = user;
        this.startLocation = scooter.getLocalizacao();
    }

    public void serialize (DataOutputStream out) throws IOException {
        out.writeUTF(reservationCode);
        scooter.serialize(out);
        user.serialize(out);
        out.flush();
    }

    public static Reservation deserialize (DataInputStream in) throws IOException {
        return new Reservation(in.readUTF(), Trotinete.deserialize(in), Utilizador.deserialize(in));

    }
}
