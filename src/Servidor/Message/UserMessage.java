package Servidor.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UserMessage {
    private String userID;
    private String password;

    public UserMessage(String userID, String password) throws IOException {
        this.userID = userID;
        this.password = password;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(userID);
        out.writeUTF(password);
        out.flush();
    }

    public static UserMessage deserialize(DataInputStream in) throws IOException {
        return new UserMessage(in.readUTF(), in.readUTF());
    }
}
