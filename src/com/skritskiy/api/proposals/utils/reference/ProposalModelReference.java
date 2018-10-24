package com.skritskiy.api.proposals.utils.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.skritskiy.api.proposals.Icons;
import com.skritskiy.api.proposals.psi.ProposalMn;
import com.skritskiy.api.proposals.psi.ProposalModel;
import com.skritskiy.api.proposals.psi.ProposalNamedElement;
import com.skritskiy.api.proposals.psi.ProposalTypes;
import com.skritskiy.api.proposals.utils.Predefined;
import com.skritskiy.api.proposals.utils.ProposalUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProposalModelReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String key;

    private PsiElement element;

    public ProposalModelReference(@NotNull PsiElement element, TextRange rangeInElement) {
        super(element, rangeInElement);
        this.element = element;
        if (element instanceof ProposalMn) {
            if(element.getFirstChild() != element.getLastChild()) {
                this.key = element.getNode().findChildByType(ProposalTypes.MODEL_NAME).getText();
            } else {
                this.key = element.getText();
            }
        } else {
            this.key = element.getNode().findChildByType(ProposalTypes.MN).findChildByType(ProposalTypes.MODEL_NAME).getText();
        }
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<ProposalModel> properties = ProposalUtil.findModel(project, key);
        List<ResolveResult> results = new ArrayList<>();
        for (ProposalModel property : properties) {
            results.add(new PsiElementResolveResult(property));
        }
        return results.toArray(new ResolveResult[results.size()]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        Project project = myElement.getProject();
        List<ProposalModel> properties = ProposalUtil.findModels(project);
        List<LookupElement> variants = new ArrayList<>();
        for (final ProposalModel property : properties) {
            if (property.getName() != null && property.getName().length() > 0) {
                variants.add(LookupElementBuilder.create(property).
                        withIcon(Icons.FILE).
                        withTypeText(property.getContainingFile().getName())
                );
            }
        }
        variants.addAll(Predefined.STANDART_TYPES.stream().map(x -> LookupElementBuilder.create(x).withTypeText("Standart type")).collect(Collectors.toList()));
        return variants.toArray();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        if (element instanceof ProposalNamedElement) {
            ((ProposalNamedElement) element).setName(newElementName);
        }

        return element;
    }
}
