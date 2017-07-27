package jp.co.rakuten.checkout.lite.model;

import java.lang.reflect.Field;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This abstract class is the base for all classes in jp.co.rakuten.checkout.lite.model package. Developers will not use this class directly.
 * 
 * @author rpayonline
 *
 */
public abstract class RpayLiteObject {

    public static final Gson PRETTY_PRINT_GSON = new GsonBuilder().setPrettyPrinting().serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    @Override
    public String toString() {
        return String.format("<%s@%s id=%s> JSON: %s", this.getClass().getName(), System.identityHashCode(this), getIdString(),
                PRETTY_PRINT_GSON.toJson(this));
    }

    public String toJson() {
        return PRETTY_PRINT_GSON.toJson(this);
    }

    private Object getIdString() {
        try {
            Field idField = this.getClass().getDeclaredField("id");
            return idField.get(this);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            return "";
        } 
    }

    protected static boolean equals(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

}
