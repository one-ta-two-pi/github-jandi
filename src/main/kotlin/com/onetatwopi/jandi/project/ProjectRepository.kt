package com.onetatwopi.jandi.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

object ProjectRepository {

    private var project: Project? = null

    fun getProject(): Project {
        return project ?: throw RuntimeException("No Project Information")
    }


    fun setProject(project: Project) {
        this.project = project
    }

    fun getBaseDir(): VirtualFile {

        return ProjectRootManager.getInstance(getProject()).contentRoots.firstOrNull()
            ?: throw RuntimeException("No Project Directory")
    }

    fun getRemoteBranchList(): Array<String> {

        val project = project ?: throw RuntimeException("No Project Information")

        // 프로젝트 루트 디렉토리를 가져옴
        val projectRoots = ProjectRootManager.getInstance(project).contentRoots

        // 원격 브랜치 목록을 저장할 Set
        val branches = mutableSetOf<String>()

        // 각 루트 디렉토리에서 Git 저장소의 원격 브랜치 정보를 추출하여 Set에 추가
        for (root in projectRoots) {
            val gitDir = File(root.path, ".git")
            if (gitDir.exists() && gitDir.isDirectory) {
                val config = File(gitDir, "config")
                if (config.exists() && config.isFile) {
                    branches.addAll(getBranchesFromConfig(config))
                }
            }
        }
        return branches.toTypedArray()
    }

    private fun getBranchesFromConfig(config: File): Set<String> {
        val branches = mutableSetOf<String>()
        val lines = config.readLines()
        var inRemoteSection = false
        var remoteName = ""
        for (line in lines) {
            if (line.startsWith("[remote")) {
                remoteName = line.trim().substringAfter("[remote").substringBefore("]")
                inRemoteSection = true
            } else if (inRemoteSection && line.startsWith("\tfetch = ")) {
                val fetchPattern = Regex("""\+refs/heads/(.*?):""")
                val matchResult = fetchPattern.find(line)
                matchResult?.let {
                    val branchName = it.groupValues[1]
                    branches.add("${remoteName}:${branchName}")
                }
                inRemoteSection = false
            }
        }
        return branches
    }
}
