package com.onetatwopi.jandi.layout.panel.issue

import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.notificationGroup
import com.intellij.util.ui.JBUI
import com.onetatwopi.jandi.layout.dto.IssueInfo
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Desktop
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
import java.time.format.DateTimeFormatter
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

class IssueButtons : ListCellRenderer<IssueInfo> {

    private val button = JPanel(BorderLayout())
    private val titleLabel = JLabel()
    private val openUserIdLabel = JLabel()
    private val openAtLabel = JLabel()
    private val statusLabel = JLabel()

    init {
        val topPanel = JPanel(BorderLayout())
        val bottomPanel = JPanel(BorderLayout())

        topPanel.isOpaque = false
        bottomPanel.isOpaque = false

        topPanel.add(titleLabel, BorderLayout.WEST)
        topPanel.add(openUserIdLabel, BorderLayout.EAST)
        bottomPanel.add(openAtLabel, BorderLayout.WEST)
        bottomPanel.add(statusLabel, BorderLayout.EAST)

        button.add(topPanel, BorderLayout.NORTH)
        button.add(bottomPanel, BorderLayout.SOUTH)

        button.border = JBUI.Borders.empty(5)
    }

    override fun getListCellRendererComponent(
        list: JList<out IssueInfo>?,
        value: IssueInfo?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean,
    ): Component {
        value?.let {
            titleLabel.text = it.title
            openUserIdLabel.text = it.createUserId
            openAtLabel.text = it.openAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            statusLabel.text = it.status
        }

        button.background = if (isSelected) list?.selectionBackground else list?.background
        button.foreground = if (isSelected) list?.selectionForeground else list?.foreground

        list?.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2 && !e.isConsumed) {
                    e.consume()
                    val selectedIndex = list.selectedIndex
                    if (selectedIndex != -1) {
                        value?.let {
                            Desktop.getDesktop().browse(URI(value.url))
                        } ?: run {
                            notificationGroup.createNotification("No url!", MessageType.WARNING)
                        }
                    }
                }
            }
        })

        return button
    }
}