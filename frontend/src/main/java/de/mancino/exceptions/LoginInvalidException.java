package de.mancino.exceptions;

public class LoginInvalidException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public LoginInvalidException(final String msg) {
        super(msg);
    }

}
