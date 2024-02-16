package com.onetatwopi.jandi.layout

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.onetatwopi.jandi.client.GitClient
import com.onetatwopi.jandi.layout.panel.TabbedPanel
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import com.onetatwopi.jandi.layout.panel.pullRequest.PullRequestPanel
import com.onetatwopi.jandi.listener.LoginIdChangeListener
import com.onetatwopi.jandi.listener.LoginIdChangeNotifier
import com.onetatwopi.jandi.login.LoginActivity
import com.onetatwopi.jandi.project.ProjectRepository
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel

class MainLayout : ToolWindowFactory, DumbAware, LoginIdChangeListener {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.setAnchor(ToolWindowAnchor.LEFT) {}
        ProjectRepository.setProject(project)
        ProjectRepository.setMainToolWindow(toolWindow)

        LoginIdChangeNotifier.setListener(this)

        processLoginIdChange(GitClient.loginId, isUpdate = false)
    }

    override fun onLoginIdChanged(loginId: String?, isUpdate : Boolean) {
        processLoginIdChange(loginId, isUpdate = isUpdate)
    }

    private fun processLoginIdChange(newLoginId: String?, isUpdate: Boolean) {

        val contentManager = ProjectRepository.getMainToolWindow().contentManager
        contentManager.removeAllContents(true)
        if (newLoginId != null) {
            val tabbedPanel = TabbedPanel
            val pullRequestPanel = PullRequestPanel
            val issuePanel = IssuePanel

            if(!isUpdate) {
                pullRequestPanel.render()
                issuePanel.render()
                tabbedPanel.addTab(pullRequestPanel)
                tabbedPanel.addTab(issuePanel)
            } else {
                pullRequestPanel.refresh()
                issuePanel.refresh()
            }

            val content = contentManager.factory.createContent(tabbedPanel.getPanel(), "", false)
            contentManager.addContent(content)
        } else {
            val panel = JPanel()
            panel.layout = BorderLayout()
            val loginButton = JButton("Login Github")

            loginButton.addActionListener {
                LoginActivity().run(ProjectRepository.getProject())
            }

            panel.add(loginButton, BorderLayout.NORTH)

            val content = contentManager.factory.createContent(panel, "", false)
            contentManager.addContent(content)
        }
    }
}