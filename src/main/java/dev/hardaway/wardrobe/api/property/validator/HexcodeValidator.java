package dev.hardaway.wardrobe.api.property.validator;

import com.hypixel.hytale.codec.schema.SchemaContext;
import com.hypixel.hytale.codec.schema.config.Schema;
import com.hypixel.hytale.codec.schema.config.StringSchema;
import com.hypixel.hytale.codec.validation.ValidationResults;
import com.hypixel.hytale.codec.validation.Validator;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class HexcodeValidator implements Validator<String> {
    public static final HexcodeValidator INSTANCE = new HexcodeValidator();
    private static final Pattern HEXCODE_PATTERN = Pattern.compile("^#.*");
    private HexcodeValidator() {
    }

    public void accept(@Nonnull String string, @Nonnull ValidationResults results) {
        if (string.charAt(0) != '#') {
            results.fail("String must start with '#'");
        }

        if (string.length() != 7) {
            results.fail("String must be 7 characters");
        }
    }

    public void updateSchema(SchemaContext context, Schema schema) {
        StringSchema stringSchema = (StringSchema) schema;
        stringSchema.setMinLength(7);
        stringSchema.setPattern(HEXCODE_PATTERN);
    }
}

