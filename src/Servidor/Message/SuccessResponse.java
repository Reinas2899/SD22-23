package Servidor.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SuccessResponse {
    private boolean flag;
    private String message;

    public SuccessResponse(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeBoolean(flag);
        out.writeUTF(message);
        out.flush();
    }

    public static SuccessResponse deserialize(DataInputStream in) throws IOException {
        return new SuccessResponse(in.readBoolean(), in.readUTF());
    }


}
