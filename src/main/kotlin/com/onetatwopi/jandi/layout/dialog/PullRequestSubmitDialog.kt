package com.onetatwopi.jandi.layout.dialog

import com.intellij.openapi.ui.ComboBox
import com.onetatwopi.jandi.project.ProjectRepository
import java.awt.Component
import java.awt.GridLayout
import javax.swing.*

object PullRequestSubmitDialog {
    private val dialog = JDialog()
    private val titleLabel = generateLabel("Title")
    private val titleField = JTextField(20)
    private val bodyLabel = generateLabel("Detail")
    private val bodyField = JTextArea(2, 20)
    private val headLabel = generateLabel("Head")
    private val headComboBox = ComboBox(ProjectRepository.getRemoteBranchList())
    private val baseLabel = generateLabel("Base")
    private val baseComboBox = ComboBox(ProjectRepository.getLocalBranchList())
    private val submitButton = JButton("Submit")

    //private val pullRequestService = PullRequestService()

    init {
        dialog.title = "Pull Request"
        dialog.layout = GridLayout(6, 1)

        dialog.add(generatePanel(titleLabel, titleField))
        dialog.add(generatePanel(headLabel, headComboBox))
        dialog.add(generatePanel(baseLabel, baseComboBox))
        dialog.add(generatePanel(bodyLabel))
        dialog.add(generatePanel(JScrollPane(bodyField)))
        dialog.add(submitButton)

        submitButton.addActionListener {
            //ToDo: Pull Request Create
//            pullRequestService.createPullRequest(
//                PullRequestSubmit(
//                    title = titleField.text,
//                    body = bodyField.text,
//                    head = headComboBox.selectedItem?.toString() ?: "",
//                    base = baseComboBox.selectedItem?.toString() ?: ""
//                )
//            )
            close()
        }

        dialog.setSize(600, 360)
        dialog.setLocationRelativeTo(null)
        dialog.isAlwaysOnTop = true
        dialog.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    }

    private fun generateLabel(text: String): JLabel {
        val label = JLabel(text)
        label.verticalAlignment = SwingConstants.CENTER
        label.horizontalAlignment = SwingConstants.CENTER
        return label
    }

    private fun generatePanel(vararg components: Component): JPanel {
        val panel = JPanel()

        panel.layout = GridLayout(1, components.size)

        for (component in components) {
            panel.add(component)
        }
        return panel
    }

    private fun init() {
        titleField.text = ""
        bodyField.text = ""
        headComboBox.selectedIndex = -1
        baseComboBox.selectedIndex = -1
    }

    fun show() {
        if (!this.dialog.isShowing) {
            init()
            this.dialog.isVisible = true
        }
    }

    private fun close() {
        if (this.dialog.isShowing) {
            this.dialog.isVisible = false
        }
    }
}