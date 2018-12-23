package home.pavel.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import home.pavel.handler.GenerateAssertsHandler;
import org.jetbrains.annotations.NotNull;

public class GenerateAssertsAction extends AbstractLocalVariableAction {

    private static final String PSI_ELEMENT_BASE_INTENTION_ACTION_NAME = "Generate asserts for Dto fields";

    private final GenerateAssertsHandler generateAssertsHandler = new GenerateAssertsHandler();

    @Override
    public void handle(PsiLocalVariable localVariable, PsiElement element, Project project) {
        generateAssertsHandler.handle(localVariable, element, project);
    }

    @Override
    public String getFamilyName() {
        return PSI_ELEMENT_BASE_INTENTION_ACTION_NAME;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        if (!editor.getDocument().getText().contains("org.junit")) {
            return false;
        }
        if (!super.isAvailable(project, editor, element)) {
            return false;
        }
        PsiLocalVariable localVariable = PsiTreeUtil.getParentOfType(element, PsiLocalVariable.class);
        PsiClass clazz = PsiTypesUtil.getPsiClass(localVariable.getType());
        return hasValidGetMethod(clazz);
    }

    @NotNull
    @Override
    public String getText() {
        return PSI_ELEMENT_BASE_INTENTION_ACTION_NAME;
    }

    private boolean hasValidGetMethod(PsiClass psiClass) {
        while (!classUtils.isTerminatedClass(psiClass)) {
            for (PsiMethod m : psiClass.getMethods()) {
                if (classUtils.isValidGetMethod(m)) {
                    return true;
                }
            }
            psiClass = psiClass.getSuperClass();
        }
        return false;
    }
}
