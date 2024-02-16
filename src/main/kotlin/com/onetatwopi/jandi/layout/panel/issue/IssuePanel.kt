package com.onetatwopi.jandi.layout.panel.issue

import com.onetatwopi.jandi.data.issue.IssueService
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.onetatwopi.jandi.layout.dto.IssueInfo
import com.onetatwopi.jandi.layout.panel.ContentPanel
import com.onetatwopi.jandi.layout.panel.MainPanelAdaptor
import java.awt.BorderLayout
import javax.swing.DefaultListModel
import javax.swing.SwingUtilities

object IssuePanel : MainPanelAdaptor<IssueInfo>, ContentPanel("Issue") {

    private var issueInfoList: List<IssueInfo> = ArrayList()
    private var buttonList: JBList<IssueInfo> = JBList()

    init {
        generateModel()
    }

    private fun setIssueInfoList(newIssueInfoList: List<IssueInfo>) {
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
        setIssueInfoList(IssueService.parseIssueList())
    }
}