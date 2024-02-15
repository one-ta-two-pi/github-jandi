package com.onetatwopi.jandi.layout

import com.intellij.notification.NotificationType
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.notificationGroup
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.onetatwopi.jandi.client.GitClient
import com.onetatwopi.jandi.layout.panel.TabbedPanel
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import com.onetatwopi.jandi.layout.panel.pullRequest.PullRequestPanel
import com.onetatwopi.jandi.listener.LoginIdChangeListener
import com.onetatwopi.jandi.listener.LoginIdChangeNotifier
import com.onetatwopi.jandi.project.ProjectRepository

class MainLayout : ToolWindowFactory, DumbAware, LoginIdChangeListener {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.setAnchor(ToolWindowAnchor.LEFT) {}
        ProjectRepository.setProject(project)
        ProjectRepository.setMainToolWindow(toolWindow)

        LoginIdChangeNotifier.setListener(this)

        processLoginIdChange(GitClient.loginId)
    }

    override fun onLoginIdChanged(loginId: String?) {
        processLoginIdChange(loginId)
    }

    private fun processLoginIdChange(newLoginId: String?) {

        val contentManager = ProjectRepository.getMainToolWindow().contentManager
        contentManager.removeAllContents(true)
        if (newLoginId != null) {
            val tabbedPanel = TabbedPanel
            val pullRequestPanel = PullRequestPanel
            pullRequestPanel.render()

            val issuePanel = IssuePanel
            issuePanel.render()
            tabbedPanel.addTab(pullRequestPanel)
            tabbedPanel.addTab(issuePanel)


            val content = contentManager.factory.createContent(tabbedPanel.getPanel(), "", false)
            contentManager.addContent(content)
        } else {
            notificationGroup.createNotification("No github tokens!", NotificationType.WARNING)
        }
    }
}