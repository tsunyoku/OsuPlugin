package com.tsunyoku.osuplugin.enums;

public enum RankedStatus {
    Graveyard(-2),
    WIP(-1),
    Pending(0),
    Ranked(1),
    Approved(2),
    Qualified(3),
    Loved(4);

    private int key;
    RankedStatus(int key) {
        this.key = key;
    }

    public static RankedStatus findByAbbr(int key) {
        for (RankedStatus status: values()) {
            if (status.key == key) { return status; }
        }

        return null;
    }
}