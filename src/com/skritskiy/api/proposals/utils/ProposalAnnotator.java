package com.skritskiy.api.proposals.utils;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.skritskiy.api.proposals.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProposalAnnotator implements Annotator {
    private static final List<IElementType> NavigationElementTypes = Collections.singletonList(
            ProposalTypes.MODEL_NAME
    );

    private static final List<IElementType> unresolvedKeywordTypes = Collections.singletonList(ProposalTypes.UKW);

    private static final List<IElementType> unresolvedMethodTypes = Collections.singletonList(ProposalTypes.UM);

    private static final List<IElementType> unresolvedEndpointPropertyTypes =
            Arrays.asList(ProposalTypes.UREQKW, ProposalTypes.URESPKW, ProposalTypes.UPARKW);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {

        if (element instanceof LeafPsiElement && NavigationElementTypes.contains(((LeafPsiElement) element).getElementType())) {
            String value = element.getText();
            if (value != null) {
                Project project = element.getProject();
                List<ProposalModel> models = ProposalUtil.findModel(project, value);
                if (models.size() == 0 && !Predefined.STANDART_TYPES.contains(value)) {
                    TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                            element.getTextRange().getEndOffset());
                    holder.createErrorAnnotation(range, "Cannot find type definition");
                } else if (models.size() > 1) {
                    if (element.getParent().getParent().getFirstChild() != element.getParent().getParent().getLastChild()) {

                        TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                                element.getTextRange().getEndOffset());
                        holder.createErrorAnnotation(range, "Model with this name already exists ");
                    }
                }
            }
        }

        if (element instanceof ProposalModeldef && element.getParent() instanceof ProposalModel) {
            ProposalModel parent = (ProposalModel) element.getParent();
            String text = element.getNode().findChildByType(ProposalTypes.MODEL_PROPERTY_KEY).getText();

            List<ProposalModeldef> modeldefList = parent.getModeldefList();
            if (modeldefList.stream().anyMatch(x -> x != element && x.getNode().findChildByType(ProposalTypes.MODEL_PROPERTY_KEY).getText().equals(text))) {
                TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                        element.getTextRange().getEndOffset());
                holder.createErrorAnnotation(range, "Duplicate property definition in model");
            }
        }

        if (element instanceof ProposalModeldef && element.getParent() instanceof ProposalGetparams) {
            validateDuplicateParams(element, holder);
            validateInvalidParams(element, holder);
        }

        if (element instanceof LeafPsiElement && unresolvedKeywordTypes.contains(((LeafPsiElement) element).getElementType())) {
            String value = element.getText();
            if (value != null) {
                TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                        element.getTextRange().getEndOffset());
                holder.createErrorAnnotation(range, "Unresolved keyword");
            }
        }

        if (element instanceof LeafPsiElement && unresolvedEndpointPropertyTypes.contains(((LeafPsiElement) element).getElementType())) {
            String value = element.getText();
            if (value != null) {
                TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                        element.getTextRange().getEndOffset());
                holder.createErrorAnnotation(range, "Unresolved endpoint property");
            }
        }

        if (element instanceof LeafPsiElement && unresolvedMethodTypes.contains(((LeafPsiElement) element).getElementType())) {
            String value = element.getText();
            if (value != null) {
                TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                        element.getTextRange().getEndOffset());
                holder.createErrorAnnotation(range, "Unresolved method type");
            }
        }
    }

    private void validateDuplicateParams(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        ProposalGetparams parent = (ProposalGetparams) element.getParent();
        String text = element.getNode().findChildByType(ProposalTypes.MODEL_PROPERTY_KEY).getText();

        List<ProposalModeldef> modeldefList = parent.getModeldefList();

        if (modeldefList.stream().anyMatch(x -> x != element && x.getNode().findChildByType(ProposalTypes.MODEL_PROPERTY_KEY).getText().equals(text))) {
            TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                    element.getTextRange().getEndOffset());
            holder.createErrorAnnotation(range, "Duplicate property definition in endpoint params");
        }
    }

    private void validateInvalidParams(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        ProposalGetparams parent = (ProposalGetparams) element.getParent();
        ProposalEndpoint endpoint = ((ProposalEndpoint) parent.getParent().getParent());
        ASTNode childByType = endpoint.getNode().findChildByType(ProposalTypes.PATH_VALUE);
        if (childByType == null) return;

        String endpointPath = childByType.getText();

        Pattern compile = Pattern.compile("\\{([\\w]*)}");

        List<String> allMatches = new ArrayList<>();
        Matcher matcher = compile.matcher(endpointPath);
        while (matcher.find()) {
            allMatches.add(matcher.group().substring(1, matcher.group().length() - 1));
        }
        String text = element.getNode().findChildByType(ProposalTypes.MODEL_PROPERTY_KEY).getText();

        if (allMatches.stream().noneMatch(x -> x.equals(text))) {
            TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                    element.getTextRange().getEndOffset());
            holder.createErrorAnnotation(range, "No such endpoint parameter");
        }
    }
}

