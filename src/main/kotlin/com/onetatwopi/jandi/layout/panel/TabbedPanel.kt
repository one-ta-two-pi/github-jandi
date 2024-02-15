package com.onetatwopi.jandi.layout.panel

import IssueSubmitDialog
import com.intellij.notification.NotificationType
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.notificationGroup
import com.intellij.ui.components.JBTabbedPane
import com.onetatwopi.jandi.client.GitClient
import com.onetatwopi.jandi.layout.dialog.PullRequestSubmitDialog
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import com.onetatwopi.jandi.layout.panel.pullRequest.PullRequestPanel
import com.onetatwopi.jandi.project.ProjectRepository
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.Timer


object TabbedPanel {

    private val panel = JPanel(BorderLayout())

    private val tabbedPane = JBTabbedPane()
    private val addButton = JButton("+")
    private val refreshButton = JButton("Refresh")
    private val project = ProjectRepository.getProject()

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
            if (GitClient.loginId == null) {
                val notification = notificationGroup.createNotification(
                    "No token",
                    "No github token",
                    NotificationType.WARNING
                )

                val timer = Timer(3000) {
                    notification.expire()
                }

                timer.isRepeats = false
                timer.start()

                notification.notify(project)

                //return@addActionListener
            }
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