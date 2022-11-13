//SD Protocol

import java.sql.Timestamp;

enum MessageType {
    SCOOTER_REQUEST,
    START_TRIP,
    END_TRIP
}


public class SDP {
    private int type;

    private String userID;
    private String scooterID;
    private Timestamp timestamp;


}