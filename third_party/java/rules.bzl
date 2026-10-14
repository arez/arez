load("@rules_java//java:defs.bzl", _java_binary = "java_binary", _java_library = "java_library", _java_plugin = "java_plugin", _java_test = "java_test")

# Preserve the existing javac policy while establishing the migration baseline.
_JAVACOPTS = [
    "--release",
    "17",
    "-Werror",
    "-Xlint:all,-processing,-serial,-this-escape",
    "-Xmaxerrs",
    "10000",
    "-Xmaxwarns",
    "10000",
    "-XepDisableAllChecks",
]
FORMATTER_JVM_FLAGS = [
    "--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
    "--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
    "--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
    "--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
    "--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
    "--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
]

def java_library(name, javacopts = [], **kwargs):
    _java_library(name = name, javacopts = _JAVACOPTS + javacopts, **kwargs)

def java_plugin(name, javacopts = [], **kwargs):
    _java_plugin(name = name, javacopts = _JAVACOPTS + javacopts, **kwargs)

def java_binary(name, javacopts = [], **kwargs):
    _java_binary(name = name, exclude_build_data = True, javacopts = _JAVACOPTS + javacopts, **kwargs)

def java_test(name, javacopts = [], jvm_flags = [], **kwargs):
    _java_test(name = name, exclude_build_data = True, javacopts = _JAVACOPTS + javacopts, jvm_flags = ["-ea"] + jvm_flags, **kwargs)

def java_testng(name, srcs, test_classes = [], test_class = None, test_methods = [], deps = [], runtime_deps = [], jvm_flags = [], javacopts = [], **kwargs):
    if test_class and test_classes:
        fail("Specify only one of test_class or test_classes")
    if test_class:
        test_classes = [test_class]
    if not test_classes:
        fail("Specify test_class or test_classes")
    java_test(
        name = name,
        srcs = srcs,
        use_testrunner = False,
        main_class = "org.testng.TestNG",
        args = ["-testclass", ",".join(test_classes), "-verbose", "2"] + (["-methods", ",".join(test_methods)] if test_methods else []),
        deps = ["//third_party/java:testng"] + deps if srcs else [],
        runtime_deps = runtime_deps + (["//third_party/java:testng"] + deps if not srcs else []),
        javacopts = javacopts,
        jvm_flags = jvm_flags,
        **kwargs
    )
