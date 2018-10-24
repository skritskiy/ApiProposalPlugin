package com.skritskiy.api.proposals.utils;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.skritskiy.api.proposals.Icons;
import com.skritskiy.api.proposals.ProposalLanguage;
import com.skritskiy.api.proposals.psi.ProposalTypes;
import org.jetbrains.annotations.NotNull;

public class ProposalCompletionContributor extends CompletionContributor {

    public static final String DUMMY_IDENTIFIER = CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED;

    public ProposalCompletionContributor() {
        CompletionProvider<CompletionParameters> modelTypeCompletionProvider = new CompletionProvider<CompletionParameters>() {
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                ProposalUtil.findModels(parameters.getEditor().getProject()).stream().forEach(x -> {
                    resultSet.addElement(LookupElementBuilder.create(x).withIcon(Icons.FILE));
                });
                Predefined.STANDART_TYPES.forEach(x->{
                    resultSet.addElement(LookupElementBuilder.create(x).withTypeText("Standart type"));

                });
            }
        };

        CompletionProvider<CompletionParameters> restRequestTypeCompletionProvider = new CompletionProvider<CompletionParameters>() {
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                resultSet.addElement(LookupElementBuilder.create("POST"));
                resultSet.addElement(LookupElementBuilder.create("PUT"));
                resultSet.addElement(LookupElementBuilder.create("GET"));
                resultSet.addElement(LookupElementBuilder.create("DELETE"));
                resultSet.addElement(LookupElementBuilder.create("HEAD"));
            }
        };

        CompletionProvider<CompletionParameters> keywordCompletionProvider = new CompletionProvider<CompletionParameters>() {
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                resultSet.addElement(LookupElementBuilder.create("endpoint"));
                resultSet.addElement(LookupElementBuilder.create("model"));
            }
        };


        CompletionProvider<CompletionParameters> endpointResponseModelCompletionProvider = new CompletionProvider<CompletionParameters>() {
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                resultSet.addElement(LookupElementBuilder.create("params"));
                resultSet.addElement(LookupElementBuilder.create("response"));
                resultSet.addElement(LookupElementBuilder.create("request"));
            }
        };

        CompletionProvider<CompletionParameters> endpointRequestBodyCompletionProvider = new CompletionProvider<CompletionParameters>() {
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                resultSet.addElement(LookupElementBuilder.create("response"));
                resultSet.addElement(LookupElementBuilder.create("params"));
            }
        };

        CompletionProvider<CompletionParameters> endpointParamsCompletionProvider = new CompletionProvider<CompletionParameters>() {
            public void addCompletions(@NotNull CompletionParameters parameters,
                                       ProcessingContext context,
                                       @NotNull CompletionResultSet resultSet) {
                resultSet.addElement(LookupElementBuilder.create("params"));
            }
        };

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(ProposalTypes.UM).withLanguage(ProposalLanguage.INSTANCE),
                restRequestTypeCompletionProvider
        );

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(ProposalTypes.UKW).withLanguage(ProposalLanguage.INSTANCE),
                keywordCompletionProvider
        );

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(ProposalTypes.URESPKW).withLanguage(ProposalLanguage.INSTANCE),
                endpointRequestBodyCompletionProvider
        );

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(ProposalTypes.UREQKW).withLanguage(ProposalLanguage.INSTANCE),
                endpointResponseModelCompletionProvider
        );

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(ProposalTypes.UPARKW).withLanguage(ProposalLanguage.INSTANCE),
                endpointParamsCompletionProvider
        );

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(ProposalTypes.MODEL_NAME).withLanguage(ProposalLanguage.INSTANCE),
                modelTypeCompletionProvider
        );
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        super.fillCompletionVariants(parameters, result);
    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        context.setDummyIdentifier(String.valueOf(DUMMY_IDENTIFIER));
    }
}
