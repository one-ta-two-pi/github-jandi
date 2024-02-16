package com.onetatwopi.jandi.login

import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.onetatwopi.jandi.layout.notification.NotificationFactory

class LoginActivity {
    fun run(project: Project) {
        val dialog = LoginDialog(project)
        dialog.show()

        if (dialog.isOK) {
            NotificationFactory.notify("Login success!", NotificationType.INFORMATION)
        } else {
            return
        }
    }
}