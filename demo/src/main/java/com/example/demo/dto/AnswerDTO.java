package com.example.demo.dto;

public class AnswerDTO {
    public Long questionId;
    public Long optionId;    // Puede venir vacío
    public String text;      // Puede venir vacío

    // Necesitamos getters y setters para que Spring lea el JSON
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
