package dev.hardaway.wardrobe.api.property.validator;

import com.hypixel.hytale.codec.schema.SchemaContext;
import com.hypixel.hytale.codec.schema.config.Schema;
import com.hypixel.hytale.codec.schema.config.StringSchema;
import com.hypixel.hytale.codec.validation.ValidationResults;
import com.hypixel.hytale.codec.validation.Validator;
import com.hypixel.hytale.server.core.asset.util.ColorParseUtil;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class HexcodeValidator implements Validator<String> {
    public static final HexcodeValidator INSTANCE = new HexcodeValidator();
    private static final Pattern HEXCODE_PATTERN = Pattern.compile("^#.*");
    private HexcodeValidator() {
    }

    public void accept(@Nonnull String string, @Nonnull ValidationResults results) {
        if (string.length() != 7) {
            results.fail("Hex color must be 7 characters");
        }

        try {
            ColorParseUtil.hexStringToColor(string);
        } catch (IllegalArgumentException e) {
            results.fail(e.getMessage());
        }

        if (results.hasFailed())
            results.fail("got: '" + string + "' expected: #RRGGBB");
    }

    public void updateSchema(SchemaContext context, Schema schema) {
        StringSchema stringSchema = (StringSchema) schema;
        stringSchema.setMinLength(7);
        stringSchema.setPattern(HEXCODE_PATTERN);
    }
}

