package in.co.ashclan.ashclanbottomplayer.Exception;


public class AudioUrlInvalidException extends IllegalStateException {
    public AudioUrlInvalidException(String url) {
        super("The url does not appear valid: " + url);
    }
}
