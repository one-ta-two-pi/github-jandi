package com.onetatwopi.jandi.data

import com.intellij.ide.DataManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager


class RepositoryParser {
    companion object {
        fun getOpenedRepository(project: Project): Pair<String, String> {
            val projectBaseDir = ProjectRootManager.getInstance(project).contentRoots.firstOrNull()
                ?: throw RuntimeException("유효한 프로젝트 디렉토리가 존재하지 않습니다.")

            val gitConfigPath = "${projectBaseDir.path}/.git/config"
            val virtualFile = LocalFileSystem.getInstance().findFileByPath(gitConfigPath)

            if (virtualFile == null || !virtualFile.exists()) {
                throw RuntimeException(".git/config 파일을 찾을 수 없습니다.")
            }

            val content = String(virtualFile.contentsToByteArray())
            val pattern = Regex("https://github.com/(.+)/(.+)")
            val matchResult = pattern.find(content) ?: throw RuntimeException("Repository 형식을 찾을 수 없습니다.")

            val user = matchResult.groupValues[1]
            val repository = matchResult.groupValues[2].replace(".git", "")
            return Pair(user, repository)
        }
    }
}