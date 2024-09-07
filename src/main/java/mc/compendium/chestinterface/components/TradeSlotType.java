package mc.compendium.chestinterface.components;

public enum TradeSlotType {
    UNKNOWN(-1),
    FIRST_INGREDIENT(0),
    SECOND_INGREDIENT(1),
    RESULT(2);

    //

    private static final TradeSlotType[] ALL = TradeSlotType.values();

    //

    public static TradeSlotType getBySlot(int slot) {
        TradeSlotType result = null;

        for(int i = 0; i < ALL.length && result == null; i++) {
            TradeSlotType type = ALL[i];
            if(type.getSlot() == slot) result = type;
        }

        return result == null ? UNKNOWN : result;
    }

    //

    private final int slot;

    //

    TradeSlotType(int slot) {
        this.slot = slot;
    }

    //

    public int getSlot() { return this.slot; }

}
