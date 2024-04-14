package ru.itmo.lab5.data.models;

import java.io.Serializable;

/**
 * Перечисление, представляющее типы билетов.
 */

public enum TicketType implements Serializable {
    VIP,
    USUAL,
    CHEAP;
    /**
     * Возвращает строку, содержащую все значения перечисления, разделенные запятыми.
     *
     * @return Строка со всеми типами билетов.
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var ticketType : values()) {
            nameList.append(ticketType.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
