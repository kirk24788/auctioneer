package de.mancino.auctioneer.dto.components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import de.mancino.armory.json.api.item.Item;

/**
 * Armory Icon.
 *
 * Das Armory Icon wird intern als byte-Array representiert.
 * Es sollte immer ein JPEG-Bild sein, es sei denn es wurde
 * kein Bild gefunden/das Bild konnte nicht geladen werden.
 * In dem Fall wird ein Fallback-PNG aus dem Resource-Verzeichnis
 * genommen.
 *
 * Ausserdem besteht noch die Möglichkeit direct ein Byte-Array
 * zu übergeben, hierbei muss darauf geachtet werden, dass es sich
 * um ein gültiges Bild handelt - deshalb sollte hiervon abstand genommen
 * werden!
 *
 * @author mmancino
 */
@Embeddable
@Entity
@XmlRootElement(name = "armoryitem")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ArmoryIcon implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final byte[] NO_ICON;

    static {
        try {
            NO_ICON = IOUtils.toByteArray(ArmoryIcon.class.getResourceAsStream("/no-icon.png"));
        } catch (IOException e) {
            throw new RuntimeException("Missing 'no-icon.png'");
        }
    }

    @Lob
    @Column(name = "ITEM_ICON", unique = false, nullable = true)
    private byte[] armoryIcon;

    /**
     * @Deprecated Package-Protected Konstruktor - nur für Hibernate Injection!
     */
    @Deprecated
    ArmoryIcon() {
        this.setArmoryIcon(NO_ICON);
    }

    /**
     * Armory Icon.
     *
     * Das Armory Icon wird intern als byte-Array representiert.
     * Es sollte immer ein JPEG-Bild sein, es sei denn es wurde
     * kein Bild gefunden/das Bild konnte nicht geladen werden.
     * In dem Fall wird ein Fallback-PNG aus dem Resource-Verzeichnis
     * genommen.
     *
     * @param item Armory Item
     */
    public ArmoryIcon(final Item item) {
        this(item.name);
    }

    /**
     * Armory Icon.
     *
     * Das Armory Icon wird intern als byte-Array representiert.
     * Es sollte immer ein JPEG-Bild sein, es sei denn es wurde
     * kein Bild gefunden/das Bild konnte nicht geladen werden.
     * In dem Fall wird ein Fallback-PNG aus dem Resource-Verzeichnis
     * genommen.
     *
     * @param iconName Icon Name
     */
    public ArmoryIcon(final String iconName) {
        refetchItem(iconName);
    }

    /**
     * Armory Icon direkt aus einem Byte-Array.
     * Hierbei muss darauf geachtet werden, dass es sich um ein gültiges Bild
     * handelt - deshalb sollte hiervon abstand genommen werden!
     *
     * @param armoryIcon Bild als Byte-Array
     */
    public ArmoryIcon(final byte[] armoryIcon) {
        if(armoryIcon == null) {
            throw new IllegalArgumentException("Armory Icon must not be null!");
        }
        this.setArmoryIcon(armoryIcon);
    }

    /**
     * Armory Icon.
     *
     * Das Armory Icon wird intern als byte-Array representiert.
     * Es sollte immer ein JPEG-Bild sein, es sei denn es wurde
     * kein Bild gefunden/das Bild konnte nicht geladen werden.
     * In dem Fall wird ein Fallback-PNG aus dem Resource-Verzeichnis
     * genommen.
     *
     * @param itemTooltip Item-Tooltip für den das Icon geladen werden soll
     */
    public static ArmoryIcon armoryIcon(final Item item) {
        return new ArmoryIcon(item);
    }

    /**
     * Armory Icon direkt aus einem Byte-Array.
     * Hierbei muss darauf geachtet werden, dass es sich um ein gültiges Bild
     * handelt - deshalb sollte hiervon abstand genommen werden!
     *
     * @param armoryIcon Bild als Byte-Array
     */
    public static ArmoryIcon armoryIcon(final byte[] armoryIcon) {
        return new ArmoryIcon(armoryIcon);
    }

    /**
     * Liefert die interne Representation des Armory Icons.
     * Das Armory Icon wird intern als byte-Array representiert.
     * Es sollte immer ein JPEG-Bild sein, es sei denn es wurde
     * kein Bild gefunden/das Bild konnte nicht geladen werden.
     * In dem Fall wird ein Fallback-PNG aus dem Resource-Verzeichnis
     * genommen.
     *
     * @return Armory Icon als Byte Array
     */
    public byte[] toByteArray() {
        return getArmoryIcon();
    }

    /**
     * Gibt zurück ob es sich um ein 'gültiges' Bild handelt - also
     * ob es von den Blizzard Servern geladen wurde (bzw. als Byte-Array
     * direkt injected) - oder ob das Fallback-PNG aus dem Resource-Verzeichnis
     * als Platzhalter vorhanden ist.
     *
     * @return false wenn das Fallback-PNG benutzt wird, true sonst
     */
    public boolean isValid() {
        return getArmoryIcon() != NO_ICON;
    }

    /**
     * Forciert das Laden des Icons von den Blizzard Servern.
     *
     * @param itemName Item-Name für den das Icon geladen werden soll
     */
    private void refetchItem(final String itemName) {
        try {
            setArmoryIcon(fetchImage(itemName));
        } catch (Exception e) {
            setArmoryIcon(NO_ICON);
        }
    }

    private byte[] fetchImage(final String iconName) throws Exception {
        // XXX: Einheitlich! G ibts auch bei wowhead!
        final String iconUrl = "http://media.blizzard.com/wow/icons/56/" + iconName.toLowerCase() + ".jpg";
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        final HttpGet fetchRequest = new HttpGet(iconUrl);
        try {
            final HttpResponse summaryResponse = httpClient.execute(fetchRequest);
            /// XXX: 404 redirects abfangen!!!
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(summaryResponse.getEntity().getContent(), baos);
            return baos.toByteArray();
        } catch (Exception e) {
            final String msg = "Error fetching icon '" + iconUrl + "'!";
            throw new Exception(msg);
        }
    }

    /**
     * @return the armoryIcon
     */
    @XmlElement
    public byte[] getArmoryIcon() {
        return armoryIcon;
    }

    /**
     * @param armoryIcon the armoryIcon to set
     */
    public void setArmoryIcon(byte[] armoryIcon) {
        this.armoryIcon = armoryIcon;
    }
}
