package de.mancino.auctioneer.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.exception.ExceptionUtils;

@XmlRootElement(name = "deal")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ErrorEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private long timestamp;
    private Class<Throwable> throwableClass;
    private String stackTrace;

    public ErrorEvent() {
        this(0,new RuntimeException());
    }

    @SuppressWarnings("unchecked")
    public ErrorEvent(final long timestamp, final Throwable throwable) {
        this.setTimestamp(timestamp);
        this.setThrowableClass((Class<Throwable>) throwable.getClass());
        this.setStackTrace(ExceptionUtils.getStackTrace(throwable));
    }

    /**
     * @return the timestamp
     */
    @XmlElement
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the throwableClass
     */
    @XmlElement
    public Class<Throwable> getThrowableClass() {
        return throwableClass;
    }

    /**
     * @param throwableClass the throwableClass to set
     */
    public void setThrowableClass(Class<Throwable> throwableClass) {
        this.throwableClass = throwableClass;
    }

    /**
     * @return the stackTrace
     */
    @XmlElement
    public String getStackTrace() {
        return stackTrace;
    }

    /**
     * @param stackTrace the stackTrace to set
     */
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    /**
     * @return the id
     */
    @XmlElement
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}
