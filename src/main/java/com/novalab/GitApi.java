package com.novalab;

import io.gitea.ApiClient;
import io.gitea.ApiException;
import io.gitea.Configuration;
import io.gitea.api.IssueApi;
import io.gitea.api.RepositoryApi;
import io.gitea.auth.ApiKeyAuth;
import io.gitea.model.Comment;
import io.gitea.model.PullRequest;

import java.util.ArrayList;
import java.util.List;

public class GitApi {
    /// Input url which enter user.
    private final String url = "https://try.gitea.io/AlexKushch/test/pulls/2";  //https://gitea.novalab.live/novalab-pool/diff-reviewer/pulls/3

    /// Pull request title.
    private String titlePR;
    /// Pull request description.
    private String descriptionPR;
    /// Pull request creator.
    private String creatorPR;
    /// Pull request status.
    private Status statusPR;
    /// URL to diff file.
    private String diffURL;
    /// List of comments in pull request discussions.
    private List<Comment> discussions = new ArrayList<Comment>();

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
        /// Set configuration and access token.
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://try.gitea.io/api/v1");

        ApiKeyAuth accessToken = (ApiKeyAuth) defaultClient.getAuthentication("AccessToken");
        accessToken.setApiKey("1c507ac020604c7a30315569fd6ccde908a64a25");  // 4399ec28ea4be7de3d5bfbc22c0e8c9da1058c62

        /// Divide url by '/' and get owner, repo and index from url.
        String [] split = url.split("/");

        String owner = split[3]; // Owner in url always the third.
        String repo = split[4]; // Repo goes after owner
        Long index = Long.parseLong(split[split.length-1]); // Index always the last in url.

        /// Get PR info.
        RepositoryApi repositoryApi = new RepositoryApi();
        PullRequest result = new PullRequest();
        try {
            result = repositoryApi.repoGetPullRequest(owner, repo, index);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        setTitlePR(result.getTitle());  // Get title.
        setDescriptionPR(result.getBody()); // Get description.
        setCreatorPR(result.getUser().getLogin());  // Get creator.

        /// Class 'PullRequest' has two field. State(open, close), Merged(merged, not merged).
        String state = result.getState();
        if(state.equals("open")){       // First of all check by State. If is 'open' then
            if(result.getMergedBy() != null){   // Check by merged.
                setStatusPR(Status.MERGED);
            } else {
                setStatusPR(Status.NOT_MERGED);
            }
        } else {
            setStatusPR(Status.CLOSED);
        }

        /// All comments are located in list of all comments. Here issues comments and PR comments.
        /// But has a different, comments from PR have a link, issue hasn't.
        IssueApi issueApi = new IssueApi();
        try {
            discussions = issueApi.issueGetRepoComments(owner, repo, "");
            for(int i = 0; i < discussions.size(); i++){
                if(!discussions.get(i).getPullRequestUrl().equals(url)){
                    discussions.remove(i);      // Remove from discussions comments which located in issues
                    i--;
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }

        setDiffURL(result.getDiffUrl());

        System.out.println(discussions);
    }
}
