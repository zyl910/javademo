package org.e3.studyjni;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/** Simple example of JNA interface mapping and usage. */
public class TestCPrintf {

    // This is the standard, stable way of mapping, which supports extensive
    // customization and mapping of Java to native types.

    public interface CLibrary extends Library {
        // 它们是C语言函数库中的函数，其定义格式如下:
        // int toupper(int ch)
        // double pow( double x, double y )
        // int printf(const char* format, ...)
        int toupper(int ch);
        double pow(double x,double y);
        void printf(String format, Object... args);
    }

    public static void main(String[] args) {
        String libname = Platform.isWindows() ? "msvcrt" : "c";
        CLibrary INSTANCE = (CLibrary)Native.load(libname, CLibrary.class);
        INSTANCE.printf("Hello, World\n");
        System.out.println("pow(2d,3d)=="+INSTANCE.pow(2d, 3d));
        System.out.println("toupper('a')=="+(char)INSTANCE.toupper((int)'a'));
        for (int i=0;i < args.length;i++) {
            INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }
    }

}
