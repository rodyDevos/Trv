
package com.igames2go.t4f.data;

import java.io.Serializable;

public class EndOfTurn implements Serializable {

    private EndOfTurnDataObject endOfTurn = new EndOfTurnDataObject();

    public EndOfTurnDataObject getEndOfTurnData() {
        return endOfTurn;
    }

    @Override
    public String toString() {
        return "" + endOfTurn.toString();
    }

}
