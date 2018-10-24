package com.skritskiy.api.proposals.utils;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.skritskiy.api.proposals.Icons;
import com.skritskiy.api.proposals.psi.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProposalPsiUtil {
    @Nullable
    @NonNls
    public static String getName(ProposalModel element) {
        ASTNode nameNode = element.getNode().findChildByType(ProposalTypes.MN).findChildByType(ProposalTypes.MODEL_NAME);
        if (nameNode != null) {
            return nameNode.getText();
        } else {
            return null;
        }
    }

    @Nullable
    @NonNls
    public static String getName(ProposalEndpoint element) {
        ASTNode nameNode = element.getNode().findChildByType(ProposalTypes.PATH_VALUE);
        ASTNode methodNode = element.getNode().findChildByType(ProposalTypes.METHOD_TYPE);
        if (nameNode != null) {
            if (methodNode != null) {
                return methodNode.getText() + " " + nameNode.getText();
            }
            return nameNode.getText();
        } else {
            return null;
        }
    }

    public static PsiElement setName(ProposalModel element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(ProposalTypes.MN);
        if (keyNode != null) {

            ProposalMn property = ProposalElementFactory.createModelName(element.getProject(), newName);
            ASTNode newKeyNode = property.getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static ProposalModel getParentModel(ProposalModel element) {
        ASTNode keyNode = element.getNode().findChildByType(ProposalTypes.MI);
        if (keyNode != null) {
            String parentModelName = keyNode.findChildByType(ProposalTypes.MN).findChildByType(ProposalTypes.MODEL_NAME).getText();
            List<ProposalModel> model = ProposalUtil.findModel(element.getProject(), parentModelName);
            if (model.size() == 1) {
                return model.get(0);
            }
        }
        return null;
    }

    public static PsiElement getNameIdentifier(ProposalEndpoint element) {
        String modelName = element.getNode().findChildByType(ProposalTypes.PATH_VALUE).getText();
        List<ProposalEndpoint> model = ProposalUtil.findEndpoint(element.getProject(), modelName);
        if (model.size() == 0) return null;

        return model.get(0).getNode().getPsi();
    }

    public static PsiElement getNameIdentifier(ProposalModel element) {
        String modelName = element.getNode().findChildByType(ProposalTypes.MN).findChildByType(ProposalTypes.MODEL_NAME).getText();
        List<ProposalModel> model = ProposalUtil.findModel(element.getProject(), modelName);
        if (model.size() == 0) return null;

        return model.get(0).getNode().getPsi();
    }

    public static PsiElement getNameIdentifier(ProposalMn element) {
        String modelName = element.getNode().findChildByType(ProposalTypes.MODEL_NAME).getText();
        List<ProposalModel> model = ProposalUtil.findModel(element.getProject(), modelName);
        if (model.size() == 0) return null;

        return model.get(0).getNode().getPsi();
    }

    public static String getName(ProposalMn element) {
        ASTNode nameNode = element.getNode().findChildByType(ProposalTypes.MODEL_NAME);
        if (nameNode != null) {
            return nameNode.getText();
        } else {
            return null;
        }
    }

    public static PsiElement setName(ProposalMn element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(ProposalTypes.MODEL_NAME);
        if (keyNode != null) {

            ProposalMn property = ProposalElementFactory.createModelName(element.getProject(), newName);
            ASTNode newKeyNode = property.getFirstChild().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static List<ProposalModel> getModels(ProposalEndpoint element) {
        ASTNode keyNode = element.getNode();
        ArrayList<ASTNode> models = new ArrayList<>();

        ASTNode requestNode = keyNode.findChildByType(ProposalTypes.ENDPOINTDEF).findChildByType(ProposalTypes.REQUEST);
        if (requestNode != null) {
            models.add(requestNode.findChildByType(ProposalTypes.MODEL));
        }

        ASTNode responseNode = keyNode.findChildByType(ProposalTypes.ENDPOINTDEF).findChildByType(ProposalTypes.RESPONSE);
        if (responseNode != null) {
            models.add(responseNode.findChildByType(ProposalTypes.MODEL));
        }

        ASTNode paramsNode = keyNode.findChildByType(ProposalTypes.ENDPOINTDEF).findChildByType(ProposalTypes.GETPARAMS);
        if (paramsNode != null) {
            List<ASTNode> collect = Arrays.asList(paramsNode.getChildren(TokenSet.create(ProposalTypes.MODELDEF))).stream().map(x -> x.findChildByType(ProposalTypes.MODEL)).collect(Collectors.toList());
            models.addAll(collect);
        }

        return models.stream().map(x -> (ProposalModel) x.getPsi()).collect(Collectors.toList());
    }

    public static List<PsiElement> getProperties(ProposalModel element) {
        List<ProposalEnumdef> enumdefList = element.getEnumdefList();
        List<ProposalModeldef> modeldefs = element.getModeldefList();

        List<PsiElement> result = new ArrayList<>(enumdefList);
        result.addAll(modeldefs);

        return result;
    }

    public static ItemPresentation getPresentation(final ProposalModel element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return element.getContainingFile().getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return Icons.FILE;
            }
        };
    }

    public static ItemPresentation getPresentation(final ProposalEndpoint element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return element.getContainingFile().getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return Icons.FILE;
            }
        };
    }

    public static ItemPresentation getPresentation(final ProposalModeldef element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                String name = element.getNode().findChildByType(ProposalTypes.MODEL_PROPERTY_KEY).getText();
                String value = element.getNode().findChildByType(ProposalTypes.MODEL).getText();

                return name + ": " + value;
            }

            @Nullable
            @Override
            public String getLocationString() {
                return element.getContainingFile().getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return Icons.FILE;
            }
        };
    }

    public static ItemPresentation getPresentation(final ProposalEnumdef element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getNode().findChildByType(ProposalTypes.ENUMVAL).getText();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return element.getContainingFile().getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return Icons.FILE;
            }
        };
    }
}
