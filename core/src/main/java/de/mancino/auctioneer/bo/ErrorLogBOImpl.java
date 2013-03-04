package de.mancino.auctioneer.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.mancino.auctioneer.dao.ErrorLogDAO;
import de.mancino.auctioneer.dto.ErrorEvent;
import de.mancino.auctioneer.exceptions.ErrorEventDoesnNotExistException;

public class ErrorLogBOImpl implements ErrorLogBO {
    private static final long serialVersionUID = 1L;

    private final ErrorLogDAO errorLogDAO;

    public ErrorLogBOImpl(final ErrorLogDAO errorLogDAO) {
        this.errorLogDAO = errorLogDAO;
    }


    @Override
    public ErrorEvent addException(Throwable throwable) {
        return errorLogDAO.insert(new ErrorEvent(System.currentTimeMillis(), throwable));
    }


    @Override
    public ErrorEvent getById(int id) throws ErrorEventDoesnNotExistException {
        return errorLogDAO.getById(id);
    }


    @Override
    public void removeAll() {
        errorLogDAO.deleteAllByMaxTimestamp(Long.MAX_VALUE);
    }


    @Override
    public List<ErrorEvent> getAll() {
        final List<ErrorEvent> sorted = new ArrayList<>(errorLogDAO.getAll());
        Collections.sort(sorted, new Comparator<ErrorEvent>() {
            @Override
            public int compare(ErrorEvent o1, ErrorEvent o2) {
                if(o1.getTimestamp() < o2.getTimestamp()) {
                    return 1;
                } else if(o1.getTimestamp() > o2.getTimestamp()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return Collections.unmodifiableList(sorted);
    }
}
