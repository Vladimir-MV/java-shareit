package ru.practicum.shareit.booking;


public enum State {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static State from (String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return state;
            }
        }
        return null;
    }
}
