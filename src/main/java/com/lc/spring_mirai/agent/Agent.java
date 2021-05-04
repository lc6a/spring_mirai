package com.lc.agent;

import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

/**
 * 代理类
 * premain方法和agentmain方法一次只会调用其中一个，由于业务类似，由work方法统一处理
 *
 * 详细说明（https://www.runoob.com/manual/jdk11api/java.instrument/java/lang/instrument/package-summary.html）：
 * Package java.lang.instrument
 * 提供允许Java编程语言代理程序检测在JVM上运行的程序的服务。 检测机制是修改方法的字节码。
 * 代理程序部署为JAR文件。 JAR文件清单中的属性指定将加载以启动代理的代理类。 可以通过多种方式启动代理：
 *
 * 对于支持命令行界面的实现，可以通过在命令行上指定选项来启动代理。
 *
 * 实现可以支持在VM启动后的某个时间启动代理的机制。 例如，实现可以提供允许工具附加到正在运行的应用程序的机制，并且启动将工具的代理加载到正在运行的应用程序中。
 *
 * 代理可以与应用程序一起打包在可执行的JAR文件中。
 *
 * 下面描述了启动代理的这些方法中的每一种。
 *
 * 从命令行界面启动代理
 * 如果实现提供了从命令行界面启动代理的方法，则通过在命令行中添加以下选项来启动代理：
 *
 * -javaagent:<jarpath>[=<options>]
 * 其中<jarpath>是代理JAR文件的路径， <options>是代理选项。
 * 代理JAR文件的清单必须在其主清单中包含属性Premain-Class 。 此属性的值是代理类的名称。 代理类必须实现与main应用程序入口点原则上类似的公共静态premain方法。 在Java虚拟机（JVM）初始化之后，将调用premain方法，然后是真正的应用程序main方法。 必须返回premain方法才能继续启动。
 *
 * premain方法具有两种可能的签名之一。 JVM首先尝试在代理类上调用以下方法：
 *
 * public static void premain(String agentArgs, Instrumentation inst)
 * 如果代理类未实现此方法，则JVM将尝试调用：
 *
 * public static void premain(String agentArgs)
 * 在VM启动后启动代理时，代理类也可以使用agentmain方法（参见下文）。 使用命令行选项启动代理程序时，不会调用agentmain方法。
 *
 * 每个代理程序都通过agentArgs参数传递其代理程序选项。 代理选项作为单个字符串传递，任何其他解析应由代理本身执行。
 *
 * 如果无法启动代理（例如，因为无法加载代理类，或者因为代理类没有适当的premain方法），JVM将中止。 如果premain方法抛出未捕获的异常，则JVM将中止。
 *
 * 不需要实现来提供从命令行界面启动代理的方法。 如果是，则它支持上面指定的-javaagent选项。 -javaagent选项可以在同一命令行中多次使用，从而启动多个代理。 将按照在命令行上指定代理程序的顺序调用premain方法。 不止一个代理商可能使用相同的<jarpath> 。
 *
 * 代理premain方法可能没有建模限制。 应用程序main可以执行的任何操作（包括创建线程）都是合法的，来自premain 。
 *
 * VM启动后启动代理
 * 实现可以提供在VM启动之后的某个时间启动代理的机制。 有关如何启动的详细信息是特定于实现的，但通常应用程序已经启动并且已经调用了其main方法。 如果实施支持在VM启动后启动代理，则以下情况适用：
 *
 * 代理JAR的清单必须包含属性Agent-Class在其主manifest。 此属性的值是代理类的名称。
 *
 * 代理类必须实现public static agentmain方法。
 *
 * agentmain方法具有两种可能的签名之一。 JVM首先尝试在代理类上调用以下方法：
 *
 * public static void agentmain(String agentArgs, Instrumentation inst)
 * 如果代理类未实现此方法，则JVM将尝试调用：
 *
 * public static void agentmain(String agentArgs)
 * 代理类还可以具有premain方法，以便在使用命令行选项启动代理程序时使用。 在VM启动后启动代理程序时，不会调用premain方法。
 *
 * 代理程序通过agentArgs参数传递其代理程序选项。 代理选项作为单个字符串传递，任何其他解析应由代理本身执行。
 *
 * agentmain方法应该执行启动代理所需的任何必要初始化。 启动完成后，该方法应返回。 如果无法启动代理（例如，因为无法加载代理类，或者因为代理类没有符合条件的agentmain方法），JVM将不会中止。 如果agentmain方法抛出未捕获的异常，它将被忽略（但可能由JVM记录以进行故障排除）。
 *
 * 在可执行JAR文件中包含代理
 * JAR文件规范定义了打包为可执行JAR文件的独立应用程序的清单属性。 如果实现支持将应用程序作为可执行JAR启动的机制，则主清单可以包括Launcher-Agent-Class属性，以指定在调用应用程序main方法之前要启动的代理的类名。 Java虚拟机尝试在代理类上调用以下方法：
 *
 * public static void agentmain(String agentArgs, Instrumentation inst)
 * 如果代理类未实现此方法，则JVM将尝试调用：
 *
 * public static void agentmain(String agentArgs)
 * agentArgs参数的值始终为空字符串。
 *
 * agentmain方法应该执行启动代理和返回所需的任何必要的初始化。 如果无法启动代理程序，例如无法加载代理程序类，则代理程序类不定义符合条件的agentmain方法，或者agentmain方法抛出未捕获的异常或错误，JVM将中止。
 *
 * 加载代理程序类以及代理程序类可用的模块/类
 * 从代理JAR文件加载的类由system class loader加载，并且是系统类加载器unnamed module的成员 。 系统类加载器通常也定义包含应用程序main方法的类。
 *
 * 代理类可见的类是系统类加载器可见的类，最低限度包括：
 *
 * boot layer中模块导出的包中的类 。 引导层是否包含所有平台模块取决于初始模块或应用程序的启动方式。
 *
 * 可以由系统类加载器（通常是类路径）定义的类，作为其未命名模块的成员。
 *
 * 代理安排由引导类加载器定义为其未命名模块的成员的任何类。
 *
 * 如果代理类需要链接到不在引导层中的平台（或其他）模块中的类，则可能需要以确保这些模块位于引导层中的方式启动应用程序。 例如，在JDK实现中，可以使用--add-modules命令行选项将模块添加到要在启动时解析的根模块集。
 *
 * 代理程序安排由引导类加载器加载的支持类（通过appendToBootstrapClassLoaderSearch或下面指定的Boot-Class-Path属性）必须仅链接到定义到引导程序类加载器的类。 无法保证引导类加载器可以定义所有平台类。
 *
 * 如果自定义系统类加载器被配置（由系统属性的手段java.system.class.loader作为指定getSystemClassLoader方法），那么它必须定义appendToClassPathForInstrumentation如在指定的方法appendToSystemClassLoaderSearch 。 换句话说，自定义系统类加载器必须支持将代理JAR文件添加到系统类加载器搜索的机制。
 *
 * 清单属性
 * 为代理JAR文件定义了以下清单属性：
 *
 * Premain-Class
 * When an agent is specified at JVM launch time this attribute specifies the agent class. That is, the class containing the premain method. When an agent is specified at JVM launch time this attribute is required. If the attribute is not present the JVM will abort. Note: this is a class name, not a file name or path.
 * Agent-Class
 * If an implementation supports a mechanism to start agents sometime after the VM has started then this attribute specifies the agent class. That is, the class containing the agentmain method. This attribute is required if it is not present the agent will not be started. Note: this is a class name, not a file name or path.
 * Launcher-Agent-Class
 * If an implementation supports a mechanism to start an application as an executable JAR then the main manifest may include this attribute to specify the class name of an agent to start before the application main method is invoked.
 * Boot-Class-Path
 * A list of paths to be searched by the bootstrap class loader. Paths represent directories or libraries (commonly referred to as JAR or zip libraries on many platforms). These paths are searched by the bootstrap class loader after the platform specific mechanisms of locating a class have failed. Paths are searched in the order listed. Paths in the list are separated by one or more spaces. A path takes the syntax of the path component of a hierarchical URI. The path is absolute if it begins with a slash character ('/'), otherwise it is relative. A relative path is resolved against the absolute path of the agent JAR file. Malformed and non-existent paths are ignored. When an agent is started sometime after the VM has started then paths that do not represent a JAR file are ignored. This attribute is optional.
 * Can-Redefine-Classes
 * Boolean ( true or false, case irrelevant). Is the ability to redefine classes needed by this agent. Values other than true are considered false. This attribute is optional, the default is false.
 * Can-Retransform-Classes
 * Boolean ( true or false, case irrelevant). Is the ability to retransform classes needed by this agent. Values other than true are considered false. This attribute is optional, the default is false.
 * Can-Set-Native-Method-Prefix
 * Boolean ( true or false, case irrelevant). Is the ability to set native method prefix needed by this agent. Values other than true are considered false. This attribute is optional, the default is false.
 * 代理JAR文件可能同时具有Premain-Class和Agent-Class属性。 使用-javaagent选项在命令行上启动代理程序时， Premain-Class属性指定代理程序类的名称，并忽略Agent-Class属性。 同样，如果代理在VM启动后的某个时间启动，则Agent-Class属性指定代理类的名称（忽略值Premain-Class属性）。
 */
public class Agent {
    public static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instrumentation) {
        work(args, instrumentation);
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        work(args, instrumentation);
    }

    private static void work(String args, Instrumentation instrumentation) {
        Agent.instrumentation = instrumentation;
    }

    public static synchronized void appendJarFile(JarFile file) {
        if (instrumentation != null) {
            instrumentation.appendToSystemClassLoaderSearch(file);
        }
    }
}
