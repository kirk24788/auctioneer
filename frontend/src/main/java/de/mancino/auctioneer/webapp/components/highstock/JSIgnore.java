package de.mancino.auctioneer.webapp.components.highstock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ignore this field while serializing JS Object.
 * 
 * @author mmancino
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface JSIgnore {

}
