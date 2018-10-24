package com.skritskiy.api.proposals.file;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.skritskiy.api.proposals.Icons;
import com.skritskiy.api.proposals.ProposalLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProposalFileType extends LanguageFileType {
    public static final ProposalFileType INSTANCE = new ProposalFileType();

    private ProposalFileType() {
        super(ProposalLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Proposal file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "API proposal file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "ap";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.FILE;
    }
}
