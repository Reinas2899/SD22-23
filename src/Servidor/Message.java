package Servidor;//SD Protocol
import Entidades.Localizacao;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import static Servidor.MessageType.SUCCESS_RESPONSE;

enum MessageType {
    SUCCESS_RESPONSE,
    REGISTER,
    CONNECTION,
    DESCONNECTION,
    NEARBY_SCOOTERS,
    LIST_SCOOTERS,
    NEARBY_REWARDS,
    LIST_REWARDS,
    SCOOTER_RESERVATION_REQUEST,
    SCOOTER_RESERVATION_RESPONSE,
    START_TRIP,
    END_TRIP,
    COST_REWARD;

    public static MessageType fromInteger(int x) {
        return switch (x) {
            case 0 -> SUCCESS_RESPONSE;
            case 1 -> REGISTER;
            case 2 -> CONNECTION;
            case 3 -> DESCONNECTION;
            case 4 -> NEARBY_SCOOTERS;
            case 5 -> LIST_SCOOTERS;
            case 6 -> NEARBY_REWARDS;
            case 7 -> LIST_REWARDS;
            case 8 -> SCOOTER_RESERVATION_REQUEST;
            case 9 -> SCOOTER_RESERVATION_RESPONSE;
            case 10 -> START_TRIP;
            case 11 -> END_TRIP;
            case 12 -> COST_REWARD;
            default -> null;
        };
    }
    public static int toInteger(MessageType x) {
        return switch (x) {
            case SUCCESS_RESPONSE -> 0;
            case REGISTER -> 1;
            case CONNECTION -> 2;
            case DESCONNECTION -> 3;
            case NEARBY_SCOOTERS -> 4;
            case LIST_SCOOTERS -> 5;
            case NEARBY_REWARDS -> 6;
            case LIST_REWARDS -> 7;
            case SCOOTER_RESERVATION_REQUEST -> 8;
            case SCOOTER_RESERVATION_RESPONSE -> 9;
            case START_TRIP -> 10;
            case END_TRIP -> 11;
            case COST_REWARD -> 12;
            default -> -1;
        };
    }
}


public class Message {
    private MessageType type;
    private Object message;

    public Message(MessageType type, Object message){
        this.type=type;
        this.message = message;
    }

    public MessageType getType(){return this.type;}
    //public int getType(){return MessageType.toInteger(this.type);}
    public void setType (MessageType type){this.type = type;}
    //public void setType (int type){this.type = MessageType.fromInteger(type);}
    public Object getMessage(){return this.message;}
    public void setMessage(Object message){this.message = message;}

    public static Message deserialize (DataInputStream in) throws IOException {
        MessageType type = MessageType.fromInteger(in.readInt());
        Object message = new Object();
        switch (type) {
            case SUCCESS_RESPONSE -> message = SuccessResponse.deserialize(in);
            case REGISTER, CONNECTION -> message = UserMessage.deserialize(in);
            case NEARBY_SCOOTERS, NEARBY_REWARDS, START_TRIP -> message = Localizacao.deserialize(in);
            case LIST_SCOOTERS, LIST_REWARDS -> message = ListObject.deserialize(in);
            case DESCONNECTION -> {
            }
            default -> {
            }
        }
        return new Message(type, message);
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(MessageType.toInteger(this.type));
        switch (this.type){
            case SUCCESS_RESPONSE:
                if(message instanceof SuccessResponse suRe)
                    suRe.serialize(out);
                break;
            case REGISTER:
            case CONNECTION:
                if(message instanceof UserMessage userMe)
                    userMe.serialize(out);
                break;
            case NEARBY_SCOOTERS:
            case NEARBY_REWARDS:
            case START_TRIP:
                if(message instanceof Localizacao loc)
                    loc.serialize(out);
                break;
            case LIST_SCOOTERS:
            case LIST_REWARDS:
                if(message instanceof ListObject list)
                    list.serialize(out);
                break;
            case DESCONNECTION:
            default:
                break;
        }

        out.flush();
    }

}

class SuccessResponse {
    private boolean flag;
    private String message;

    public SuccessResponse(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public void serialize (DataOutputStream out) throws IOException {
        out.writeBoolean(flag);
        out.writeUTF(message);
        out.flush();
    }

    public static SuccessResponse deserialize (DataInputStream in) throws IOException {
        return new SuccessResponse(in.readBoolean(), in.readUTF());
    }


}

class UserMessage{
    private String userID;
    private String password;
    public UserMessage(String userID, String password) throws IOException {
        this.userID = userID;
        this.password = password;
    }

    public void serialize (DataOutputStream out) throws IOException {
        out.writeUTF(userID);
        out.writeUTF(password);
        out.flush();
    }

    public static UserMessage deserialize (DataInputStream in) throws IOException {
        return new UserMessage(in.readUTF(), in.readUTF());
    }
}

class ListObject{
    private int size;
    private List<Entidades.Localizacao> objects;

    public ListObject(int size, List<Entidades.Localizacao> objects) throws IOException {
        this.size = size;
        this.objects = objects;
    }
    public void serialize (DataOutputStream out) throws IOException {
        out.writeInt(size);
        for (Localizacao loc:objects){
            out.writeFloat(loc.getX());
            out.writeFloat(loc.getY());
        }
        out.flush();
    }

    public static ListObject deserialize (DataInputStream in) throws IOException {
        int size = in.readInt();
        List<Entidades.Localizacao> objects = null;

        for (int i = 0; i < size; i++) {
            Localizacao c = Localizacao.deserialize(in);
            objects.add(c);
        }
        return new ListObject(size, objects);

    }
}

class ReservationMessage{
    private String reservationCode;
    private Localizacao location;
    public ReservationMessage(String reservationCode, Localizacao location) throws IOException {
        this.reservationCode = reservationCode;
        this.location = location;
    }

    public void serialize (DataOutputStream out) throws IOException {
        out.writeUTF(reservationCode);
        out.writeFloat(location.getX());
        out.writeFloat(location.getY());
        out.flush();
    }

    public static ReservationMessage deserialize (DataInputStream in) throws IOException {
        return new ReservationMessage(in.readUTF(), Localizacao.deserialize(in));

    }
}

