package de.mancino.prowl;

public enum Priority {
    VERY_LOW(-2),  
    MODERATE(-1),  
    NORMAL(0),  
    HIGH(1),  
    EMERGENCY(2);  

    public final int value;  

    Priority(final int value){  
        this.value = value;  
    }
}
