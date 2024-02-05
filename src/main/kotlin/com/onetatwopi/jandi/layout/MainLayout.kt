package com.onetatwopi.jandi.layout

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.platform.PlatformProjectOpenProcessor.Companion.isNewProject
import com.onetatwopi.jandi.layout.panel.ContentPanel
import com.onetatwopi.jandi.layout.panel.TabbedPanel

class MainLayout : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.setAnchor(ToolWindowAnchor.LEFT) {
            project.isNewProject()
        }

        val tabbedPanel = TabbedPanel()

        val pullRequestPanel = ContentPanel("Pull Request")
        val issuePanel = ContentPanel("Issue")

        tabbedPanel.addTab(pullRequestPanel)
        tabbedPanel.addTab(issuePanel)

        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(tabbedPanel.getPanel(), "", false)
        contentManager.addContent(content)
    }
}