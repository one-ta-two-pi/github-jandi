package com.onetatwopi.jandi.layout.panel.issue

import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.onetatwopi.jandi.layout.dto.IssueInfo
import com.onetatwopi.jandi.layout.mockIssue
import com.onetatwopi.jandi.layout.panel.ContentPanel
import com.onetatwopi.jandi.layout.panel.MainPanelAdaptor
import kotlinx.serialization.json.*
import java.awt.BorderLayout
import javax.swing.DefaultListModel
import javax.swing.SwingUtilities

object IssuePanel : MainPanelAdaptor<IssueInfo>, ContentPanel("Issue") {

    private var issueInfoList: List<IssueInfo> = ArrayList()
    private var buttonList: JBList<IssueInfo> = JBList()

    init {
        generateModel()
    }

    fun setIssueInfoList(newIssueInfoList: List<IssueInfo>) {
        this.issueInfoList = newIssueInfoList
        generateModel()
    }

    override fun generateModel() {
        val models = DefaultListModel<IssueInfo>()

        for (issueInfo in issueInfoList) {
            models.addElement(issueInfo)
        }

        this.buttonList.model = models
    }

    override fun render() {
        SwingUtilities.invokeLater {
            buttonList.cellRenderer = IssueButtons()
            val scrollPane = JBScrollPane(buttonList)

            panel.layout = BorderLayout()
            panel.add(scrollPane, BorderLayout.CENTER)
            panel.revalidate()
        }
    }

    override fun refresh() {
        if (this.issueInfoList.isEmpty()) {
            val jsonIssues = Json.parseToJsonElement(mockIssue).jsonArray

            val issues = mutableListOf<IssueInfo>()
            for (i in 0 until jsonIssues.size) {
                val jsonIssue = jsonIssues[i].jsonObject
                issues.add(generateIssue(jsonIssue))
            }

            setIssueInfoList(issues)
        } else {
            this.setIssueInfoList(ArrayList())
        }
    }

    private fun generateIssue(jsonIssue: JsonObject) = IssueInfo(
        title = jsonIssue["title"]?.jsonPrimitive?.contentOrNull ?: "",
        createUserId = jsonIssue["user"]?.jsonObject?.get("login")?.jsonPrimitive?.contentOrNull ?: "",
        url = jsonIssue["url"]?.jsonPrimitive?.contentOrNull ?: "",
        status = jsonIssue["state"]?.jsonPrimitive?.contentOrNull ?: "",
        openAt = jsonIssue["created_at"]?.jsonPrimitive?.contentOrNull ?: "",
        closeAt = jsonIssue["closed_at"]?.jsonPrimitive?.contentOrNull ?: ""
    )
}