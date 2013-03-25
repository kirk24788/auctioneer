package de.mancino.auctioneer.bo;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.ErrorEvent;
import de.mancino.auctioneer.exceptions.ErrorEventDoesnNotExistException;

public interface ErrorLogBO extends Serializable {
    public ErrorEvent addException(Throwable throwable);
    public ErrorEvent getById(int id) throws ErrorEventDoesnNotExistException;
    public void removeAll();
    public List<ErrorEvent> getAll();
    public void remove(final long maxTimestamp);
}
