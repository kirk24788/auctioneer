package de.mancino.auctioneer.webapp.components;

import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.SharedResources;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.util.time.Time;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.webapp.AuctioneerWebApp;

public class ArmoryImage {

    public static Image createArmoryImage(final AuctioneerWebApp app, final String id, final ArmoryItem armoryItem) {
        final SharedResources sharedResources = app.getSharedResources(); 
        final String imageName = "armoryImage-" + armoryItem.getArmoryId().toString() + ".jpg"; 
        if (sharedResources.get(ArmoryImage.class, imageName, null, null, false) == null) { 
            final Resource resource = new DynamicImageResource() {
                private static final long serialVersionUID = 1L;
                @Override 
                protected byte[] getImageData() {
                    setLastModifiedTime(Time.START_OF_UNIX_TIME);
                    return armoryItem.getArmoryIcon().toByteArray(); 
                }
            }.setCacheable(true); 
            sharedResources.putClassAlias(ArmoryImage.class, "armoryImages"); 
            sharedResources.add(ArmoryImage.class, imageName, null, null, resource); 
            app.mountSharedResource(imageName, imageName); 
        } 
        return new Image(id, new ResourceReference(ArmoryImage.class, imageName)); 
    }
    /*
     * 
http://auctioneer.mancino-net.de/;jsessionid=0ADD9AC9A1A43ADA715D4D56B49AB785?wicket:interface=:33:armoryItemList:5:armoryItemLine:icon::IResourceListener::
http://auctioneer.mancino-net.de/;jsessionid=0ADD9AC9A1A43ADA715D4D56B49AB785?wicket:interface=:36:armoryItemList:5:armoryItemLine:icon::IResourceListener::

http://auctioneer.mancino-net.de/;jsessionid=0ADD9AC9A1A43ADA715D4D56B49AB785?wicket:interface=:32:priceWatchList:0:priceWatchLine:icon::IResourceListener::
http://auctioneer.mancino-net.de/;jsessionid=0ADD9AC9A1A43ADA715D4D56B49AB785?wicket:interface=:34:priceWatchList:0:priceWatchLine:icon::IResourceListener::
     */
}
