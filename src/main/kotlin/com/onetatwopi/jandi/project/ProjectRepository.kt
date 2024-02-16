package com.onetatwopi.jandi.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.wm.ToolWindow
import java.io.File

object ProjectRepository {

    private var project: Project? = null
    private var mainToolWindow: ToolWindow? = null

    fun getProject(): Project {
        return project ?: throw RuntimeException("No Project Information")
    }

    fun setProject(project: Project) {
        this.project = project
    }

    fun getMainToolWindow(): ToolWindow {
        return mainToolWindow ?: throw RuntimeException("No ToolWindow")
    }

    fun setMainToolWindow(toolWindow: ToolWindow) {
        this.mainToolWindow = toolWindow
    }

    fun getLocalBranchList(): Array<String> {
        val project = project ?: throw RuntimeException("No Project Information")

        val projectRoots = ProjectRootManager.getInstance(project).contentRoots

        val branches = mutableSetOf<String>()

        for (root in projectRoots) {
            val remote = File(root.path, ".git/refs/remotes")
            if (remote.exists() && remote.isDirectory) {
                remote.listFiles()?.filter {
                    it.isDirectory
                }?.forEach { outer ->
                    outer.listFiles()?.filter {
                        !it.name.equals("HEAD")
                                && it.isFile
                    }?.forEach {
                        branches.add(it.name)
                    }
                }
            }
        }

        return branches.toTypedArray()
    }

    fun getRemoteBranchList(): Array<String> {

        val project = project ?: throw RuntimeException("No Project Information")

        val projectRoots = ProjectRootManager.getInstance(project).contentRoots

        val branches = mutableSetOf<String>()

        for (root in projectRoots) {
            val remote = File(root.path, ".git/refs/remotes")
            if (remote.exists() && remote.isDirectory) {
                remote.listFiles()?.filter {
                    it.isDirectory
                }?.forEach { outer ->
                    val remoteName = outer.name
                    outer.listFiles()?.filter {
                        !it.name.equals("HEAD")
                                && it.isFile
                    }?.forEach {
                        branches.add("${remoteName}:${it.name}")
                    }
                }
            }
        }
        return branches.toTypedArray()
    }
}
