package com.skritskiy.api.proposals.utils;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.skritskiy.api.proposals.ProposalLanguage;
import com.skritskiy.api.proposals.psi.ProposalTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProposalFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        FormattingModel formattingModelForPsiFile = FormattingModelProvider
                .createFormattingModelForPsiFile(element.getContainingFile(),
                        new ProposalBlock(element.getNode(),
                                Wrap.createWrap(WrapType.NONE, false),
                                Alignment.createAlignment(),
                                createSpaceBuilder(settings)),
                        settings);
        int i = 0;
        return formattingModelForPsiFile;
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, ProposalLanguage.INSTANCE)
                .between(ProposalTypes.METHOD_TYPE, ProposalTypes.PATH_VALUE)
                .spacing(1, 1, 0, false, 0)
                .between(ProposalTypes.MODEL, ProposalTypes.ENDPOINT)
                .spacing(1, 1, 2, false, 0)
                .between(ProposalTypes.MODEL, ProposalTypes.MODEL)
                .spacing(1, 1, 2, false, 0)
                .between(ProposalTypes.ENDPOINT, ProposalTypes.ENDPOINT)
                .spacing(1, 1, 2, false, 0)
                .between(ProposalTypes.COLON, ProposalTypes.MODEL)
                .spacing(1, 1, 0, false, 0)
                .after(ProposalTypes.COLON)
                .spacing(1, 1, 0, false, 0)
                .between(ProposalTypes.MODEL, ProposalTypes.MODEL_PROPERTY_KEY)
                .lineBreakInCode()
                .between(ProposalTypes.MN, ProposalTypes.OB)
                .spacing(1,1,0,false,0)
                .between(ProposalTypes.PATH_VALUE, ProposalTypes.OB)
                .spacing(1,1,0,false,0)
                .before(ProposalTypes.COLON)
                .spaces(0)
                .after(ProposalTypes.COLON)
                .blankLines(0)
                .before(ProposalTypes.OB)
                .spaces(1)
                .after(ProposalTypes.OB)
                .lineBreakInCode()
                .after(ProposalTypes.CB)
                .lineBreakInCode()
                .after(ProposalTypes.ENUMDEF)
                .lineBreakInCode()
                .before(ProposalTypes.MODELDEF)
                .lineBreakInCode()
                .before(ProposalTypes.REQUEST_KW)
                .lineBreakInCode()
                .before(ProposalTypes.RESPONSE_KW)
                .lineBreakInCode()
                .before(ProposalTypes.CB)
                .lineBreakInCode()
                .before(ProposalTypes.GETPARAMS)
                .lineBreakInCode();
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}
