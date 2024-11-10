package com.hytejasvi.taskManagementApp.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuotesApiResponse {

        @JsonProperty("quote")
        private String quote;

        @JsonProperty("author")
        private String author;
}
