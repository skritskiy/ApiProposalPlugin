package com.skritskiy.api.proposals;

import com.intellij.lang.Language;

public class ProposalLanguage extends Language {
    public static final ProposalLanguage INSTANCE = new ProposalLanguage();
    private ProposalLanguage() {
        super("Proposal");
    }
}
