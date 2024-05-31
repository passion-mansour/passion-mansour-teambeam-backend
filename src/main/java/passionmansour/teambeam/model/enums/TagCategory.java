package passionmansour.teambeam.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TagCategory {
    post("post"),
    schedule("schedule"),
    todo("todo");

    private final String value;

    TagCategory(String value) {
        this.value = value;
    }

    @JsonValue // 전체를 대표할 하나의 메서드를 지정
    public String getValue() {
        return value;
    }

    public static TagCategory fromString(String value) {
        for (TagCategory category : TagCategory.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid TagCategory value: " + value);
    }
}