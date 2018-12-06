package com.hzgc.compare;


import java.util.List;

public class CompareParam {
    private List<String> ipcIds;
    private String dateStart;
    private String dateEnd;
    private List<Feature> features;
    private float sim;
    private int resultCount;
    private boolean isTheSamePerson;
    //排序参数
    private List<Integer> sort;

    public CompareParam(String dateStart, String dateEnd, List<Feature> features, float sim, int resultCount, boolean isTheSamePerson) {

        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.features = features;
        this.isTheSamePerson = isTheSamePerson;
        this.sim = sim;
        this.resultCount = resultCount;
    }

    public List<String> getIpcIds() {
        return ipcIds;
    }

    public void setIpcIds(List<String> ipcIds) {
        this.ipcIds = ipcIds;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public boolean isTheSamePerson() {
        return isTheSamePerson;
    }

    public float getSim() {
        return sim;
    }

    public int getResultCount() {
        return resultCount;
    }

    public List<Integer> getSort() {
        return sort;
    }

    public void setSort(List<Integer> sort) {
        this.sort = sort;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public void setSim(float sim) {
        this.sim = sim;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public void setTheSamePerson(boolean theSamePerson) {
        isTheSamePerson = theSamePerson;
    }

    @Override
    public String toString() {
        return "CompareParam{" +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", features=" + features +
                ", sim=" + sim +
                ", resultCount=" + resultCount +
                ", isTheSamePerson=" + isTheSamePerson +
                '}';
    }
}
