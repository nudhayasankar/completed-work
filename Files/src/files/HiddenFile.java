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
    public String asHTML() {
        StringBuilder html = new StringBuilder();
        // Grey font, bold format name
        html.append("<span style=\"color : grey;\"><strong>");
        html.append(getName());
        html.append("</strong> (");
        // File size and last modified
        html.append(size);
        html.append(" bytes,last modified ");
        html.append(sdf.format(lastModified));
        html.append(")<em>[hidden]</em>");
        return html.toString();
    }

    @Override
    public void add(Node child) {

    }
}
