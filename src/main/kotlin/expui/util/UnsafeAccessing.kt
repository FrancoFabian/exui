package expui.util

import java.lang.invoke.MethodHandles
import java.lang.invoke.VarHandle
import java.lang.reflect.AccessibleObject

internal object UnsafeAccessing {
    private val lookup: MethodHandles.Lookup = MethodHandles.lookup()

    private val isAccessibleFieldHandle: VarHandle? by lazy {
        try {
            val field = AccessibleObject::class.java.getDeclaredField("override")
            MethodHandles.privateLookupIn(AccessibleObject::class.java, lookup)
                .unreflectVarHandle(field)
        } catch (e: Throwable) {
            null
        }
    }

    val desktopModule by lazy {
        ModuleLayer.boot().findModule("java.desktop").get()
    }

    val ownerModule by lazy {
        this.javaClass.module
    }

    private val implAddOpens by lazy {
        try {
            Module::class.java.getDeclaredMethod(
                "implAddOpens", String::class.java, Module::class.java
            ).accessible()
        } catch (e: Throwable) {
            null
        }
    }

    fun assignAccessibility(obj: AccessibleObject) {
        try {
            val handle = isAccessibleFieldHandle ?: return
            handle.set(obj, true)
        } catch (e: Throwable) {
            // ignore
        }
    }

    fun assignAccessibility(module: Module, packages: List<String>) {
        try {
            packages.forEach {
                implAddOpens?.invoke(module, it, ownerModule)
            }
        } catch (e: Throwable) {
            // ignore
        }
    }

    private class Parent {
        var first = false

        @Volatile
        var second: Any? = null
    }
}

internal fun <T : AccessibleObject> T.accessible(): T {
    return apply {
        UnsafeAccessing.assignAccessibility(this)
    }
}
