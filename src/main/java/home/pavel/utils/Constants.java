package home.pavel.utils;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    //TODO move to file
    public static final Map<String, String> TYPE_GENERATED_MAP = new HashMap<String, String>() {
        {
            put("boolean", "false");
            put("java.lang.Boolean", "false");
            put("int", "0");
            put("byte", "(byte)0");
            put("java.lang.Byte", "(byte)0");
            put("java.lang.Integer", "0");
            put("java.lang.String", "");
            put("java.math.BigDecimal", "BigDecimal.ONE");
            put("java.lang.Long", "0L");
            put("long", "0L");
            put("short", "(short)0");
            put("java.lang.Short", "(short)0");
            put("java.util.Date", "new Date()");
            put("float", "0.0F");
            put("java.lang.Float", "0.0F");
            put("double", "0.0D");
            put("java.lang.Double", "0.0D");
            put("java.lang.Character", "\'c\'");
            put("char", "\'c\'");
            put("java.time.LocalDateTime", "LocalDateTime.now()");
            put("java.time.LocalDate", "LocalDate.now()");

        }
    };

    private Constants() {
    }
}
