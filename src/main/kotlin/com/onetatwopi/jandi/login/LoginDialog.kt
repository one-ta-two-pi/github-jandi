package com.onetatwopi.jandi.login

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.messages.showProcessExecutionErrorDialog
import com.onetatwopi.jandi.client.GitClient
import javax.swing.*

class LoginDialog(private val project: Project) : DialogWrapper(project) {
    private val userTokenField = JTextField(32)

    init {
        title = "Login"
        isResizable = false
        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(JLabel("GitToken"))
        panel.add(userTokenField)
        return panel
    }

    override fun doOKAction() {
        val token = userTokenField.text
        try {
            GitClient.login(token)
        } catch (e : Exception) {
            showProcessExecutionErrorDialog(project = project, "Login Fail", "Error", "Cause", e.message!!, 2)
            return
        }
        super.doOKAction()
    }
}