package com.onetatwopi.jandi.layout.notification

import com.intellij.notification.NotificationType
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.notificationGroup
import com.onetatwopi.jandi.project.ProjectRepository
import javax.swing.Timer

object NotificationFactory {

    fun notify(message: String, type: NotificationType){
        val notification = notificationGroup.createNotification(message, type)

        val timer = Timer(3000){
            notification.expire()
        }

        timer.isRepeats = false
        timer.start()
        notification.notify(ProjectRepository.getProject())
    }
}