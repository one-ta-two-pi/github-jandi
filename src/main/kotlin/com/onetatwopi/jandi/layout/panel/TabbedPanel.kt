package com.onetatwopi.jandi.layout.panel

import com.onetatwopi.jandi.layout.dialog.IssueSubmitDialog
import com.intellij.notification.NotificationType
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.notificationGroup
import com.intellij.ui.components.JBTabbedPane
import com.onetatwopi.jandi.client.GitClient
import com.onetatwopi.jandi.layout.dialog.PullRequestSubmitDialog
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import com.onetatwopi.jandi.layout.panel.pullRequest.PullRequestPanel
import com.onetatwopi.jandi.login.LoginActivity
import com.onetatwopi.jandi.project.ProjectRepository
import org.reflections.Reflections
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.Timer


object TabbedPanel {

    private val panel = JPanel(BorderLayout())

    private val tabbedPane = JBTabbedPane()
    private val repositoryComboBox = JComboBox<String>()
    private val addButton = JButton("+")
    private val refreshButton = JButton("Refresh")
    private val loginButton = JButton("Login")
    private val project = ProjectRepository.getProject()

    init {
        val buttonPanel = JPanel()

        GitClient.repos.forEach {
            repositoryComboBox.addItem(it)
        }

        buttonPanel.add(repositoryComboBox)
        buttonPanel.add(addButton)
        buttonPanel.add(refreshButton)
        buttonPanel.add(loginButton)

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

                return@addActionListener
            }

            if(!PullRequestSubmitDialog.isShowing() && !IssueSubmitDialog.isShowing()){
                when (tabbedPane.getTitleAt(tabbedPane.selectedIndex)) {
                    "Pull Request" -> PullRequestSubmitDialog.show()
                    "Issue" -> IssueSubmitDialog.show()
                }
            }
        }

        loginButton.addActionListener {
            LoginActivity().run(project = project)
        }

        repositoryComboBox.addActionListener {
            val packageName = MainPanelAdaptor::class.java.`package`.name
            val reflections = Reflections(packageName)
            val classes = reflections.getSubTypesOf(MainPanelAdaptor::class.java)

            for (clazz in classes) {
                val instance = clazz.kotlin.objectInstance
                instance?.let {
                    val method = clazz.getDeclaredMethod("refresh")
                    method.invoke(it)
                }
            }
        }
    }

    fun addTab(contentPanel: ContentPanel) {
        tabbedPane.addTab(contentPanel.name, contentPanel.panel)
    }

    fun getPanel(): JPanel {
        return panel
    }

    fun getSelectedRepository(): String {
        return repositoryComboBox.selectedItem as String
    }
}