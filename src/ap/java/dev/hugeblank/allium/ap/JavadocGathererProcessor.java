package dev.hugeblank.allium.ap;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class JavadocGathererProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var rootEl : roundEnv.getRootElements()) {
            if (!(rootEl instanceof TypeElement klass)) continue;

            ClassJavadoc json = new ClassJavadoc(new JsonObject());
            String klassDoc = processingEnv.getElementUtils().getDocComment(klass);

            if (klassDoc != null) json.setDoc(klassDoc);

            for (var enclosed : klass.getEnclosedElements()) {
                if (enclosed.getKind().isField()) {
                    String fieldDoc = processingEnv.getElementUtils().getDocComment(enclosed);

                    if (fieldDoc == null) continue;

                    json.setFieldDoc(enclosed.getSimpleName().toString(), fieldDoc);
                } else if (enclosed.getKind() == ElementKind.METHOD || enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                    var exec = (ExecutableElement) enclosed;
                    var name = exec.getSimpleName() + getDescriptor(exec);

                    String methodDoc = processingEnv.getElementUtils().getDocComment(exec);

                    if (methodDoc != null) json.method(name).setDoc(methodDoc);
                }
            }

            if (!json.representation().isEmpty()) {
                try {
                    var res = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "docs/" + ((TypeElement) rootEl).getQualifiedName() + ".json", rootEl);
                    try (var writer = res.openWriter(); var jsonWriter = new JsonWriter(writer)) {
                        jsonWriter.setIndent("    ");
                        Streams.write(json.representation(), jsonWriter);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return false;
    }

    private void writeInternalName(StringBuilder sb, TypeMirror type) {
        switch (type.getKind()) {
            case BOOLEAN -> sb.append("Z");
            case BYTE -> sb.append("B");
            case SHORT -> sb.append("S");
            case INT -> sb.append("I");
            case LONG -> sb.append("J");
            case CHAR -> sb.append("C");
            case FLOAT -> sb.append("F");
            case DOUBLE -> sb.append("D");
            case VOID -> sb.append("V");
            case ARRAY -> {
                sb.append("[");
                writeInternalName(sb, ((ArrayType) type).getComponentType());
            }
            case DECLARED -> {
                sb.append("L");
                sb.append(processingEnv.getElementUtils().getBinaryName((TypeElement) ((DeclaredType) type ).asElement())
                    .toString()
                    .replace('.', '/'));
                sb.append(";");
            }
            case TYPEVAR -> writeInternalName(sb, ((TypeVariable) type).getUpperBound());
            default -> throw new IllegalStateException("wtf is this type :sob: " + type);
        }
    }

    private String getDescriptor(ExecutableElement exec) {
        StringBuilder sb = new StringBuilder();

        sb.append("(");

        for (var param : exec.getParameters()) {
            writeInternalName(sb, param.asType());
        }

        sb.append(")");
        writeInternalName(sb, exec.getReturnType());

        return sb.toString();
    }
}
