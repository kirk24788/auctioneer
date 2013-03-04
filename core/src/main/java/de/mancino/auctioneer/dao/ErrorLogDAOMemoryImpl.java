package de.mancino.auctioneer.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mancino.auctioneer.dto.ErrorEvent;
import de.mancino.auctioneer.exceptions.ErrorEventDoesnNotExistException;

public class ErrorLogDAOMemoryImpl implements ErrorLogDAO {
    private static final long serialVersionUID = 1L;
    private List<ErrorEvent> errorEvents = new ArrayList<>();

    private int getNextId() {
        int nextId = 1;
        for(final ErrorEvent errorEvent : errorEvents) {
            if(nextId <= errorEvent.getId()) {
                nextId = errorEvent.getId()+1;
            }
        }
        return nextId;
    }

    @Override
    public ErrorEvent insert(final ErrorEvent errorEvent) {
        synchronized (errorEvents) {
            errorEvents.add(errorEvent);
            errorEvent.setId(getNextId());
            return errorEvent;
        }
    }

    @Override
    public void delete(final ErrorEvent errorEvent) {
        synchronized (errorEvents) {
            errorEvents.remove(errorEvent);
        }
    }

    @Override
    public void deleteAllByMaxTimestamp(long maxTimestamp) {
        final List<ErrorEvent> deleteList = new ArrayList<>();
        synchronized (errorEvents) {
            for(final ErrorEvent errorEvent : errorEvents) {
                if(errorEvent.getTimestamp()<maxTimestamp) {
                    deleteList.add(errorEvent);
                }
            }
            errorEvents.removeAll(deleteList);
        }
    }

    @Override
    public ErrorEvent getById(int id) throws ErrorEventDoesnNotExistException {
        synchronized (errorEvents) {
            for(final ErrorEvent errorEvent : errorEvents) {
                if(errorEvent.getId()==id) {
                    return errorEvent;
                }
            }
            throw new ErrorEventDoesnNotExistException(id);
        }
    }

    @Override
    public List<ErrorEvent> getAll() {
        synchronized (errorEvents) {
            return Collections.unmodifiableList(errorEvents);
        }
    }

    @Override
    public int getSize() {
        synchronized (errorEvents) {
            return errorEvents.size();
        }
    }
}
