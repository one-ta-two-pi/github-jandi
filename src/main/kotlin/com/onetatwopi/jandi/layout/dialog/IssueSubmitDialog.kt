import com.onetatwopi.jandi.data.issue.IssueService
import java.awt.*
import javax.swing.*

object IssueSubmitDialog {
    private val dialog = JDialog()
    private val titleLabel = generateLabel("Title")
    private val titleField = JTextField(20)
    private val bodyLabel = generateLabel("Detail")
    private val bodyField = JTextArea(2, 20)
    private val submitButton = JButton("Submit")

    private val issueService = IssueService()

    private const val WIDTH = 600
    private const val HEIGHT = 400

    init {
        dialog.title = "Issue"
        dialog.setSize(WIDTH, HEIGHT)
        dialog.layout = GridBagLayout()
        val stand = GridBagConstraints()

        stand.gridx = 0
        stand.gridy = 0
        stand.fill = GridBagConstraints.BOTH
        dialog.add(generatePanel(titleLabel), stand)

        stand.gridy++
        dialog.add(generatePanel(titleField), stand)

        stand.gridy++
        stand.fill = GridBagConstraints.BOTH
        dialog.add(generatePanel(bodyLabel), stand)

        stand.gridy++
        stand.fill = GridBagConstraints.BOTH
        dialog.add(generatePanel(JScrollPane(bodyField)), stand)

        stand.gridy++
        stand.fill = GridBagConstraints.BOTH
        dialog.add(submitButton, stand)

        dialog.setLocationRelativeTo(null)
        dialog.isAlwaysOnTop = true
        dialog.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

        submitButton.addActionListener {
//            ToDo: IssueService 연동
//            issueService.createIssue(
//                IssueSubmit(
//                    title = titleField.text,
//                    body=bodyField.text
//                )
//            )

            close()
        }
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

    private fun generatePanel(vararg components: Component): JPanel {
        val panel = JPanel()
        val layout = GridLayout(1, components.size)
        panel.layout = layout

        for (component in components) {
            panel.add(component)
        }

        // 각 컴포넌트가 최대 크기로 확장되도록 설정
        for (component in components) {
            component.maximumSize = Dimension(Integer.MAX_VALUE, component.preferredSize.height)
        }

        return panel
    }
}