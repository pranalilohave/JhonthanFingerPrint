package in.co.ashclan.model;

import java.io.Serializable;

public class ItemData implements Serializable {
    String text;
    Integer imageId;

    public ItemData(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText(){
        return text;
    }

    public Integer getImageId(){
        return imageId;
    }
}
