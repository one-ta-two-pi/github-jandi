package com.onetatwopi.jandi.layout.panel.pullRequest

import com.intellij.util.ui.JBUI
import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import java.awt.BorderLayout
import java.awt.Component
import java.time.format.DateTimeFormatter
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

class PullRequestButtons : ListCellRenderer<PullRequestInfo> {
    private val button = JPanel(BorderLayout())
    private val titleLabel = JLabel()
    private val requestUserLabel = JLabel()
    private val createdAtLabel = JLabel()
    private val statusLabel = JLabel()

    init {
        val topPanel = JPanel(BorderLayout())
        val bottomPanel = JPanel(BorderLayout())

        topPanel.isOpaque = false
        bottomPanel.isOpaque = false

        topPanel.add(titleLabel, BorderLayout.WEST)
        topPanel.add(requestUserLabel, BorderLayout.EAST)
        bottomPanel.add(createdAtLabel, BorderLayout.WEST)
        bottomPanel.add(statusLabel, BorderLayout.EAST)

        button.add(topPanel, BorderLayout.NORTH)
        button.add(bottomPanel, BorderLayout.SOUTH)

        button.border = JBUI.Borders.empty(5)
    }

    override fun getListCellRendererComponent(
        list: JList<out PullRequestInfo>?,
        value: PullRequestInfo?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        value?.let {
            titleLabel.text = it.name
            requestUserLabel.text = it.requestUserId
            createdAtLabel.text = it.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            statusLabel.text = it.status
        }

        button.background = if (isSelected) list?.selectionBackground else list?.background
        button.foreground = if (isSelected) list?.selectionForeground else list?.foreground

        return button
    }
}