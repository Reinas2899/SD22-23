package Servidor;

public class NotificationThread implements Runnable {
    private String notificationMessage;
    private String recipient;

    public NotificationThread(String notificationMessage, String recipient) {
        this.notificationMessage = notificationMessage;
        this.recipient = recipient;
    }

    @Override
    public void run() {


        //this method will contain the code that sends the notification to the recipient. You can use any method you prefer to send the notification (e.g. email, SMS, etc.).
    }
}