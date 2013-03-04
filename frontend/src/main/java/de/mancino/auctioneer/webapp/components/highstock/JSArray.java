package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Generic JavaScript Array
 * 
 * @author mmancino
 */
public class JSArray<P extends JSObject,T> extends JSObject implements Serializable {
    private static final long serialVersionUID = Highstock.VERSION;
    
    private ArrayList<T> data = new ArrayList<T>();
    private final int identLevel;
    
    private final P parent;
    
    /**
     * Generic JavaScript Array.
     * 
     * @param identLevel Ident level for this array.
     */
    JSArray(P parent) {
        this.identLevel = parent.identLevel() + JSObject.IDENT_PER_LEVEL;
        this.parent = parent;
    }
    
    /**
     * Generic JavaScript Array.
     * 
     * @param identLevel Ident level for this array.
     */
    JSArray(P parent, T ... elements) {
        this.identLevel = parent.identLevel() + JSObject.IDENT_PER_LEVEL;
        this.parent = parent;
        for(T element : elements) {
            data.add(element);
        }
    }
    
    /**
     * Clear all array entries.
     * 
     * @return self
     */
    public JSArray<P, T> clear() {
        data.clear();
        return this;
    }
    

    /**
     * Add object to JS Array.
     * 
     * @param o object to add
     * @return self
     */
    public JSArray<P, T> add(T o) {
        data.add(o);
        return this;
    }

    /**
     * Returns to the parent object.
     * 
     * @return parent object
     */
    public P done() {
        return parent;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return identLevel;
    }

    /**
     * Only for JSObject printing!
     * @return internal Data
     */
    ArrayList<T> getData() {
        return data;
    }
}
