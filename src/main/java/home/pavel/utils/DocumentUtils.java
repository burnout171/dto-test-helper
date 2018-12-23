package home.pavel.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;

import static java.util.Objects.isNull;

public final class DocumentUtils {

    private DocumentUtils() {
    }

    public static void commitAndSaveDocument(PsiDocumentManager psiDocumentManager, Document document) {
        if (isNull(document)) {
            return;
        }
        psiDocumentManager.doPostponedOperationsAndUnblockDocument(document);
        psiDocumentManager.commitDocument(document);
        FileDocumentManager.getInstance().saveDocument(document);
    }

    public static String calculateShift(Document document, int statementOffset) {
        StringBuilder splitText = new StringBuilder();
        int cur = statementOffset;
        String text = document.getText(new TextRange(cur - 1, cur));
        while (text.equals(" ") || text.equals("\t")) {
            splitText.insert(0, text);
            cur--;
            if (cur < 1) {
                break;
            }
            text = document.getText(new TextRange(cur - 1, cur));
        }
        splitText.insert(0, "\n");
        return splitText.toString();
    }
}
