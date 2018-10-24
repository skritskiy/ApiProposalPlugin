package com.skritskiy.api.proposals.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.skritskiy.api.proposals.ProposalLexerAdapter;
import com.skritskiy.api.proposals.psi.ProposalTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ProposalSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey SEPARATOR =
            createTextAttributesKey("COLON", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("MODEL_KW", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey MODEL_NAME =
            createTextAttributesKey("MODEL_NAME", DefaultLanguageHighlighterColors.STATIC_FIELD);
    public static final TextAttributesKey MODEL_PROPERTY_NAME =
            createTextAttributesKey("MODEL_PROPERTY_NAME", DefaultLanguageHighlighterColors.LABEL);
    public static final TextAttributesKey MODEL_PROPERTY_TYPE =
            createTextAttributesKey("MODEL_PROPERTY_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey ENDPOINT_PROPERTY_KEY =
            createTextAttributesKey("ENDPOINT_PROPERTY_KEY", DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT);
    public static final TextAttributesKey ENDPOINT_METHOD_TYPE =
            createTextAttributesKey("ENDPOINT_METHOD_TYPE", DefaultLanguageHighlighterColors.METADATA);
    public static final TextAttributesKey ENDPOINT_PATH =
            createTextAttributesKey("ENDPOINT_PATH", DefaultLanguageHighlighterColors.LABEL);
    public static final TextAttributesKey ENDPOINT_REQUEST_RESPONSE_MODEL =
            createTextAttributesKey("ENDPOINT_REQUEST_RESPONSE_MODEL", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey ENUM_VALUE =
            createTextAttributesKey("ENUM_VALUE", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

    private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR};
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] NAME_KEYS = new TextAttributesKey[]{MODEL_NAME};
    private static final TextAttributesKey[] MODEL_PARAMETER_NAME_KEYS = new TextAttributesKey[]{MODEL_PROPERTY_NAME};
    private static final TextAttributesKey[] ENDPOINT_PROPERTY_KEY_KEYS = new TextAttributesKey[]{ENDPOINT_PROPERTY_KEY};
    private static final TextAttributesKey[] ENDPOINT_METHOD_TYPE_KEYS = new TextAttributesKey[]{ENDPOINT_METHOD_TYPE};
    private static final TextAttributesKey[] ENDPOINT_PATH_KEYS = new TextAttributesKey[]{ENDPOINT_PATH};
    private static final TextAttributesKey[] ENUM_VALUE_KEYS = new TextAttributesKey[]{ENUM_VALUE};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};

    private static final List<IElementType> ENDPOINT_PROPERTY_KEYS = Arrays.asList(
            ProposalTypes.RESPONSE_KW,
            ProposalTypes.REQUEST_KW,
            ProposalTypes.GET_PARAMS_KW);


    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ProposalLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(ProposalTypes.COLON)) {
            return SEPARATOR_KEYS;
        } else if (tokenType.equals(ProposalTypes.MODEL_KW)) {
            return KEYWORD_KEYS;
        } else if (tokenType.equals(ProposalTypes.MODEL_NAME)) {
            return NAME_KEYS;
        } else if (tokenType.equals(ProposalTypes.MODEL_PROPERTY_KEY)) {
            return MODEL_PARAMETER_NAME_KEYS;
        }  else if (ENDPOINT_PROPERTY_KEYS.contains(tokenType)){
            return ENDPOINT_PROPERTY_KEY_KEYS;
        } else if (tokenType.equals(ProposalTypes.METHOD_TYPE)){
            return ENDPOINT_METHOD_TYPE_KEYS;
        } else if (tokenType.equals(ProposalTypes.PATH_VALUE)){
            return ENDPOINT_PATH_KEYS;
        }  else if (tokenType.equals(ProposalTypes.ENUMVAL)) {
            return ENUM_VALUE_KEYS;
        } else if (tokenType.equals(ProposalTypes.COMMENT)){
            return COMMENT_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}
