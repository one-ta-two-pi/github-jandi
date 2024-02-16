package com.onetatwopi.jandi.login

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.onetatwopi.jandi.client.GitClient
import javax.swing.*

class LoginDialog(project: Project) : DialogWrapper(project) {
    private val userTokenField = JPasswordField(32)

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
        val token = userTokenField.password.concatToString()
        try {
            GitClient.login(token)
            super.doOKAction()
        } catch (e : Exception) {
            setErrorText(e.message)
        }
    }
}