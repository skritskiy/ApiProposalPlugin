package com.skritskiy.api.proposals.utils;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.skritskiy.api.proposals.psi.ProposalTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProposalBlock extends AbstractBlock {
    private static final List<IElementType> NON_INDENTING_TYPES = Arrays.asList(
            ProposalTypes.ENDPOINT,
            ProposalTypes.MODEL_KW,
            ProposalTypes.MODEL,
            ProposalTypes.METHOD_TYPE,
            ProposalTypes.GET_PARAMS_KW,
            ProposalTypes.CB,
            ProposalTypes.REQUEST,
            ProposalTypes.RESPONSE
    );

    private static final List<IElementType> NON_WRAPPING_TYPES = Arrays.asList(
            ProposalTypes.ENUMDEF,
            ProposalTypes.MODELDEF,
            ProposalTypes.ENDPOINTDEF

    );

    private SpacingBuilder spacingBuilder;

    protected ProposalBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment,
                            SpacingBuilder spacingBuilder) {
        super(node, wrap, alignment);
        this.spacingBuilder = spacingBuilder;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        while (child != null) {
            if (child.getElementType() != TokenType.WHITE_SPACE && NON_WRAPPING_TYPES.contains(child.getElementType())) {
                ASTNode childOfChild = child.getFirstChildNode();
                while (childOfChild != null) {
                    if (childOfChild.getElementType() != TokenType.WHITE_SPACE) {
                        Block block = new ProposalBlock(childOfChild, Wrap.createWrap(WrapType.NONE, true), Alignment.createAlignment(),
                                spacingBuilder);
                        blocks.add(block);
                    }
                    childOfChild = childOfChild.getTreeNext();
                }
            } else if (child.getElementType() != TokenType.WHITE_SPACE) {
                Block block = new ProposalBlock(child, Wrap.createWrap(WrapType.NONE, true), Alignment.createAlignment(),
                        spacingBuilder);
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    @Override
    public Indent getIndent() {
        return NON_INDENTING_TYPES.contains(myNode.getElementType()) ? Indent.getNoneIndent() : Indent.getNormalIndent();
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
