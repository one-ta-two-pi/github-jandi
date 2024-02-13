package com.onetatwopi.jandi.layout.panel

import com.intellij.ui.components.JBTabbedPane
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import com.onetatwopi.jandi.layout.panel.pullRequest.PullRequestPanel
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.*

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
            val dialog = JDialog()
            dialog.title = "Create Pull Request"

            val titleLabel = JLabel("Title:")
            val titleField = JTextField(20)
            val userLabel = JLabel("User:")
            val userField = JTextField(20)
            val statusLabel = JLabel("Status:")
            val statusComboBox = JComboBox(arrayOf("Open", "Closed", "In Progress"))
            val urlLabel = JLabel("URL:")
            val urlField = JTextField(20)
            val submitButton = JButton("Submit")

            submitButton.addActionListener {
                val title = titleField.text
                val user = userField.text
                val status = statusComboBox.selectedItem as String
                val url = urlField.text

                println("Title: $title, User: $user, Status: $status, URL: $url")

                dialog.isVisible = false
            }

            dialog.layout = GridLayout(5, 2)
            dialog.add(titleLabel)
            dialog.add(titleField)
            dialog.add(userLabel)
            dialog.add(userField)
            dialog.add(statusLabel)
            dialog.add(statusComboBox)
            dialog.add(urlLabel)
            dialog.add(urlField)
            dialog.add(submitButton)

            dialog.setSize(300, 200)
            dialog.setLocationRelativeTo(addButton)

            dialog.isVisible = true
        }
    }

    fun addTab(contentPanel: ContentPanel) {
        tabbedPane.addTab(contentPanel.name, contentPanel.panel)
    }

    fun getPanel(): JPanel {
        return panel
    }
}