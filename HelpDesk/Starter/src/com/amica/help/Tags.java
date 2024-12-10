package com.amica.help;

import java.util.SortedSet;
import java.util.TreeSet;

public class Tags {
    private SortedSet<Tag> tags;

    public Tags(){
        this.tags = new TreeSet<>();
    }

    public Tag getTag(String keyword){
        Tag newTag = new Tag(keyword);
        for (Tag t : tags){
            if(t.equals(newTag)){
                return t;
            }
        }
        tags.add(newTag);
        return newTag;
    }
}
