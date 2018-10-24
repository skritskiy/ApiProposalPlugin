package com.skritskiy.api.proposals.utils.structureview;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.psi.PsiFile;
import com.skritskiy.api.proposals.file.ProposalFile;
import org.jetbrains.annotations.NotNull;

public class ProposalStructureViewModel extends StructureViewModelBase implements
        StructureViewModel.ElementInfoProvider {
    public ProposalStructureViewModel(@NotNull PsiFile psiFile) {
        super(psiFile, new ProposalStructureViewElement(psiFile));
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element instanceof ProposalFile;
    }
}
