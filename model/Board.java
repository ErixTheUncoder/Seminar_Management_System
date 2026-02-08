package model;

/**
 * Board class - Represents a poster board for presentations
 */
public class Board {
    private String boardID;
    private String orientation; // Portrait or Landscape
    private String paperSize;    // A0, A1, etc.
    private String dimensions;   // example: "841 x 1189 mm"
    private String material;     // Foamcore, Cork, etc.
    private boolean isAvailable; // Track if board is assigned

    /**
     * Constructor for Board
     */
    public Board(String boardID, String orientation, String paperSize, String dimensions, String material) {
        this.boardID = boardID;
        this.orientation = orientation;
        this.paperSize = paperSize;
        this.dimensions = dimensions;
        this.material = material;
        this.isAvailable = true;
    }

    // Getters
    public String getBoardID() {
        return boardID;
    }

    public String getOrientation() {
        return orientation;
    }

    public String getPaperSize() {
        return paperSize;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getMaterial() {
        return material;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | %s | %s (%s) - %s", 
            boardID, paperSize, orientation, material, isAvailable ? "Available" : "Assigned");
    }
}
