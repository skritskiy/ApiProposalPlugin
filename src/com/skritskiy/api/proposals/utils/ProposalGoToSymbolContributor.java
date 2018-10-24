package com.skritskiy.api.proposals.utils;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.skritskiy.api.proposals.psi.ProposalEndpoint;
import com.skritskiy.api.proposals.psi.ProposalModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProposalGoToSymbolContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        List<ProposalModel> models = ProposalUtil.findModels(project);
        List<String> names = new ArrayList<>(models.size());
        for (ProposalModel model : models) {
            if (model.getName() != null && model.getName().length() > 0) {
                names.add(model.getName());
            }
        }

        List<ProposalEndpoint> endpoints = ProposalUtil.findEndpoints(project);
        for (ProposalEndpoint endpoint : endpoints) {
            if(endpoint.getName()!= null && endpoint.getName().length() > 0) {
                names.add(endpoint.getName());

            }
        }
        return names.toArray(new String[names.size()]);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        List<ProposalModel> model = ProposalUtil.findModel(project, name);
        List<NavigationItem> items = model.stream().map(x->(NavigationItem)x).collect(Collectors.toList());
        items.addAll(ProposalUtil.findEndpoint(project, name).stream().map(x->(NavigationItem)x).collect(Collectors.toList()));

        return items.toArray(new NavigationItem[items.size()]);
    }
}
