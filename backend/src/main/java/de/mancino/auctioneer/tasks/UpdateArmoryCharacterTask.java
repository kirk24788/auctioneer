/*
 * UpdatePriceWatch.java 11.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.auctioneer.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.armory.Armory;
import de.mancino.armory.exceptions.RequestException;
import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.dto.ArmoryCharacter;

/**
 * Task for updating the cash log.
 *
 * @author mmancino
 */
public class UpdateArmoryCharacterTask extends AuctioneerTask {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UpdateArmoryCharacterTask.class);

    /**
     * Armory
     */
    private final Armory armory;

    /**
     * Armory Character BO
     */
    private final ArmoryCharacterBO armoryCharacterBO;

    private final boolean forceIlvlUpdate;


    public UpdateArmoryCharacterTask(final ArmoryCharacterBO armoryCharacterBO, final Armory armory, final boolean forceIlvlUpdate, ErrorLogBO errorLogBO) {
        super(errorLogBO);
        this.armoryCharacterBO = armoryCharacterBO;
        this.armory = armory;
        this.forceIlvlUpdate = forceIlvlUpdate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runInternal() {

        LOG.info("Armory Characters: {}", armoryCharacterBO.listArmoryCharacters().size());
        for(ArmoryCharacter dbCharacter : armoryCharacterBO.listArmoryCharacters()) {
            try {
                if(dbCharacter.getLevel()>=10) {
                    LOG.debug("Updating Armory Character: {}", dbCharacter);

                    de.mancino.armory.json.api.character.Character liveCharacter = armory.api.getCharacterInfo(
                            dbCharacter.getRealmName().toString(),
                            dbCharacter.getCharacterName().toString());
                    boolean changed = false;
                    changed = updateAverageItemLevel(dbCharacter, liveCharacter) || changed;
                    changed = updateAverageItemLevelEquipped(dbCharacter, liveCharacter) || changed;
                    changed = updateLevel(dbCharacter, liveCharacter) || changed;

                    if(changed) {
                        armoryCharacterBO.updateArmoryCharacter(dbCharacter);
                        LOG.info("Armory Character has changed: {}", dbCharacter);
                    }
                } else {
                    LOG.debug("Skipping Low Level ({}) Armory Character: {}", dbCharacter.getLevel(), dbCharacter);
                }

            } catch (RequestException e) {
                errorLogBO.addException(e);
                LOG.error(e.getLocalizedMessage());
            }
        }
    }

    private boolean updateLevel(ArmoryCharacter dbCharacter, de.mancino.armory.json.api.character.Character liveCharacter) {
        if(dbCharacter.getLevel() != liveCharacter.level) {
            dbCharacter.setLevel(liveCharacter.level);
            return true;
        }
        return false;
    }

    private boolean updateAverageItemLevelEquipped(ArmoryCharacter dbCharacter,
            de.mancino.armory.json.api.character.Character liveCharacter) {
        if(dbCharacter.getAverageItemLevelEquipped() != liveCharacter.items.averageItemLevelEquipped) {
            dbCharacter.setAverageItemLevelEquipped(liveCharacter.items.averageItemLevelEquipped);
            return true;
        }
        return false;
    }

    private boolean updateAverageItemLevel(ArmoryCharacter dbCharacter, de.mancino.armory.json.api.character.Character liveCharacter) {
        if(dbCharacter.getAverageItemLevel() < liveCharacter.items.averageItemLevel ||
                (dbCharacter.getAverageItemLevel() > liveCharacter.items.averageItemLevel && forceIlvlUpdate)) {
            dbCharacter.setAverageItemLevel(liveCharacter.items.averageItemLevel);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskTitle() {
        return "ArmoryCharacter Update";
    }
}
