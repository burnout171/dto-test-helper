package home.pavel.utils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public final class ClassUtils {

    private static final ClassUtils INSTANCE = new ClassUtils();

    private ClassUtils() {
    }

    public static ClassUtils getInstance() {
        return INSTANCE;
    }

    public boolean isTerminatedClass(PsiClass psiClass) {
        if (isNull(psiClass)) {
            return true;
        }
        String qualifiedName = psiClass.getQualifiedName();
        return nonNull(qualifiedName) && qualifiedName.startsWith("java");
    }

    public List<PsiMethod> getListOfSetMethods(PsiClass clazz) {
        return getListOfMethods(clazz, this::isValidSetMethod);
    }

    public boolean isValidSetMethod(PsiMethod m) {
        return m.hasModifierProperty("public") && !m.hasModifierProperty("static") && m.getName().startsWith("set");
    }

    public List<PsiMethod> getListOfGetMethods(PsiClass clazz) {
        return getListOfMethods(clazz, this::isValidGetMethod);
    }

    public boolean isValidGetMethod(PsiMethod m) {
        return m.hasModifierProperty("public") && !m.hasModifierProperty("static") && m.getName().startsWith("get");
    }

    private List<PsiMethod> getListOfMethods(PsiClass clazz, Predicate<PsiMethod> predicate) {
        List<PsiMethod> allMethods = new ArrayList<>();
        while (!isTerminatedClass(clazz)) {
            List<PsiMethod> methodList = Arrays.stream(clazz.getMethods())
                    .filter(predicate)
                    .collect(Collectors.toList());
            allMethods.addAll(methodList);
            clazz = clazz.getSuperClass();
        }
        return allMethods;
    }
}
