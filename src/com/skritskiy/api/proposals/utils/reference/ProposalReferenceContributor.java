package com.skritskiy.api.proposals.utils.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.skritskiy.api.proposals.psi.ProposalMn;
import com.skritskiy.api.proposals.psi.ProposalTypes;
import org.jetbrains.annotations.NotNull;


public class ProposalReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(ProposalTypes.MN),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                                 @NotNull ProcessingContext
                                                                         context) {
                        if (element instanceof ProposalMn) {
                            String value = element.getText();
                            if (value != null) {
                                return new PsiReference[]{
                                        new ProposalModelReference(element, new TextRange(0, value.length() + 1))};
                            }
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}
