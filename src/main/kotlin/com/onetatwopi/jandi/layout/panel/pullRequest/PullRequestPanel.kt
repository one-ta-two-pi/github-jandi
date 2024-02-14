package com.onetatwopi.jandi.layout.panel.pullRequest

import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.onetatwopi.jandi.data.pullRequest.PullRequestService
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
    private var pullRequestService: PullRequestService

    init {
        generateModel()
        pullRequestService = PullRequestService()
    }

    private fun setPullRequestInfoList(newPullRequestInfoList: List<PullRequestInfo>) {
        pullRequestInfoList = newPullRequestInfoList
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
        if (pullRequestInfoList.isEmpty()) {
            setPullRequestInfoList(
                listOf(
                    PullRequestInfo(
                        "Pull Request 1",
                        "User1",
                        "Open",
                        "https://www.naver.com",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    ),
                    PullRequestInfo(
                        "Pull Request 2",
                        "User2",
                        "Closed",
                        "https://www.naver.com",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    ),
                    PullRequestInfo(
                        "Pull Request 3",
                        "User3",
                        "In Progress",
                        "https://www.naver.com",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    )
                )
            )
        } else {
            // ToDo: Pull Request 정보 불러오기
            setPullRequestInfoList(ArrayList())
            // setPullRequestInfoList(pullRequestService.getPullRequestList("token", Pair("repoOwner", "repoName")))
        }
        // ToDo: Pull Request 정보 불러오기
        // setPullRequestInfoList(pullRequestService.getPullRequestList("token", Pair("repoOwner", "repoName")))
    }
}