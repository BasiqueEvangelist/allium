package dev.hugeblank.allium.ap;

import com.google.gson.JsonObject;

public record ClassJavadoc(JsonObject representation) {
    public void setDoc(String doc) {
        representation.addProperty("doc", doc);
    }

    public void setFieldDoc(String fieldName, String doc) {
        if (!representation.has("fields")) {
            representation.add("fields", new JsonObject());
        }

        representation.getAsJsonObject("fields").addProperty(fieldName, doc);
    }

    public Method method(String methodName) {
        if (!representation.has("methods")) {
            representation.add("methods", new JsonObject());
        }

        var methods = representation.getAsJsonObject("methods");

        if (!methods.has(methodName)) {
            methods.add(methodName, new JsonObject());
        }

        return new Method(methods.getAsJsonObject(methodName));
    }

    public record Method(JsonObject representation) {
        public void setDoc(String doc) {
            representation.addProperty("doc", doc);
        }
    }
}
