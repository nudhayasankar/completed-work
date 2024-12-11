package com.amica.help;

import java.util.*;

public class Tags {
    private SortedSet<Tag> tags;
    private Map<String, Tag> synonymMap = new HashMap<>();

    public Tags(){
        tags = new TreeSet<>();
    }

    public List<Tag> getTags() {
      return new ArrayList<>(tags);
    }

    public Tag getTag(String keyword){
        if(synonymMap.containsKey(keyword.toLowerCase())){
            return synonymMap.get(keyword.toLowerCase());
        }
        Tag newTag = new Tag(keyword);
        for (Tag t : tags){
            if(t.equals(newTag)){
                return t;
            }
        }
        tags.add(newTag);
        return newTag;
    }

    public void addSynonym(String synonym, Tag tag){
        synonymMap.put(synonym.toLowerCase(), tag);
    }

}
