package doom.enums;

public enum MediaType {
    PLAIN_TEXT("text/plain"),
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
