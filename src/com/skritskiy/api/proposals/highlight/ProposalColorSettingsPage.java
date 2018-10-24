package com.skritskiy.api.proposals.highlight;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.skritskiy.api.proposals.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class ProposalColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keyword", ProposalSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("Separator", ProposalSyntaxHighlighter.SEPARATOR),
            new AttributesDescriptor("Model name", ProposalSyntaxHighlighter.MODEL_NAME),
            new AttributesDescriptor("Model property name", ProposalSyntaxHighlighter.MODEL_PROPERTY_NAME),
            new AttributesDescriptor("Model property type", ProposalSyntaxHighlighter.MODEL_PROPERTY_TYPE),
            new AttributesDescriptor("Endpoint property name", ProposalSyntaxHighlighter.ENDPOINT_PROPERTY_KEY),
            new AttributesDescriptor("Endpoint method type", ProposalSyntaxHighlighter.ENDPOINT_METHOD_TYPE),
            new AttributesDescriptor("Endpoint path", ProposalSyntaxHighlighter.ENDPOINT_PATH),
            new AttributesDescriptor("Endpoint request & response model", ProposalSyntaxHighlighter.ENDPOINT_REQUEST_RESPONSE_MODEL),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new ProposalSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "endpoint TestEndpoint {\n" +
                "    method: POST\n" +
                "    path: /test/{testId}/test2/test3\n" +
                "    responseModel: TestResponseModel\n" +
                "    requestBody: TestRequest\n" +
                "}\n" +
                "\n" +
                "model TestResponseModel {\n" +
                "    id : Integer\n" +
                "    test: TestModel\n" +
                "    test2: Test2Model\n" +
                "}\n" +
                "\n" +
                "model Test2Model {\n" +
                "    id: Integer\n" +
                "}\n";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Proposal";
    }
}
