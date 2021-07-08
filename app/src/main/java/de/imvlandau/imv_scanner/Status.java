package de.imvlandau.imv_scanner;

public enum Status {
    ACCEPTED, ALREADY_HERE, NOT_REGISTERED, BAD_URL;


    public static Status fromInteger(int value) {
        switch (value) {
            case -1:
                return Status.BAD_URL;
            case 202:
                return Status.ACCEPTED;
            case 226:
                return Status.ALREADY_HERE;
            default:
                return NOT_REGISTERED;
        }
    }
}
