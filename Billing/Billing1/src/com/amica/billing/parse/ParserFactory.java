package com.amica.billing.parse;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParserFactory {
    static final String CSV_FILE = ".csv";
    static final String FLATFILE = ".flat";

    public Parser createParser(String fileName) {
        if(fileName.contains(CSV_FILE)) {
            return new CSVParser();
        }
        if(fileName.contains(FLATFILE)) {
            return new FlatParser();
        }
        return null;
    }
}
