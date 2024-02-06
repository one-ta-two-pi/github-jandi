package com.onetatwopi.jandi.layout

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.platform.PlatformProjectOpenProcessor.Companion.isNewProject
import com.onetatwopi.jandi.layout.dto.IssueInfo
import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import com.onetatwopi.jandi.layout.panel.TabbedPanel
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import com.onetatwopi.jandi.layout.panel.pullRequest.PullRequestPanel
import java.time.LocalDateTime

private val stubbingPullRequestData = listOf(
    PullRequestInfo("Pull Request 1", "User1", "https://www.naver.com", "Open", LocalDateTime.now()),
    PullRequestInfo("Pull Request 2", "User2", "https://www.naver.com", "Closed", LocalDateTime.now()),
    PullRequestInfo("Pull Request 3", "User3", "https://www.naver.com", "In Progress", LocalDateTime.now())
)

private val stubbingIssueInfoData = listOf(
    IssueInfo("Issue 1", "User3", "https://www.naver.com", "Open", LocalDateTime.now()),
    IssueInfo("Issue 2", "User2", "https://www.naver.com", "Open", LocalDateTime.now()),
    IssueInfo("Issue 3", "User1", "https://www.naver.com", "Open", LocalDateTime.now())
)

class MainLayout : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.setAnchor(ToolWindowAnchor.LEFT) {
            project.isNewProject()
        }

        val tabbedPanel = TabbedPanel()

        val pullRequestPanel = PullRequestPanel
        pullRequestPanel.setPullRequestInfoList(stubbingPullRequestData)
        pullRequestPanel.render()

        val issuePanel = IssuePanel
        issuePanel.setIssueInfoList(stubbingIssueInfoData)
        issuePanel.render()

        tabbedPanel.addTab(pullRequestPanel)
        tabbedPanel.addTab(issuePanel)

        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(tabbedPanel.getPanel(), "", false)
        contentManager.addContent(content)
    }
}