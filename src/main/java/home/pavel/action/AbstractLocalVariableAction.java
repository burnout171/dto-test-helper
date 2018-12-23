package home.pavel.action;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.util.IncorrectOperationException;
import home.pavel.utils.ClassUtils;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.isNull;

public abstract class AbstractLocalVariableAction extends PsiElementBaseIntentionAction {

    final ClassUtils classUtils = ClassUtils.getInstance();

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        PsiLocalVariable localVariable = PsiTreeUtil.getParentOfType(element, PsiLocalVariable.class);
        if (isNull(localVariable)) {
            return;
        }
        if (!(localVariable.getParent() instanceof PsiDeclarationStatement)) {
            return;
        }
        handle(localVariable, element, project);
    }

    public abstract void handle(PsiLocalVariable localVariable, PsiElement element, Project project);

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        PsiLocalVariable localVariable = PsiTreeUtil.getParentOfType(element, PsiLocalVariable.class);
        if (isNull(localVariable)) {
            return false;
        }
        if (!(localVariable.getParent() instanceof PsiDeclarationStatement)) {
            return false;
        }
        PsiClass clazz = PsiTypesUtil.getPsiClass(localVariable.getType());
        if (isNull(clazz)) {
            return false;
        }
        return true;
    }
}