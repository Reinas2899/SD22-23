package Servidor.Message;//SD Protocol
import Entidades.Localizacao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


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

