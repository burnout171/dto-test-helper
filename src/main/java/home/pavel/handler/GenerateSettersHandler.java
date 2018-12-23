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

public class GenerateSettersHandler {

    private final ClassUtils classUtils = ClassUtils.getInstance();
    private final MethodBasedGenerator methodBasedGenerator = MethodBasedGenerator.getInstance();

    public void handle(PsiLocalVariable localVariable, PsiElement element, Project project) {
        PsiClass clazz = PsiTypesUtil.getPsiClass(localVariable.getType());
        List<PsiMethod> methodList = classUtils.getListOfSetMethods(clazz);
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        Document document = psiDocumentManager.getDocument(element.getContainingFile());
        String shift = calculateShift(document, element.getTextOffset());
        String buildString = methodBasedGenerator.generateCode(localVariable.getName(), methodList, shift, getActualGenerator());
        document.insertString(localVariable.getParent().getTextOffset() + localVariable.getParent().getText().length(), buildString);
        commitAndSaveDocument(psiDocumentManager, document);
    }

    private BiFunction<String, PsiMethod, String> getActualGenerator() {
        return (generateName, method) -> {
            PsiParameter[] parameters = method.getParameterList().getParameters();
            if (parameters.length > 1) {
                return "";
            }
            PsiParameter parameter = parameters[0];
            String classType = parameter.getType().getCanonicalText();
            String defaultValue = methodBasedGenerator.defaultParameterResolver(method, classType);
            return generateName + "." + method.getName() + "(" + defaultValue + ");";
        };
    }
}
