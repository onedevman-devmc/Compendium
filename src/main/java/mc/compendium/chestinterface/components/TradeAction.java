package mc.compendium.chestinterface.components;

public enum TradeAction {
    UNKNOWN(false, false),
    SINGLE_TRADE(true, false),
    MULTIPLE_TRADE(false, true);

    //

    private final boolean isResultInCursor;
    private final boolean isResultInInventory;

    //

    TradeAction(boolean isResultInCursor, boolean isResultInInventory) {
        this.isResultInCursor = isResultInCursor;
        this.isResultInInventory = isResultInInventory;
    }

    //

    public boolean isResultInCursor() { return this.isResultInCursor; }

    public boolean isResultInInventory() { return this.isResultInInventory; }

}