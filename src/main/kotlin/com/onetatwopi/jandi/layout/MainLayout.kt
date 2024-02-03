package com.onetatwopi.jandi.layout

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.platform.PlatformProjectOpenProcessor.Companion.isNewProject
import com.intellij.ui.content.ContentFactory
import com.onetatwopi.jandi.layout.panel.PanelGenerator

class MainLayout : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(p0: Project, p1: ToolWindow) {
        p1.setAnchor(ToolWindowAnchor.LEFT) {
            p0.isNewProject()
        }

        val panelGenerator = PanelGenerator()

        val contentPanel = panelGenerator.generatePanel()

        val content = ContentFactory.getInstance().createContent(contentPanel, "", false)
        p1.contentManager.addContent(content)
    }
}