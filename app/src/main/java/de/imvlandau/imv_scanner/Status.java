package de.imvlandau.imv_scanner;

import androidx.annotation.NonNull;

public enum Status {
    ACCEPTED("akzeptiert"),
    ALREADY_HERE("nchon da"),
    NOT_REGISTERED("nicht registriert"),
    WRONG_URL("falsch URL");

    Status(String staringValue) {
        this.stringValue = staringValue;
    }

    private final String stringValue;

    public static Status fromInteger(int value) {
        switch (value) {
            case -1:
            case 405:
                return Status.WRONG_URL;
            case 202:
                return Status.ACCEPTED;
            case 226:
                return Status.ALREADY_HERE;
            default:
                return NOT_REGISTERED;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return this.stringValue;
    }
}
