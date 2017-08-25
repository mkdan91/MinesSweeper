package com.example.mkdan.minessweeper.Logic;

public class Board {
    private int mMatrixSize;
    private Tile mTiles[][];
    private int mNumOfMines;
    private int mNumOfFlags = 0;
    private int mNumOfUnpressedTiles;

    public Board(int mMatrixSize, int mNumOfMines) {
        this.mMatrixSize = mMatrixSize;
        this.mNumOfMines = mNumOfMines;
        mTiles = new Tile[mMatrixSize][mMatrixSize];
        mNumOfUnpressedTiles = mMatrixSize * mMatrixSize;
        startMatrix();
    }

    void pressTile(int row, int col) {
        mTiles[row][col].press();
        mNumOfUnpressedTiles--;
    }

    public void addFlag() {
        mNumOfFlags++;
    }

    public void removeFlag() {
        mNumOfFlags--;
    }

    public int getNumOfFlags() {
        return mNumOfFlags;
    }

    public int getNumOfMines() {
        return mNumOfMines;
    }

    public int getRowColSize() {
        return mMatrixSize;
    }

    public int getNumOfUnpressedTiles() {
        return mNumOfUnpressedTiles;
    }

    public Tile getTile(int row, int col) {
        return mTiles[row][col];
    }

    // a function that gives the tile by a position and not by row and col
    public Tile getTileByPosition(int position){
        return mTiles[position/mMatrixSize][position%mMatrixSize];
    }

    // checking if the tile we are looking at exists //
    public boolean checkTileExists(int row, int col) {
        if (row >= 0 && row < mTiles.length && col >= 0 && col < mTiles[0].length && mTiles[row][col] != null) {
            return true;
        }
        return false;
    }

    // putting mines on the matrix //
    private void startMatrix() {

        for (int i = 0; i < mNumOfMines; i++) {
            int row = (int) (Math.random() * mMatrixSize);
            int col = (int) (Math.random() * mMatrixSize);

            if (mTiles[row][col] == null || !mTiles[row][col].isMine()) {
                mTiles[row][col] = new Tile();
            } else {
                i--;
                continue;
            }
        }
        int counter = 0;

        // putting numbers on tiles that are near mines //
        for (int i = 0; i < mTiles.length; i++) {
            for (int j = 0; j < mTiles[0].length; j++) {
                if (mTiles[i][j] != null && mTiles[i][j].isMine()) {
                    continue;
                }
                for (int k = i - 1; k <= i + 1; k++) {
                    for (int h = j - 1; h <= j + 1; h++) {
                        if (k >= 0 && h >= 0 && k < mTiles.length && h < mTiles.length && mTiles[k][h] != null && mTiles[k][h].isMine()) {
                            counter++;
                        }
                    }
                }
                mTiles[i][j] = new Tile(counter);
                counter = 0;
            }
        }
    }

}