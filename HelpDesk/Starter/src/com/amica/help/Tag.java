package com.amica.help;

public class Tag implements Comparable<Tag> {
    private String value;

    public Tag(String value){
        this.value = value.toLowerCase();
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Tag)) return false;

        Tag t = (Tag) o;

        return this.compareTo(t) == 0;
    }

    @Override
    public int compareTo(Tag t){
        return this.value.compareToIgnoreCase(t.value);
    }
}
