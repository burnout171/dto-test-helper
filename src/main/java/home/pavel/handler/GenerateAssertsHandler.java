package home.pavel.handler;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTypesUtil;
import home.pavel.generator.MethodBasedGenerator;
import home.pavel.utils.ClassUtils;

import java.util.List;
import java.util.function.BiFunction;

import static home.pavel.utils.DocumentUtils.calculateShift;
import static home.pavel.utils.DocumentUtils.commitAndSaveDocument;

public class GenerateAssertsHandler {

    private final ClassUtils classUtils = ClassUtils.getInstance();
    private final MethodBasedGenerator methodBasedGenerator = MethodBasedGenerator.getInstance();

    public void handle(PsiLocalVariable localVariable, PsiElement element, Project project) {
        PsiClass clazz = PsiTypesUtil.getPsiClass(localVariable.getType());
        List<PsiMethod> methodList = classUtils.getListOfGetMethods(clazz);
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        Document document = psiDocumentManager.getDocument(element.getContainingFile());
        String shift = calculateShift(document, element.getTextOffset());
        String buildString = methodBasedGenerator.generateCode(localVariable.getName(), methodList, shift, getActualGenerator());
        document.insertString(localVariable.getParent().getTextOffset() + localVariable.getParent().getText().length(), buildString);
        commitAndSaveDocument(psiDocumentManager, document);
    }

    private BiFunction<String, PsiMethod, String> getActualGenerator() {
        return (generateName, method) -> {
            if (0 != method.getParameterList().getParametersCount()) {
                return "";
            }
            String returnType = method.getReturnType().getCanonicalText();
            String defaultValue = methodBasedGenerator.defaultParameterResolver(method, returnType);
            StringBuilder methodCall = new StringBuilder().append(generateName).append(".").append(method.getName()).append("()");
            return "assertEquals(" + defaultValue + ", " + methodCall + ");";
        };
    }
}
