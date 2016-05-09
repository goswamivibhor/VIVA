package com.govibs.viva.ai.nlp.bean;

import java.io.Serializable;

/**
 * The AI Response for VIVA.
 * Created by Vibhor on 3/20/16.
 */
public class AI_Response_Viva implements Serializable {

    private String sentiment;
    private String language;
    private String taxonomy;
    private String chatResponse;
    private String question;
    private String category;
    private String concept;
    private boolean init;

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getChatResponse() {
        return chatResponse;
    }

    public void setChatResponse(String chatResponse) {
        this.chatResponse = chatResponse;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}
