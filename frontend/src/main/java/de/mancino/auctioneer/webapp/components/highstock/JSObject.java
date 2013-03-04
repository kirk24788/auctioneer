package de.mancino.auctioneer.webapp.components.highstock;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;

import de.mancino.auctioneer.dto.components.Color;

/**
 * JavaScript Object Base Class.
 * 
 * Formatting is set here globally for all JS Objects.
 * This class also provides a method for printing JS Objects.
 * 
 * @author mmancino
 */
public abstract class JSObject {
    /**
     * Pretty Formatting?
     */
    @JSIgnore
    protected static final boolean prettyFormat = true;
    /**
     * Line Break, if {@link #prettyFormat}prettyFormat is set to true.
     */
    @JSIgnore
    protected static final String LINE_BREAK = prettyFormat ? "\n" : "";
    
    /**
     * Number of whitepsaces per level
     */
    @JSIgnore
    public static final int IDENT_PER_LEVEL = 2;

    /**
     * Ident Whitespaces, if {@link #prettyFormat}prettyFormat is set to true.
     * 
     * @return Ident Whitespaces
     */
    protected String IDENT() {
        return prettyFormat ? StringUtils.repeat(" ", identLevel()): "";
    }

    /**
     * Print the member with given name as JS attribute.
     * 
     * @param fieldName attribute name
     * 
     * @return JS attribute as string
     */
    protected String printObject(final String fieldName) {
        Class<? extends JSObject> actualClass = this.getClass();
        try {
            final Field field = actualClass.getDeclaredField(fieldName);
            if(!field.isAnnotationPresent(JSIgnore.class)) {
                field.setAccessible(true);
                try {
                    final Object fieldValue = field.get(this);
                    if(fieldValue != null) {
                        final String formattedFieldValue;
                        if(fieldValue.getClass().equals(Color.class)) {
                            formattedFieldValue = "'#" + fieldValue.toString() + "'";
                        } else if (fieldValue.getClass().equals(String.class)) {
                            formattedFieldValue = "\"" + fieldValue.toString() + "\""; 
                        } else if (fieldValue.getClass().equals(JSArray.class)) {
                            StringBuffer sb = new StringBuffer();
                            @SuppressWarnings("unchecked")
                            JSArray<JSObject, Object> objectList = ((JSArray<JSObject, Object>) field.get(this));
                            sb.append("[").append(LINE_BREAK);
                            for(Object o : objectList.getData()) {
                                if(o == null) {
                                    sb.append(objectList.IDENT()).append("null").append(",").append(LINE_BREAK);
                                } else if(o.getClass().equals(Color.class)) {
                                    sb.append(objectList.IDENT()).append("'#" + o.toString() + "'").append(",").append(LINE_BREAK);
                                } else if (o.getClass().equals(String.class)) {
                                    sb.append(objectList.IDENT()).append("\"" + o.toString() + "\"").append(",").append(LINE_BREAK); 
                                } else {
                                    sb.append(objectList.IDENT()).append(o.toString()).append(",").append(LINE_BREAK);
                                }

                            }
                            sb.append(objectList.IDENT()).append("]");
                            formattedFieldValue = sb.toString();
                        } else {
                            formattedFieldValue = fieldValue.toString();
                        }
                        return  IDENT() + fieldName + ": " + formattedFieldValue + ", " + LINE_BREAK;
                    } else {
                        return "";
                    }
                } catch (IllegalArgumentException e) {
                    throw new IllegalStateException("Field '" + fieldName + "'  not found!", e);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Field '" + fieldName + "'  not accessible!", e);
                } 
            } else {
                return "";
            }
        } catch (SecurityException e) {
            throw new IllegalStateException("Field '" + fieldName + "' not accessible!", e);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Field '" + fieldName + "' not found!");
        }
    }

    /**
     * Generate the JavaScript Code for the Options regarding the chart area and plot area as well as general chart options.
     * 
     * @return self JavaScript Chart
     */
    public String toString() {
        return toString(true);
    }

    /**
     * Generate the JavaScript Code for the Options regarding the chart area and plot area as well as general chart options.
     * 
     * @return self JavaScript Chart
     */
    protected String toString(boolean enabled) {
        StringBuffer chart = new StringBuffer();
        if(enabled) {
            chart.append("{").append(LINE_BREAK);
            for(Field field : this.getClass().getDeclaredFields()) {
                //if(!field.getName().equals("serialVersionUID")) {
                chart.append(printObject(field.getName()));
                //}
            }
            chart.append(IDENT()).append("}");
        } else {
            chart.append("{ enabled: false }");
        }
        return chart.toString();
    }

    /**
     * Number of Whitespaces to ident.
     * 
     * @return Number of Whitespaces to ident.
     */
    protected abstract int identLevel();
}
