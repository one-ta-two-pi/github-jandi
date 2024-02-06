package com.onetatwopi.jandi.layout.panel.issue

import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.onetatwopi.jandi.layout.dto.IssueInfo
import com.onetatwopi.jandi.layout.panel.ContentPanel
import com.onetatwopi.jandi.layout.panel.MainPanelAdaptor
import java.awt.BorderLayout
import javax.swing.DefaultListModel

object IssuePanel : MainPanelAdaptor<IssueInfo>, ContentPanel("Issue") {

    private var issueInfoList: List<IssueInfo> = ArrayList()

    fun setIssueInfoList(newIssueInfoList: List<IssueInfo>) {
        this.issueInfoList = newIssueInfoList
    }

    override fun generateModel(): DefaultListModel<IssueInfo> {
        val models = DefaultListModel<IssueInfo>()

        for (issueInfo in issueInfoList) {
            models.addElement(issueInfo)
        }

        return models
    }

    override fun render() {
        val buttonList = JBList(generateModel())
        buttonList.cellRenderer = IssueButtons()
        val scrollPane = JBScrollPane(buttonList)

        panel.layout = BorderLayout()
        panel.add(scrollPane, BorderLayout.CENTER)
    }
}