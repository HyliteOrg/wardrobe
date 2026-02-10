package dev.hardaway.wardrobe.api.property.validator;

import com.hypixel.hytale.codec.schema.SchemaContext;
import com.hypixel.hytale.codec.schema.config.Schema;
import com.hypixel.hytale.codec.validation.ValidationResults;
import com.hypixel.hytale.codec.validation.Validator;
import dev.hardaway.wardrobe.api.property.WardrobeProperties;

import javax.annotation.Nonnull;

@Deprecated(forRemoval = true)
public class PropertiesIconValidator implements Validator<WardrobeProperties> {
    public static final PropertiesIconValidator INSTANCE = new PropertiesIconValidator();

    private PropertiesIconValidator() {
    }

    public void accept(@Nonnull WardrobeProperties properties, @Nonnull ValidationResults results) {
        if (properties.getIcon() == null) {
            results.fail("Properties must include an icon");
        }
    }

    @Override
    public void updateSchema(SchemaContext schemaContext, Schema schema) {
    }
}
