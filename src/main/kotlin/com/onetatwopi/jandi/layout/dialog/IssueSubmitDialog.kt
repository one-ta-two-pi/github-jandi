package com.onetatwopi.jandi.layout.dialog

import com.intellij.openapi.ui.Messages
import com.onetatwopi.jandi.data.issue.IssueService
import com.onetatwopi.jandi.layout.dto.IssueSubmit
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import java.awt.*
import javax.swing.*

object IssueSubmitDialog {
    private val dialog = JDialog()

    private const val WIDTH = 400
    private const val HEIGHT = 250

    init {
        initializeDialog()
        initializeComponents()
    }

    private fun initializeDialog() {
        dialog.title = "Create Issue"
        dialog.setSize(WIDTH, HEIGHT)
        dialog.layout = GridBagLayout()
        dialog.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        dialog.setLocationRelativeTo(null)
    }

    private fun initializeComponents() {
        val font = Font("Arial", Font.PLAIN, 14) // Custom font

        val titleLabel = generateLabel("Title")
        val titleField = generateTextField(20, "Enter the issue title here")

        val bodyLabel = generateLabel("Detail")
        val bodyField = generateTextArea(5, 20, "Enter the issue details here")

        val milestoneLabel = generateLabel("Milestone")
        val milestoneField = generateTextField(10, "Enter the milestone here")

        val submitButton = JButton("Submit")
        submitButton.font = font
        submitButton.addActionListener {
            val issueSubmit = IssueSubmit(
                title = titleField.text,
                body = bodyField.text,
                milestone = milestoneField.text
            )

            if (!validInput(issueSubmit)) {
                return@addActionListener
            }

            IssueService.instance.createIssue(issueSubmit)
            close()
            IssuePanel.refresh()
        }

        addComponentToDialog(titleLabel, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL)
        addComponentToDialog(titleField, 1, 0, 2, 1, GridBagConstraints.HORIZONTAL)
        addComponentToDialog(bodyLabel, 0, 1, 1, 1, GridBagConstraints.HORIZONTAL)
        addComponentToDialog(JScrollPane(bodyField), 1, 1, 2, 1, GridBagConstraints.BOTH)
        addComponentToDialog(milestoneLabel, 0, 2, 1, 1, GridBagConstraints.HORIZONTAL)
        addComponentToDialog(milestoneField, 1, 2, 2, 1, GridBagConstraints.HORIZONTAL)
        addComponentToDialog(submitButton, 0, 3, 3, 1, GridBagConstraints.HORIZONTAL)
    }

    private fun generateTextField(columns: Int, toolTip: String): JTextField {
        val textField = JTextField(columns)
        textField.font = Font("Arial", Font.PLAIN, 14)
        textField.toolTipText = toolTip
        return textField
    }

    private fun generateTextArea(rows: Int, columns: Int, toolTip: String): JTextArea {
        val textArea = JTextArea(rows, columns)
        textArea.font = Font("Arial", Font.PLAIN, 14)
        textArea.toolTipText = toolTip
        return textArea
    }

    private fun generateLabel(text: String): JLabel {
        val label = JLabel(text)
        label.verticalAlignment = SwingConstants.CENTER
        label.horizontalAlignment = SwingConstants.CENTER
        label.font = Font("Arial", Font.PLAIN, 14)
        return label
    }

    private fun addComponentToDialog(
        component: Component,
        gridX: Int,
        gridY: Int,
        gridWidth: Int,
        gridHeight: Int,
        fill: Int
    ) {
        val gbc = GridBagConstraints()
        gbc.gridx = gridX
        gbc.gridy = gridY
        gbc.gridwidth = gridWidth
        gbc.gridheight = gridHeight
        gbc.insets = Insets(5, 5, 5, 5)
        gbc.fill = fill
        gbc.weightx = 1.0 // Ensure components expand horizontally
        gbc.weighty = 1.0 // Ensure components expand vertically

        dialog.add(component, gbc)
    }

    private fun validInput(issueSubmit: IssueSubmit): Boolean {
        val (title, body, milestone) = issueSubmit

        if (title.isBlank()) {
            Messages.showMessageDialog("제목을 입력해주세요.", "Error", Messages.getErrorIcon())
            return false
        }

        if (body.isBlank()) {
            Messages.showMessageDialog("내용을 입력해주세요.", "Error", Messages.getErrorIcon())
            return false
        }

        try {
            if (milestone.isNotBlank()) {
                milestone.toInt()
            }
        } catch (e: NumberFormatException) {
            Messages.showMessageDialog("마일스톤은 숫자여야 합니다.", "Error", Messages.getErrorIcon())
            return false
        }

        return true
    }

    private fun clearTextComponents(container: Container) {
        for (component in container.components) {
            when (component) {
                is JTextField -> component.text = ""
                is JTextArea -> component.text = ""
                is JScrollPane -> clearTextComponents(component)
                is Container -> clearTextComponents(component)
            }
        }
    }

    fun show() {
        if (!dialog.isShowing) {
            clearTextComponents(dialog)
            dialog.isVisible = true
            dialog.paintAll(dialog.graphics)
        }
    }

    private fun close() {
        if (dialog.isShowing) {
            dialog.isVisible = false
        }
    }

    fun isShowing(): Boolean {
        return this.dialog.isShowing
    }
}
