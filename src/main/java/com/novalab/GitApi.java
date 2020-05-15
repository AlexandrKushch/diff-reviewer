package com.novalab;

import io.gitea.ApiClient;
import io.gitea.ApiException;
import io.gitea.Configuration;
import io.gitea.api.IssueApi;
import io.gitea.api.RepositoryApi;
import io.gitea.auth.ApiKeyAuth;
import io.gitea.model.AccessToken;
import io.gitea.model.Comment;
import io.gitea.model.PullRequest;
import io.gitea.model.User;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;

public class GitApi {
    private final String url = "https://try.gitea.io/AlexKushch/test/pulls/2";  //https://gitea.novalab.live/novalab-pool/diff-reviewer/pulls/3

    private String titlePR;
    private String descriptionPR;
    private String creatorPR;
    private Status statusPR;
    private String diffURL;

    private void setTitlePR(String titlePR) {
        this.titlePR = titlePR;
    }

    private void setDescriptionPR(String descriptionPR) {
        this.descriptionPR = descriptionPR;
    }

    private void setCreatorPR(String creatorPR) {
        this.creatorPR = creatorPR;
    }

    private void setStatusPR(Status statusPR) {
        this.statusPR = statusPR;
    }

    private void setDiffURL(String diffURL) {
        this.diffURL = diffURL;
    }

    public void GetPR() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://try.gitea.io/api/v1");

        ApiKeyAuth accessToken = (ApiKeyAuth) defaultClient.getAuthentication("AccessToken");
        accessToken.setApiKey("1c507ac020604c7a30315569fd6ccde908a64a25");  // 4399ec28ea4be7de3d5bfbc22c0e8c9da1058c62

        String [] split = url.split("/");

        String owner = split[3];
        String repo = split[4];
        Long index = Long.parseLong(split[split.length-1]);

        RepositoryApi repositoryApi = new RepositoryApi();
        PullRequest result = new PullRequest();
        try {
            result = repositoryApi.repoGetPullRequest(owner, repo, index);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        setTitlePR(result.getTitle());
        setDescriptionPR(result.getBody());
        setCreatorPR(result.getUser().getLogin());

        String state = result.getState();
        if(state.equals("open")){
            if(result.getMergedBy() != null){
                setStatusPR(Status.MERGED);
            } else {
                setStatusPR(Status.NOT_MERGED);
            }
        } else {
            setStatusPR(Status.CLOSED);
        }

        IssueApi issueApi = new IssueApi();
        List<Comment> discussions = new ArrayList<Comment>();
        try {
            discussions = issueApi.issueGetRepoComments(owner, repo, "");
            for(int i = 0; i < discussions.size(); i++){
                if(!discussions.get(i).getPullRequestUrl().equals(url)){
                    discussions.remove(i);
                    i--;
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }

        setDiffURL(result.getDiffUrl());

        System.out.println(result);
    }
}
