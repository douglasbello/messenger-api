package br.com.douglasbello.messenger.entities.enums;

public enum FriendshipRequestStatus {
    WAITING_RESPONSE(1),
    ACCEPTED(2),
    DECLINED(3);

    private int code;

    FriendshipRequestStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static FriendshipRequestStatus valueOf(int code) {
        for (FriendshipRequestStatus value : FriendshipRequestStatus.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid FriendshipRequestStatus code.");
    }
}