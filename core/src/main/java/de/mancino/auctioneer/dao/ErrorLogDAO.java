package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.ErrorEvent;
import de.mancino.auctioneer.exceptions.ErrorEventDoesnNotExistException;

public interface ErrorLogDAO extends Serializable {
    public ErrorEvent insert(final ErrorEvent deal);
    public void delete(final ErrorEvent deal);
    public void deleteAllByMaxTimestamp(final long maxTimestamp);
    public ErrorEvent getById(final int id) throws ErrorEventDoesnNotExistException;
    public List<ErrorEvent> getAll();
    public int getSize();
}
