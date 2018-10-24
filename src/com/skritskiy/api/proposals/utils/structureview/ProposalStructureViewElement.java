package com.skritskiy.api.proposals.utils.structureview;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.skritskiy.api.proposals.file.ProposalFile;
import com.skritskiy.api.proposals.psi.ProposalEndpoint;
import com.skritskiy.api.proposals.psi.ProposalModel;
import com.skritskiy.api.proposals.psi.impl.ProposalEndpointImpl;
import com.skritskiy.api.proposals.psi.impl.ProposalModelImpl;
import com.skritskiy.api.proposals.utils.ProposalUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProposalStructureViewElement implements StructureViewTreeElement, SortableTreeElement {

    private NavigatablePsiElement element;

    public ProposalStructureViewElement(NavigatablePsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        String name = element.getName();
        return name != null ? name : "";
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        ItemPresentation presentation = element.getPresentation();
        return presentation != null ? presentation : new PresentationData();
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        if (element instanceof ProposalFile) {
            List<TreeElement> treeElements = new ArrayList<TreeElement>();

            ProposalModel[] models = PsiTreeUtil.getChildrenOfType(element, ProposalModel.class);
            if (models != null) {
                for (ProposalModel property : models) {
                    treeElements.add(new ProposalStructureViewElement((ProposalModelImpl) property));
                }
            }

            ProposalEndpoint[] endpoints = PsiTreeUtil.getChildrenOfType(element, ProposalEndpoint.class);
            if (endpoints != null) {

                for (ProposalEndpoint endpoint : endpoints) {
                    treeElements.add(new ProposalStructureViewElement((ProposalEndpointImpl) endpoint));
                }
            }

            return treeElements.toArray(new TreeElement[treeElements.size()]);
        } else if (element instanceof ProposalModel) {
            ProposalModel element = (ProposalModel) this.element;
            List<ProposalModel> hierarchy = ProposalUtil.getHierarchy(element);

            List<PsiElement> properties = hierarchy.stream().flatMap(x -> x.getProperties().stream()).collect(Collectors.toList());
            TreeElement[] hierarchyProperties = properties.stream().map(x -> new ProposalInheritedStructureViewElement((NavigatablePsiElement) x)).toArray(TreeElement[]::new);
            TreeElement[] treeElements = element.getProperties().stream().map(x -> new ProposalStructureViewElement((NavigatablePsiElement) x)).toArray(TreeElement[]::new);
            List<TreeElement> treeElements1 = new ArrayList<>(Arrays.asList(treeElements));
            treeElements1.addAll(Arrays.asList(hierarchyProperties));

            return treeElements1.toArray(new TreeElement[treeElements1.size()]);
        } else {
            return EMPTY_ARRAY;
        }
    }

    @Override
    public void navigate(boolean requestFocus) {
        element.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return element.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element.canNavigateToSource();
    }
}
