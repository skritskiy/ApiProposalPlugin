package com.skritskiy.api.proposals.psi;

import com.intellij.psi.tree.IElementType;
import com.skritskiy.api.proposals.ProposalLanguage;
import org.jetbrains.annotations.NotNull;

public class ProposalElementType extends IElementType {
    public ProposalElementType(@NotNull String debugName) {
        super(debugName, ProposalLanguage.INSTANCE);
    }
}
