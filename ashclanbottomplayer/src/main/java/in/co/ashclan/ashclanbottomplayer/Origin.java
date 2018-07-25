package in.co.ashclan.ashclanbottomplayer;

/**
 * This is the origin of Audio file.
 */
public enum Origin {
  URL, // url like http:/www.example.com/sample.mp3
  RAW, // From raw resource folder
  ASSETS, // From asset folder
  FILE_PATH // From file in device path
}
