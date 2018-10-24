package com.skritskiy.api.proposals.utils;

import com.intellij.psi.PsiElement;
import com.intellij.usages.impl.rules.UsageType;
import com.intellij.usages.impl.rules.UsageTypeProvider;
import com.skritskiy.api.proposals.psi.ProposalEndpoint;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProposalUsageTypeProvider implements UsageTypeProvider {
    private static final UsageType USAGE_ENDPOINT = new UsageType("Endpoint");
    private static final UsageType USAGE_MODEL = new UsageType("Model");

    @Nullable
    @Override
    public UsageType getUsageType(PsiElement element) {
        PsiElement parent = element.getParent();
        List<PsiElement> list = new ArrayList<>();
        while (parent != null) {
            list.add(parent);
            parent = parent.getParent();
        }

        return list.stream().anyMatch(x -> x instanceof ProposalEndpoint) ? USAGE_ENDPOINT : USAGE_MODEL;
    }
}
