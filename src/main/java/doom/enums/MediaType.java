package doom.enums;

public enum MediaType {
    JSON("application/json"),
    FORM_URLENCODED("application/x-www-form-urlencoded"),
    FORM_DATA("multipart/form-data");

    String val;
    MediaType(String s){
        val = s;
    }

    public String getVal() {
        return val;
    }
}
