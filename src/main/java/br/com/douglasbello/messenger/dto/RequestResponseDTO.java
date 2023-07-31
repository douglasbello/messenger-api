package br.com.douglasbello.messenger.dto;

public class RequestResponseDTO {
    private int status;
    private String message;

    public RequestResponseDTO() {

    }

    public RequestResponseDTO(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
