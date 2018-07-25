package in.co.ashclan.ashclanbottomplayer.Exception;

/**
 * Invalid assets file exception.
 */

public class AudioAssetsInvalidException extends Exception {
    public AudioAssetsInvalidException(String path) {
        super("The file name is not a valid Assets file: " + path);
    }
}
