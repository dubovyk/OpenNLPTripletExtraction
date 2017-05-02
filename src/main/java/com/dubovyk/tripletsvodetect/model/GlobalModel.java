package com.dubovyk.tripletsvodetect.model;

import com.dubovyk.tripletsvodetect.NLP.model.TripletSVO;
import com.dubovyk.tripletsvodetect.NLP.nlp.TripletSVODetector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class GlobalModel {
    private static GlobalModel instance;

    private final ObservableList<TripletSVO> currentData = FXCollections.observableArrayList();

    private POSModel model;
    private POSTaggerME tagger;
    private TripletSVODetector detector = new TripletSVODetector();
    private GlobalModel(){

    }

    public static GlobalModel getInstance(){
        if (instance == null){
            instance = new GlobalModel();
        }
        return instance;
    }

    public void parseText(String text){
        text = text.replace("\"", "");
        System.out.println(text);
        currentData.addAll(detector.getRelationsFromText(text));
    }

    public void parseText(String[] sentences){
        String data = String.join(" ", sentences);
        data = data.replace(" ", "");
        currentData.addAll(detector.getRelationsFromText(data));
    }

    public void clear(){
        currentData.clear();
    }

    public void addAll(Collection<TripletSVO> triplets){
        currentData.addAll(triplets);
    }

    public void addTriplet(TripletSVO triplet){
        currentData.add(triplet);
    }

    public void removeTriplet(TripletSVO triplet){
        currentData.remove(triplet);
    }

    public ObservableList<TripletSVO> getCurrentData(){
        return currentData;
    }
}
