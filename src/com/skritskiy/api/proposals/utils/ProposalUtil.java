package com.skritskiy.api.proposals.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.skritskiy.api.proposals.file.ProposalFile;
import com.skritskiy.api.proposals.file.ProposalFileType;
import com.skritskiy.api.proposals.psi.ProposalEndpoint;
import com.skritskiy.api.proposals.psi.ProposalModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProposalUtil {
    public static List<ProposalModel> findModel(Project project, String name) {
        List<ProposalModel> result = new ArrayList<>();

        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ProposalFileType.INSTANCE,
                GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            ProposalFile proposalFile = (ProposalFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (proposalFile != null) {
                ProposalModel[] models = PsiTreeUtil.getChildrenOfType(proposalFile, ProposalModel.class);
                if (models != null) {
                    for (ProposalModel model : models) {
                        if (name.equals(model.getName())) {
                            result.add(model);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<ProposalModel> findModels(Project project) {
        List<ProposalModel> result = new ArrayList<ProposalModel>();
        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ProposalFileType.INSTANCE,
                        GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            ProposalFile simpleFile = (ProposalFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                ProposalModel[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, ProposalModel.class);
                if (properties != null) {
                    Collections.addAll(result, properties);
                }
            }
        }
        return result;
    }

    public static List<ProposalEndpoint> findEndpoint(Project project, String name) {
        List<ProposalEndpoint> result = new ArrayList<>();

        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ProposalFileType.INSTANCE,
                GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            ProposalFile proposalFile = (ProposalFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (proposalFile != null) {
                ProposalEndpoint[] endpoints = PsiTreeUtil.getChildrenOfType(proposalFile, ProposalEndpoint.class);
                if (endpoints != null) {
                    for (ProposalEndpoint endpoint : endpoints) {
                        if (name.equals(endpoint.getName())) {
                            result.add(endpoint);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<ProposalEndpoint> findEndpoints(Project project) {
        List<ProposalEndpoint> result = new ArrayList<ProposalEndpoint>();
        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ProposalFileType.INSTANCE,
                        GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            ProposalFile simpleFile = (ProposalFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                ProposalEndpoint[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, ProposalEndpoint.class);
                if (properties != null) {
                    Collections.addAll(result, properties);
                }
            }
        }
        return result;
    }

    public static List<ProposalModel> getHierarchy(ProposalModel model) {
        List<ProposalModel> list = new ArrayList<>();
        ProposalModel parent = model.getParentModel();
        while (parent != null) {
            list.add(parent);
            parent = parent.getParentModel();
        }

        return list;
    }
}
