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
        if (pullRequestInfoList.isEmpty()) {
            setPullRequestInfoList(
                listOf(
                    PullRequestInfo(
                        "Pull Request 1",
                        "User1",
                        "Open",
                        "https://www.naver.com",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        PullRequestDetailInfo(
                            number = 1,
                            title = "detail1",
                            requestUserId = "id1",
                            status = "Open",
                            url = "https://www.naver.com",
                            body = "body1",
                            createdAt = "2024-02-15"
                        )
                    ),
                    PullRequestInfo(
                        "Pull Request 2",
                        "User2",
                        "Closed",
                        "https://www.duam.net",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        PullRequestDetailInfo(
                            number = 2,
                            title = "detail2",
                            requestUserId = "id2",
                            status = "Closed",
                            url = "https://www.naver.com",
                            body = "body2",
                            createdAt = "2024-02-15"
                        )
                    ),
                    PullRequestInfo(
                        "Pull Request 3",
                        "User3",
                        "In Progress",
                        "https://www.google.com",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        PullRequestDetailInfo(
                            number = 3,
                            title = "detail3",
                            requestUserId = "id3",
                            status = "In Progress",
                            url = "https://www.naver.com",
                            body = "body3",
                            createdAt = "2024-02-15"
                        )
                    )
                )
            )
        } else {
            setPullRequestInfoList(PullRequestService.instance.parsePullRequestList())
        }
        setPullRequestInfoList(PullRequestService.instance.parsePullRequestList())
    }
}