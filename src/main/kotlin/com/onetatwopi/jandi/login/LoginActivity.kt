package com.onetatwopi.jandi.login

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.Messages
import com.onetatwopi.jandi.client.GitClient

class LoginActivity : StartupActivity {

    override fun runActivity(project: Project) {
        while(GitClient.loginId == null) {
            val dialog = LoginDialog(project)
            dialog.show()

            if (dialog.isOK) {
                Messages.showMessageDialog("로그인 성공!", "Information", Messages.getInformationIcon())
            } else {
                return
            }
        }
    }
}