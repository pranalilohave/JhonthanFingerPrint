package in.co.ashclan.ashclanbottomplayer.Exception;

public class AudioListNullPointerException extends NullPointerException {
    public AudioListNullPointerException() {
        super("The playlist is empty or null");
    }
}
