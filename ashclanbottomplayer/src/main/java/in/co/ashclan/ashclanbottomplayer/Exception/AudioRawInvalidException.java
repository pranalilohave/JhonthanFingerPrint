package in.co.ashclan.ashclanbottomplayer.Exception;

/**
 * Invalid raw resource file id exception.
 */
public class AudioRawInvalidException extends Exception {
    public AudioRawInvalidException(String rawId) {
        super("Not a valid raw file id: " + rawId);
    }
}
