package com.dubovyk.tripletsvodetect.NLP.nlp;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to split a string to sentences.
 *
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class SentenceStringDetector {
    private SentenceDetectorME sentenceDetectorME;

    public SentenceStringDetector(String modelPath){
        try {
            InputStream inputStream = new FileInputStream(modelPath);
            SentenceModel model = new SentenceModel(inputStream);
            sentenceDetectorME = new SentenceDetectorME(model);
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    public SentenceStringDetector(InputStream modelStream){
        try {
            SentenceModel model = new SentenceModel(modelStream);
            sentenceDetectorME = new SentenceDetectorME(model);
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    public List<String> getSentences(String inputLine){
        return Arrays.asList(sentenceDetectorME.sentDetect(inputLine));
    }
}
