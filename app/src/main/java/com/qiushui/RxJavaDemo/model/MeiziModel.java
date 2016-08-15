package com.qiushui.RxJavaDemo.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * @author Qiushui
 */
public class MeiziModel extends RealmObject{

    private boolean error;

    private long time;

    private RealmList<ResultsBeen> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public RealmList<ResultsBeen> getResults() {
        return results;
    }

    public void setResults(RealmList<ResultsBeen> results) {
        this.results = results;
    }
}
