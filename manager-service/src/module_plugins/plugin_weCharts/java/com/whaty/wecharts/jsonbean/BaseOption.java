package com.whaty.wecharts.jsonbean;

import java.io.Serializable;

public class BaseOption implements Serializable {

    private Grid grid;

    public BaseOption() {
    }

    public BaseOption(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
