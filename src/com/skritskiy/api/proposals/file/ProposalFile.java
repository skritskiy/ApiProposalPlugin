package com.skritskiy.api.proposals.file;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.skritskiy.api.proposals.ProposalLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ProposalFile extends PsiFileBase {
    public ProposalFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ProposalLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ProposalFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Proposal File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}
