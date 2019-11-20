package com.ehmaugbogo.marketlister.views.main.ui.list;

/*
This class is almost unnecessary, I just added it for expression sake
*/

public enum PriorityState {
    NONE(0),
    URGENT(1),
    IMPORTANT(2);

    private int state;

    private PriorityState(int state){
        this.state=state;
    }

    public int getStateValue() {
        return state;
    }
}
