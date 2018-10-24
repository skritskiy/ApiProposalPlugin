package com.skritskiy.api.proposals;

import com.intellij.lexer.FlexAdapter;

public class ProposalLexerAdapter extends FlexAdapter {
    public ProposalLexerAdapter() {
        super(new ProposalLexer(null));
    }
}
