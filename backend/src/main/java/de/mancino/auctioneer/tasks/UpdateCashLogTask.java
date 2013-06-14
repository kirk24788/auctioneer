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
import de.mancino.armory.json.vault.money.Money;
import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.dto.ArmoryCharacter;
import de.mancino.auctioneer.dto.components.Currency;

/**
 * Task for updating the cash log.
 *
 * @author mmancino
 */
public class UpdateCashLogTask extends AuctioneerTask {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UpdateCashLogTask.class);

    /**
     * Armory
     */
    private final Armory armory;

    /**
     * Armory Character BO
     */
    private final ArmoryCharacterBO armoryCharacterBO;


    public UpdateCashLogTask(final ArmoryCharacterBO armoryCharacterBO, final Armory armory, ErrorLogBO errorLogBO) {
        super(errorLogBO);
        this.armoryCharacterBO = armoryCharacterBO;
        this.armory = armory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runInternal() {

        LOG.info("Cash Log Size: {} characters", armoryCharacterBO.listArmoryCharacters().size());
        final long updateTime = System.currentTimeMillis();
        for(ArmoryCharacter armoryCharacter : armoryCharacterBO.listArmoryCharacters()) {
            try {
                LOG.debug("Updating Cash Log for: {}", armoryCharacter);
                changeCharacter(armoryCharacter);
                Money money = armory.vault.getMoney();
                if(!characterIsSelected(money, armoryCharacter)) {
                    changeCharacter(armoryCharacter);
                    money = armory.vault.getMoney();
                    if(!characterIsSelected(money, armoryCharacter)) {
                        changeCharacter(armoryCharacter);
                        money = armory.vault.getMoney();
                        if(!characterIsSelected(money, armoryCharacter)) {
                            throw new RequestException("Couldn't select Character " + armoryCharacter);
                        }
                    }
                }

                final Currency cash = new Currency(money.money);
                armoryCharacterBO.addCashSample(armoryCharacter.getId(), cash, updateTime);
            } catch (RequestException e) {
                errorLogBO.addException(e);
                LOG.error(e.getLocalizedMessage());
            }
        }
    }

    private boolean characterIsSelected(final Money money, final ArmoryCharacter armoryCharacter) {
        return money.character.name.equals(armoryCharacter.getCharacterName().toString())
                && money.character.realmName.equals(armoryCharacter.getRealmName().toString());
    }

    private void changeCharacter(final ArmoryCharacter armoryCharacter)
            throws RequestException {
        armory.vault.changeActiveChar(armoryCharacter.getCharacterName().toString(),
                armoryCharacter.getFaction(),
                armoryCharacter.getRealmName().toString());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Why wait?!?
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskTitle() {
        return "CashLog Update";
    }
}
