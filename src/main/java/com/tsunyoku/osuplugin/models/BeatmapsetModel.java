package com.tsunyoku.osuplugin.models;

import com.tsunyoku.osuplugin.enums.RankedStatus;

public class BeatmapsetModel {
    public int SetID;
    public BeatmapModel[] ChildrenBeatmaps;
    public RankedStatus RankedStatus;
    public String ApprovedDate;
    public String LastUpdate;
    public String LastChecked;
    public String Artist;
    public String Title;
    public String Creator;
    public String Source;
    public String Tags;
    public boolean HasVideo;
    public int Genre;
    public int Language;
    public int Favourites;
}
