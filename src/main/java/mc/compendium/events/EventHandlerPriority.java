package mc.compendium.events;

public enum EventHandlerPriority {
    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    HIGHEST;

    //

    public static final EventHandlerPriority[] DECREASING_ORDER = { HIGHEST, HIGH, NORMAL, LOW, LOWEST };

}
