package com.skritskiy.api.proposals.utils;

import com.intellij.find.findUsages.CustomUsageSearcher;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.ex.ApplicationEx;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageInfo2UsageAdapter;
import com.intellij.util.Processor;
import com.skritskiy.api.proposals.psi.ProposalEndpoint;
import com.skritskiy.api.proposals.psi.ProposalModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProposalCustomUsageSearcher extends CustomUsageSearcher {

    private ApplicationEx applicationEx;

    public ProposalCustomUsageSearcher(ApplicationEx applicationEx) {
        this.applicationEx = applicationEx;
    }

    @Override
    public void processElementUsages(@NotNull PsiElement element, @NotNull Processor<Usage> processor, @NotNull FindUsagesOptions options) {
        AccessToken accessToken = applicationEx.acquireReadActionLock();
        Project project = element.getContainingFile().getProject();
        if(!(element instanceof ProposalModel)){
            return;
        }

        ProposalModel parent = ((ProposalModel) element);

        List<ProposalModel> allModels = ProposalUtil.findModels(project);
        List<ProposalModel> targetModels = new ArrayList<>();

        for (ProposalModel model : allModels) {
            if(ProposalUtil.getHierarchy(model).contains(parent)){
                targetModels.add(model);
            }
        }

        List<ProposalEndpoint> endpoints = ProposalUtil.findEndpoints(element.getProject());
        for (ProposalEndpoint endpoint : endpoints) {
            List<ProposalModel> models = endpoint.getModels();
            for (ProposalModel model : models) {
                if(targetModels.stream().anyMatch(x->x.getNameIdentifier()!=null && x.getNameIdentifier().equals(model.getNameIdentifier()))){
                    UsageInfo usageInfo = new UsageInfo(model);
                    processor.process(new UsageInfo2UsageAdapter(usageInfo));
                }
            }
        }

        accessToken.finish();
    }
}
