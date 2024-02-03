package com.onetatwopi.jandi.layout.panel

import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel

class PanelGenerator {

    private val mainPanel: JPanel = JPanel()
    private val pullRequestPanel: JPanel = JPanel()

    fun generatePanel(): JPanel {
        mainPanel.layout = BorderLayout(0, 20)
        mainPanel.border = BorderFactory.createEmptyBorder()

        mainPanel.add(generatePullRequestPanel(), BorderLayout.PAGE_START)

        return mainPanel
    }

    fun generatePullRequestPanel(): JPanel {
        pullRequestPanel.name = "Pull Request"

        return pullRequestPanel
    }
}