/**
 *
 */
package de.mancino.auctioneer.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mancino.auctioneer.dto.RealmStatus;

/**
 *
 * @author mmancino
 */
public class RealmStatusDAOMemoryImpl implements RealmStatusDAO {
    private static final long serialVersionUID = 1L;

    private final List<RealmStatus> serverStatusList = new ArrayList<RealmStatus>();

    /**
     * {@inheritDoc}
     */
    @Override
    public RealmStatus getLastServerStatus() {
        synchronized (serverStatusList) {
            if(serverStatusList.size()>=1) {
                return serverStatusList.get(serverStatusList.size()-1);
            } else {
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealmStatus> getAll() {
        synchronized (serverStatusList) {
            return Collections.unmodifiableList(new ArrayList<RealmStatus>(serverStatusList));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addServerStatus(RealmStatus serverStatus) {
        synchronized (serverStatusList) {
            serverStatusList.add(serverStatus);
            Collections.sort(serverStatusList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllByMaxTimestamp(long maxTimestamp) {
        synchronized (serverStatusList) {
            int delIndex = -1;
            for(int i = 0 ; i < serverStatusList.size() ; i++) {
                if(serverStatusList.get(i).getTimestamp() <= maxTimestamp) {
                    delIndex = i;
                }
            }
            for( ; delIndex >= 0 ; delIndex-- ) {
                serverStatusList.remove(delIndex);
            }
        }
    }

}
