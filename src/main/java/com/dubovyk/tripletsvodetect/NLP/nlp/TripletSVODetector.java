package com.dubovyk.tripletsvodetect.NLP.nlp;

import com.dubovyk.tripletsvodetect.NLP.config.consts;
import com.dubovyk.tripletsvodetect.NLP.model.TripletSVO;
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class used to detect to detect tuples of Subject-Verb-Object. In order
 * to do it, class uses a lib by Washington University based on OpenNLP and OpenNLP itself.
 *
 * Now, it is not very good in parsing very complex sentences as it will require more detailed
 * analysis of syntax tree which requires more complex algorithms.
 *
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class TripletSVODetector {
    private OpenNlpSentenceChunker chunker;
    private SentenceStringDetector detector;
    private ReVerbExtractor extractor;
    private POSModel model;
    private POSTaggerME tagger;

    public TripletSVODetector(){
        try {
            model = new POSModel(this.getClass().getResourceAsStream(consts.EN_POS_MAXENT));
            tagger = new POSTaggerME(model);
            chunker = new OpenNlpSentenceChunker();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        detector = new SentenceStringDetector(this.getClass().getResourceAsStream(consts.EN_SENT));
        extractor = new ReVerbExtractor();
    }


    /**
     * This method is a main entry point in the class` interface. Just create a class instance
     * and use the method as following: "List<TripletSVO> result = detector.getRelationsFromText(test)".
     *
     * @param textInput This argument is a text input.
     * @return Returns a list of parsed triplets (SubjectVerbObject). Can work with complex sentences as well.
     */
    public List<TripletSVO> getRelationsFromText(String textInput){
        List<TripletSVO> relations = new ArrayList<>();
        for(String sentence : detector.getSentences(textInput)){
            relations.addAll(getRelationsFromSentence(sentence));
        }
        return relations;
    }

    public List<TripletSVO> getRelationsFromSentence(String sentence){
        List<TripletSVO> relations = new ArrayList<>();
        ChunkedSentence chunkedSentence = chunker.chunkSentence(sentence);
        ConfidenceFunction confidenceFunction = null;
        try {
            for (ChunkedBinaryExtraction extraction : extractor.extract(chunkedSentence)){
                TripletSVO svo = new TripletSVO();

                svo.setSubject(extraction.getArgument1().getText());
                svo.setObject(extraction.getArgument2().getText());

                /*
                * Here we scan the "relation" and "object" parts of the parsing
                * results to find and remove extra information (which is for example adjectives).
                * To avoid doing this, it is required to build more complex tree and analyze it.
                * */
                String object[] = extraction.getArgument2().getText().split(" ");
                String tags[] = tagger.tag(object);
                for(int i = 0;  i < object.length; i++){
                    if (!(tags[i].equals("NN") || tags[i].equals("NNS") || tags[i].equals("NNP") || tags[i].equals("NNPS") || tags[i].equals("PRP") || tags[i].equals("IN") || tags[i].equals("VBG"))){
                        object[i] = null;
                    }

                }

                svo.setObject(joinAndDropNulls(object));

                String relation[] = extraction.getRelation().getText().split(" ");
                tags = tagger.tag(relation);

                for(int i = 0;  i < relation.length; i++){
                    if (tags[i].equals("JJ") || tags[i].equals("RB") || tags[i].equals("JJS") || tags[i].equals("IN") || tags[i].equals("DT") || tags[i].equals("DT") || tags[i].equals("PRP$")){
                        relation[i] = null;
                    }

                    if (tags[i].equals("NN")){
                        svo.setObject(relation[i]);
                        relation[i] = null;
                    }
                }

                svo.setRelation(joinAndDropNulls(relation));
                svo.setRelation(joinAndDropNulls(relation));

                relations.add(svo);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return relations;
    }


    /**
     * @param stringArray
     * @return Joined stringArray, but if some elements of array are "" or nulls, no extra delimiter will be added.
     */
    private String joinAndDropNulls(String[] stringArray){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < stringArray.length; i++){
            if(stringArray[i] != null && !stringArray[i].equals("")){
                builder.append(stringArray[i]);
                if (i < stringArray.length - 1){
                    builder.append(" ");
                }
            }
        }
        return builder.toString().trim();
    }
}
