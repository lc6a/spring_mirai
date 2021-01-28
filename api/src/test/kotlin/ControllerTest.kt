import com.lc.spring_mirai.controller.ConfigOfController
import com.lc.spring_mirai.controller.ControllerFactory
import com.lc.spring_mirai.controller.annotation.Mapping
import org.junit.Test


@Mapping
class ControllerTest {
    @Mapping("abc/{a}/[.]")
    suspend fun testMapping(a: Long, b: String){}

    @Test
    fun test() {
        val ctrl = ControllerFactory.createControllerClass(ControllerTest(), ConfigOfController())
        require(ctrl.isController())
        require(ctrl.functions.size == 1)
        require(ctrl.functions[0].params.size == 3)
        require(ctrl.functions[0].mappedItems.size == 3)
        val mapped = ctrl.functions[0].mappedItems
    }


    @Test
    fun regexTest(){
        val regex = """^\{\w+}$""".toRegex()
        require("{a}".matches(regex))
        require("{abc123ABC}".matches(regex))
        require(!"{}".matches(regex))
        require(!"{+}".matches(regex))
        require(!"{a".matches(regex))
        val str = "{abc}"
        require(str.substring(1, str.length - 1) == "abc")
    }

}
