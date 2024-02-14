package com.onetatwopi.jandi.layout.panel

import IssueSubmitDialog
import com.intellij.ui.components.JBTabbedPane
import com.onetatwopi.jandi.layout.dialog.PullRequestSubmitDialog
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import com.onetatwopi.jandi.layout.panel.pullRequest.PullRequestPanel
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel

object TabbedPanel {

    private val panel = JPanel(BorderLayout())

    private val tabbedPane = JBTabbedPane()
    private val addButton = JButton("+")
    private val refreshButton = JButton("Refresh")

    init {
        val buttonPanel = JPanel()
        buttonPanel.add(addButton)
        buttonPanel.add(refreshButton)

        panel.add(buttonPanel, BorderLayout.NORTH)
        panel.add(tabbedPane, BorderLayout.CENTER)

        tabbedPane.addChangeListener {
            when (tabbedPane.getTitleAt(tabbedPane.selectedIndex)) {
                "Pull Request" -> PullRequestPanel.refresh()
                "Issue" -> IssuePanel.refresh()
            }
        }

        refreshButton.addActionListener {
            when (tabbedPane.getTitleAt(tabbedPane.selectedIndex)) {
                "Pull Request" -> PullRequestPanel.refresh()
                "Issue" -> IssuePanel.refresh()
            }
        }

        addButton.addActionListener {
            when (tabbedPane.getTitleAt(tabbedPane.selectedIndex)) {
                "Pull Request" -> PullRequestSubmitDialog.show()
                "Issue" -> IssueSubmitDialog.show()
            }
        }
    }

    fun addTab(contentPanel: ContentPanel) {
        tabbedPane.addTab(contentPanel.name, contentPanel.panel)
    }

    fun getPanel(): JPanel {
        return panel
    }
}