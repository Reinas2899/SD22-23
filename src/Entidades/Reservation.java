package Entidades;

import Servidor.Message.ReservationMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

public class Reservation {
    //identifier
    private String reservationCode;
    //What
    private Trotinete scooter;
    //When
    private Timestamp startTime;
    private Timestamp endTime;
    //Where
    private final Localizacao startLocation;
    private Localizacao endLocation;


    public Reservation(String reservationCode, Trotinete scooter) {
        this.reservationCode = reservationCode;
        this.scooter = scooter;
        this.startLocation = scooter.getLocalizacao();
        this.startTime = new Timestamp(System.currentTimeMillis());
    }

    public Reservation(String reservationCode, Localizacao scooterLocation) {
        this.reservationCode = reservationCode;
        //search for scooter
        this.startLocation = scooterLocation;
    }

    public void endReservation(){

    }

    public void serialize (DataOutputStream out) throws IOException {
        out.writeUTF(reservationCode);
        scooter.serialize(out);
        out.flush();
    }

    public void serializeBasics (DataOutputStream out) throws IOException {
        out.writeUTF(reservationCode);
        out.writeFloat(scooter.getLocalizacao().getX());
        out.writeFloat(scooter.getLocalizacao().getY());
        out.flush();
    }

    public static Reservation deserialize (DataInputStream in) throws IOException {
        return new Reservation(in.readUTF(), Trotinete.deserialize(in));
    }

    public static Reservation deserializeBasics (DataInputStream in) throws IOException {
        return new Reservation(in.readUTF(), Localizacao.deserialize(in));
    }
}
