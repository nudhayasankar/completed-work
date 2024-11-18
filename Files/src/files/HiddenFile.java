package files;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HiddenFile extends Node{
    private int size;
    private Date lastModified;

    private SimpleDateFormat readFormat = new SimpleDateFormat("MM/dd/yy");
    // Format given date in yyyy-MM-dd format
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public HiddenFile(String name, int size, String lastModified) {
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
        return "<span style=\"color : grey;\">";
    }

    @Override
    protected String getDetailsHTML() {
        return "last modified " + sdf.format(lastModified) + ")<em>[hidden]</em>";
    }

    @Override
    protected String getFooterHTML() {
        return "</span>";
    }
}
