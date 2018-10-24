package com.skritskiy.api.proposals.psi;

import com.intellij.psi.tree.IElementType;
import com.skritskiy.api.proposals.ProposalLanguage;
import org.jetbrains.annotations.NotNull;

public class ProposalTokenType extends IElementType {
    public ProposalTokenType(@NotNull String debugName) {
        super(debugName, ProposalLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "ProposalTokenType." + super.toString();
    }
}
