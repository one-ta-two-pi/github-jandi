package com.onetatwopi.jandi.layout

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.onetatwopi.jandi.layout.panel.TabbedPanel
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import com.onetatwopi.jandi.layout.panel.pullRequest.PullRequestPanel

class MainLayout : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.setAnchor(ToolWindowAnchor.LEFT) {}

//        TODO: 해당 기능 공통 처리
//        val repositoryInfo: Pair<String, String>
//        try {
//            repositoryInfo = RepositoryParser.getOpenedRepository(project)
//            println("repositoryInfo = $repositoryInfo")
//        } catch (
//            _: Exception
//        ) {
//            println("No .git Folder")
//        }


        // TODO: 공통 처리
        // val (repoOwner, repoName) = RepositoryParser.getOpenedRepository(project)
        // println("repositoryInfo = $repoOwner, $repoName")
        // val modifiedRepoName = repoName.replace(".git", "")

        val tabbedPanel = TabbedPanel
        //val pullRequestList = pullRequestService.getPullRequestList("shouldAddToken", repositoryInfo)
        val pullRequestPanel = PullRequestPanel
        //pullRequestPanel.setPullRequestInfoList(pullRequestList)
        pullRequestPanel.render()

        val issuePanel = IssuePanel
        issuePanel.render()
        tabbedPanel.addTab(pullRequestPanel)
        tabbedPanel.addTab(issuePanel)

        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(tabbedPanel.getPanel(), "", false)
        contentManager.addContent(content)
    }
}