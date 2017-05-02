package com.dubovyk.tripletsvodetect.NLP.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * This is a class for representing the Subject-Verb-Object relation.
 * Also, it uses SimpleStringProperty for fields instead of String as
 * instances of this class are used to give data for table in the GUI application.
 *
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class TripletSVO {
    private final SimpleStringProperty subject = new SimpleStringProperty("");
    private final SimpleStringProperty object = new SimpleStringProperty("");
    private final SimpleStringProperty relation = new SimpleStringProperty("");
    private double confidence;
    public TripletSVO(){}

    public TripletSVO(String subject, String relation, String object){
        this.subject.set(subject);
        this.relation.set(relation);
        this.object.set(object);
    }

    public String getSubject() {
        return subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getRelation() {
        return relation.get();
    }

    public void setRelation(String relation) {
        this.relation.set(relation);
    }

    public String getObject() {
        return object.get();
    }

    public void setObject(String object) {
        this.object.set(object);
    }

    public boolean isComplete(){
        return !(subject.get().isEmpty() || relation.get().isEmpty() || object.get().isEmpty());
    }

    public String toString(){
        return "Subject-Relation-Object triplet is  { " + subject.get() + " } -- { " + relation.get() + " } -- { " + object.get() + " }.";
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
