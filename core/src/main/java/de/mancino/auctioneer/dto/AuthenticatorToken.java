package de.mancino.auctioneer.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "authenticatortoken")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AuthenticatorToken implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;

    public AuthenticatorToken() {
        this("");
    }

    @SuppressWarnings("unchecked")
    public AuthenticatorToken(final String token) {
        this.token = token;
    }


    @XmlElement
    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}
