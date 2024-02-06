package com.onetatwopi.jandi.layout.panel.pullRequest

import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import com.onetatwopi.jandi.layout.panel.ContentPanel
import com.onetatwopi.jandi.layout.panel.MainPanelAdaptor
import java.awt.BorderLayout
import javax.swing.DefaultListModel

object PullRequestPanel : MainPanelAdaptor<PullRequestInfo>, ContentPanel("Pull Request") {

    private var pullRequestInfoList: List<PullRequestInfo> = ArrayList()
    fun setPullRequestInfoList(newPullRequestInfoList: List<PullRequestInfo>) {
        pullRequestInfoList = newPullRequestInfoList
    }

    override fun generateModel(): DefaultListModel<PullRequestInfo> {
        val models = DefaultListModel<PullRequestInfo>()

        for (pullRequestInfo in pullRequestInfoList) {
            models.addElement(pullRequestInfo)
        }

        return models
    }

    override fun render() {
        val buttonList = JBList(generateModel())
        buttonList.cellRenderer = PullRequestButtons()
        val scrollPane = JBScrollPane(buttonList)

        panel.layout = BorderLayout()
        panel.add(scrollPane, BorderLayout.CENTER)
    }
}