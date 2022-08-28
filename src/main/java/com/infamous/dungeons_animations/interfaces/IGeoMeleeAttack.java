package com.infamous.dungeons_animations.interfaces;

public interface IGeoMeleeAttack {
    boolean canMELEEATTACKING();
    Boolean getMELEEATTACKING();
    void setMELEEATTACKING(boolean r);
    void setcanMELEEATTACKING(boolean r);
    default int getMeleeType() {
        return 0;
    };
}
