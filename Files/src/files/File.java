package files;

import java.text.SimpleDateFormat;
import java.util.Date;

public class File extends Node{
    private int size;

    public File(String name, int size, String lastModified) {
        super(name);
        this.size = size;
        try{
            this.lastModified = readFormat.parse(lastModified);
        } catch(Exception e){
            System.out.println("Cannot parse date" + e.getMessage());
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void add(Node child) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    protected String getHeaderHTML() {
        return "";
    }

    @Override
    protected String getDetailsHTML() {
        return " last modified " + sdf.format(lastModified) + ")";
    }

    @Override
    protected String getFooterHTML() {
        return "";
    }
}
