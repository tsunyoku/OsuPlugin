package com.tsunyoku.osuplugin.enums;

public enum RankedStatus {
    Graveyard,
    WIP,
    Pending,
    Ranked,
    Approved,
    Qualified,
    Loved;

    public final int value = ordinal() - 2;
}
