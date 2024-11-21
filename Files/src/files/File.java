package files;

import java.text.SimpleDateFormat;
import java.util.Date;

public class File extends Node{
    private int size;
    private Date lastModified;
    private SimpleDateFormat readFormat = new SimpleDateFormat("MM/dd/yy");
    // Format given date in yyyy-MM-dd format
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


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

    public Date getLastModified() {
        return lastModified;
    }

    protected String getDetailsHTML() {
        return " last modified " + sdf.format(lastModified) + ")";
    }

    public String asHTML(){
        StringBuilder htmlString = new StringBuilder();
        htmlString.append(getNameHTML());
        htmlString.append("(").append(getSizeHTML());
        htmlString.append(getDetailsHTML());
        return htmlString.toString();
    }

    @Override
    public void add(Node c) {

    }

    protected String getNameHTML() {
        return "<strong>" + getName() + "</strong>";
    }

    protected String getSizeHTML() {
        return getSize() + " bytes";
    }

}
