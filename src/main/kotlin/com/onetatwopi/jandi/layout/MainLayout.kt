package com.onetatwopi.jandi.layout

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.platform.PlatformProjectOpenProcessor.Companion.isNewProject
import com.onetatwopi.jandi.data.RepositoryParser
import com.onetatwopi.jandi.data.issue.IssueService
import com.onetatwopi.jandi.data.pullRequest.PullRequestService
import com.onetatwopi.jandi.layout.dto.IssueInfo
import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import com.onetatwopi.jandi.layout.panel.TabbedPanel
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import com.onetatwopi.jandi.layout.panel.pullRequest.PullRequestPanel
import kotlinx.serialization.json.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private val stubbingPullRequestData = listOf(
    PullRequestInfo(
        "Pull Request 1",
        "User1",
        "Open",
        "https://www.naver.com",
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    ),
    PullRequestInfo(
        "Pull Request 2",
        "User2",
        "Closed",
        "https://www.naver.com",
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    ),
    PullRequestInfo(
        "Pull Request 3",
        "User3",
        "In Progress",
        "https://www.naver.com",
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )
)

class MainLayout : ToolWindowFactory, DumbAware {

    private var pullRequestService: PullRequestService
    private val issueService = IssueService()

    init {
        pullRequestService = PullRequestService()
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.setAnchor(ToolWindowAnchor.LEFT) {
            project.isNewProject()
        }

        val repositoryInfo = RepositoryParser.getOpenedRepository(project)
        println("repositoryInfo = $repositoryInfo")

        // TODO: 공통 처리
        // val (repoOwner, repoName) = RepositoryParser.getOpenedRepository(project)
        // println("repositoryInfo = $repoOwner, $repoName")
        // val modifiedRepoName = repoName.replace(".git", "")

        val tabbedPanel = TabbedPanel()
        //val pullRequestList = pullRequestService.getPullRequestList("shouldAddToken", repositoryInfo)
        val pullRequestPanel = PullRequestPanel
        //pullRequestPanel.setPullRequestInfoList(pullRequestList)
        pullRequestPanel.setPullRequestInfoList(stubbingPullRequestData)
        pullRequestPanel.render()

        val issuePanel = IssuePanel

        issuePanel.setIssueInfoList(issueService.getIssueList())
        issuePanel.render()

        tabbedPanel.addTab(pullRequestPanel)
        tabbedPanel.addTab(issuePanel)

        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(tabbedPanel.getPanel(), "", false)
        contentManager.addContent(content)
    }
}