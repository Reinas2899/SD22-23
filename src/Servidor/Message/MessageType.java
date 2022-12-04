package Servidor.Message;

public enum MessageType {
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
