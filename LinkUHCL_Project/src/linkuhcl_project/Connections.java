/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkuhcl_project;

/**
 *
 * @author asini
 */
public class Connections {
    private String senderId;
    private String receiverId;
    private String message;
    private String status;
    private String dateAndTime;

    public Connections(String senderId, String receiverId, String message, String status, String dateAndTime) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.status = status;
        this.dateAndTime = dateAndTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    
    
}
