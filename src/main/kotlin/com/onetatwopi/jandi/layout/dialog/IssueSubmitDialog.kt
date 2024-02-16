import com.intellij.openapi.ui.Messages
import com.onetatwopi.jandi.data.issue.IssueService
import com.onetatwopi.jandi.layout.dto.IssueSubmit
import com.onetatwopi.jandi.layout.panel.issue.IssuePanel
import java.awt.*
import javax.swing.*

object IssueSubmitDialog {
    private val dialog = JDialog()
    private val titleLabel = generateLabel("Title")
    private val titleField = JTextField(20)
    private val bodyLabel = generateLabel("Detail")
    private val bodyField = JTextArea(5, 20)
    private val milestoneLabel = JLabel("Milestone")
    private val milestoneField = JTextField()
    private val submitButton = JButton("Submit")

    private const val WIDTH = 400
    private const val HEIGHT = 250

    init {
        dialog.title = "Create Issue"
        dialog.setSize(WIDTH, HEIGHT)
        dialog.layout = GridBagLayout()

        val font = Font("Arial", Font.PLAIN, 14) // Custom font

        titleLabel.font = font
        addComponentToDialog(titleLabel, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL)

        titleField.font = font
        titleField.toolTipText = "Enter the issue title here"
        addComponentToDialog(titleField, 1, 0, 2, 1, GridBagConstraints.HORIZONTAL)

        bodyLabel.font = font
        addComponentToDialog(bodyLabel, 0, 1, 1, 1, GridBagConstraints.HORIZONTAL)

        bodyField.font = font
        bodyField.toolTipText = "Enter the issue details here"
        addComponentToDialog(JScrollPane(bodyField), 1, 1, 2, 1, GridBagConstraints.BOTH)

        milestoneLabel.font = font
        addComponentToDialog(milestoneLabel, 0, 2, 1, 1, GridBagConstraints.HORIZONTAL)

        milestoneField.font = font
        milestoneField.toolTipText = "Enter the milestone here"
        addComponentToDialog(milestoneField, 1, 2, 2, 1, GridBagConstraints.HORIZONTAL)

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
        addComponentToDialog(submitButton, 0, 3, 3, 1, GridBagConstraints.HORIZONTAL)

        dialog.setLocationRelativeTo(null)
        dialog.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
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

    private fun init() {
        titleField.text = ""
        bodyField.text = ""
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

    private fun generateLabel(text: String): JLabel {
        val label = JLabel(text)
        label.verticalAlignment = SwingConstants.CENTER
        label.horizontalAlignment = SwingConstants.CENTER
        return label
    }
}
