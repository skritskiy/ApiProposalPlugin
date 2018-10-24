package com.skritskiy.api.proposals.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.skritskiy.api.proposals.file.ProposalFile;
import com.skritskiy.api.proposals.file.ProposalFileType;

public class ProposalElementFactory {
    public static ProposalMn createModelName(Project project, String name) {
        final ProposalFile file = createFileWithModelName(project, name);
        ASTNode childByType = file.getFirstChild().getNode().findChildByType(ProposalTypes.MN);
        return (ProposalMn) childByType.getPsi();
    }

    public static ProposalFile createFileWithModelName(Project project, String text) {
        String name = "dummy.simple";
        return (ProposalFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, ProposalFileType.INSTANCE, dummyModel.replaceFirst("\\{HOLDER}", text));
    }

    private static final String dummyModel = "model {HOLDER} {\n" +
            "    dummy: DummyModel\n" +
            "}";
}
