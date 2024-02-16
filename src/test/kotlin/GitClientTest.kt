import com.intellij.util.net.HTTPMethod
import com.onetatwopi.jandi.client.Category
import com.onetatwopi.jandi.client.GitClient
import org.apache.http.message.BasicNameValuePair
import org.junit.Test

class GitClientTest {
    @Test
    fun `로그인 테스트`() {
//        GitClient.login("")
        println(GitClient.loginId)
    }

    @Test
    fun `외부 api 테스트`() {
        GitClient.login("")
        println(GitClient.repoRequest(method = HTTPMethod.GET, repo = GitClient.repos[1], category = Category.ISSUE, number = 1))
        println(GitClient.repoRequest(method = HTTPMethod.POST, repo = GitClient.repos[0], category = Category.ISSUE, body =
        listOf(BasicNameValuePair("title", "find a bug"))))
        println(GitClient.repoRequest(method = HTTPMethod.PATCH, repo = GitClient.repos[0], category = Category.PULL, number = 7,
            body = listOf(BasicNameValuePair("title", "test pr"), BasicNameValuePair("base", "master"))
        ))
        // String으로 반환하기 때문에 여기서 Gson으로 파싱하여 사용할 것 권장, object Mapper 써도 되지만 Gson이 더 나을듯?...
    }


}