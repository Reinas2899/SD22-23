package Servidor.Message;//SD Protocol
import Entidades.Localizacao;
import Entidades.Utilizador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class Message {
    private MessageType type;
    private Object message;

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type.toString() +
                "message=" + message.toString() +
                '}';
    }

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
            case REGISTER -> message = Utilizador.deserializeBasics(in);
            case CONNECTION -> message = Utilizador.deserializeBasics(in);
            case NEARBY_SCOOTERS -> message = Localizacao.deserialize(in);
            case NEARBY_REWARDS -> message = Localizacao.deserialize(in);
            case START_TRIP -> message = Localizacao.deserialize(in);
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
                if(message instanceof Utilizador user)
                    user.serializeBasics(out);
                break;
            case CONNECTION:
                if(message instanceof Utilizador user)
                    user.serializeBasics(out);
                break;
            case NEARBY_SCOOTERS:
                if(message instanceof Localizacao loc)
                    loc.serialize(out);

            case NEARBY_REWARDS:
                if(message instanceof Localizacao loc)
                    loc.serialize(out);
            case START_TRIP:
                if(message instanceof Localizacao loc)
                    loc.serialize(out);
                break;
            case LIST_SCOOTERS:
                if(message instanceof ListObject list)
                    list.serialize(out);
                break;
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

