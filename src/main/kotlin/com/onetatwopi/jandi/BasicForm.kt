package com.onetatwopi.jandi

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.platform.PlatformProjectOpenProcessor.Companion.isNewProject
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel


class BasicForm: ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(p0: Project, p1: ToolWindow) {
        p1.setAnchor(ToolWindowAnchor.BOTTOM) {
            p0.isNewProject()
        }

        contentPanel.layout = BorderLayout(0, 20)
        contentPanel.border = BorderFactory.createEmptyBorder()

        val content = ContentFactory.getInstance().createContent(contentPanel, "", false)
        p1.contentManager.addContent(content)
    }
}

val contentPanel = JPanel()