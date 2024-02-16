package com.onetatwopi.jandi.layout.panel.pullRequest

import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.onetatwopi.jandi.data.pullRequest.PullRequestService
import com.onetatwopi.jandi.layout.dto.PullRequestDetailInfo
import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import com.onetatwopi.jandi.layout.panel.ContentPanel
import com.onetatwopi.jandi.layout.panel.MainPanelAdaptor
import java.awt.BorderLayout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.DefaultListModel
import javax.swing.SwingUtilities

object PullRequestPanel : MainPanelAdaptor<PullRequestInfo>, ContentPanel("Pull Request") {

    private var pullRequestInfoList: List<PullRequestInfo> = ArrayList()
    private var buttonList: JBList<PullRequestInfo> = JBList()

    init {
        generateModel()
    }

    private fun setPullRequestInfoList(newPullRequestInfoList: List<PullRequestInfo>) {
        this.pullRequestInfoList = newPullRequestInfoList
        generateModel()
    }

    override fun generateModel() {
        val models = DefaultListModel<PullRequestInfo>()

        for (pullRequestInfo in pullRequestInfoList) {
            models.addElement(pullRequestInfo)
        }

        buttonList.model = models
    }

    override fun render() {
        SwingUtilities.invokeLater {
            buttonList.cellRenderer = PullRequestButtons()
            val scrollPane = JBScrollPane(buttonList)

            panel.layout = BorderLayout()
            panel.add(scrollPane, BorderLayout.CENTER)
            panel.revalidate()
        }
    }

    override fun refresh() {
        setPullRequestInfoList(PullRequestService.instance.parsePullRequestList())
    }
}