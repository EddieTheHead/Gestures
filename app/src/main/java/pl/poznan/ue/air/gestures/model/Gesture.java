package pl.poznan.ue.air.gestures.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hubert Bossy on 2017-11-15.
 */

public class Gesture implements Serializable {
    public Gesture(List<Float> traceX, List<Float> traceY, List<Float> traceZ, String title, Actions action) {
        this.traceX = traceX;
        this.traceY = traceY;
        this.traceZ = traceZ;

        this.title = title;
        this.action = action;
    }

    public Gesture(String title) {
        this.title = title;
        this.action = Actions.NEXT_SLIDE;
    }

    public enum Actions {NEXT_SLIDE, PREV_SLIDE};
    private List<Float> traceX;
    private List<Float> traceY;
    private List<Float> traceZ;

    private String title;
    private Actions action;

    public List<Float> getTraceX() {
        return traceX;
    }
    public void setTraceX(List<Float> traceX) {
        this.traceX = traceX;
    }
    public Actions getAction() {
        return action;
    }
    public void setAction(Actions action) {
        this.action = action;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setTraceY(List<Float> traceY) {
        this.traceY = traceY;
    }
    public List<Float> getTraceZ() {
        return traceZ;
    }
    public void setTraceZ(List<Float> traceZ) {
        this.traceZ = traceZ;
    }
    public List<Float> getTraceY() {
        return traceY;
    }

}
