package home.pavel.generator;

import com.intellij.psi.PsiMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.BiFunction;

import static home.pavel.utils.Constants.TYPE_GENERATED_MAP;

public class MethodBasedGenerator {

    private static final MethodBasedGenerator INSTANCE = new MethodBasedGenerator();

    private MethodBasedGenerator() {
    }

    public static MethodBasedGenerator getInstance() {
        return INSTANCE;
    }

    public String generateCode(String fieldName, List<PsiMethod> methodList, String shift, BiFunction<String, PsiMethod, String> actualGenerator) {
        StringBuilder builder = new StringBuilder();
        builder.append(shift);
        for (PsiMethod method : methodList) {
            String result = actualGenerator.apply(fieldName, method);
            builder.append(result).append(shift);
        }
        return builder.toString();
    }

    public String defaultParameterResolver(PsiMethod method, String type) {
        String defaultValue = TYPE_GENERATED_MAP.get(type);
        if ("".equals(defaultValue)) {
            return "\"" + StringUtils.uncapitalize(method.getName().substring(3)) + "\"";
        }
        return defaultValue;
    }

}
