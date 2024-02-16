package com.onetatwopi.jandi.login

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class LoginActivity {
    fun run(project: Project) {
        val dialog = LoginDialog(project)
        dialog.show()

        if (dialog.isOK) {
            Messages.showMessageDialog("로그인 성공!", "Information", Messages.getInformationIcon())
        } else {
            return
        }
    }
}