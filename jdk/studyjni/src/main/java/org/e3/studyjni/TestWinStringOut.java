package org.e3.studyjni;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.nio.charset.Charset;

/** Test string out on Windows api (测试Windows Api中的字符串输出参数)
 *
 * <p>Kernel32.dll:</p>
 * <ul>
 *     <li>GetComputerNameA function: https://docs.microsoft.com/zh-cn/windows/win32/api/winbase/nf-winbase-getcomputernamea
 *     <br/><c>BOOL GetComputerNameA(LPSTR lpBuffer, LPDWORD nSize);</c>
 *     </li>
 * </ul>
 */
public class TestWinStringOut {
    /** Windows library - Kernel32.dll
     */
    public interface Kernel32Library extends StdCallLibrary
    {
        boolean GetComputerNameA (Pointer lpBuffer, IntByReference nSize);
        boolean GetComputerNameW (Pointer lpBuffer, IntByReference nSize);
    }

    public static void main(String[] args) {
        System.out.println("TestWinStringOut");
        // 修正Windows字符串乱码的办法:`VM options` add `-Dfile.encoding=GBK`.
        // init
        Kernel32Library lib = (Kernel32Library) Native.load("Kernel32", Kernel32Library.class);
        // param out: narrow string (窄字符串).
        if (true) {
            IntByReference nSize = new IntByReference();
            String str = null;
            boolean b = lib.GetComputerNameA(Pointer.NULL, nSize);
            int cch = nSize.getValue();
            if (cch>0) {    // lpBuffer传NULL时, 函数恒返回 false, 但 nSize 会返回字符串长度(严格说是TCHAR长度), 该值此是包含`\0`结束符的长度的.
                nSize.setValue(cch+1);    // 设置足够长度. 为了保险, 长度再+1.
                int memorySize = nSize.getValue();
                try(CloseableMemory memory = new CloseableMemory(memorySize)) { // 分配内存缓冲区.
                    memory.clear(); // 清零.
                    b = lib.GetComputerNameA(memory, nSize);    // 实际调用.
                    if (b) {
                        str = memory.getString(0);  // 获取字符串.
                    }
                }
            }
            System.out.println(String.format("GetComputerNameA: %s, %d, %s\n", Boolean.toString(b), nSize.getValue(), str));
        }
        // param in: wide string (宽字符串).
        if (true) {
            IntByReference nSize = new IntByReference();
            String str = null;
            boolean b = lib.GetComputerNameW(Pointer.NULL, nSize);
            int cch = nSize.getValue();
            if (cch>0) {    // lpBuffer传NULL时, 函数恒返回 false, 但 nSize 会返回字符串长度(严格说是TCHAR长度), 该值此是包含`\0`结束符的长度的.
                nSize.setValue(cch+1);    // 设置足够长度. 为了保险, 长度再+1.
                int memorySize = nSize.getValue() * Native.WCHAR_SIZE;
                try(CloseableMemory memory = new CloseableMemory(memorySize)) { // 分配内存缓冲区.
                    memory.clear(); // 清零.
                    b = lib.GetComputerNameW(memory, nSize);    // 实际调用.
                    if (b) {
                        str = memory.getWideString(0);  // 获取字符串.
                    }
                }
            }
            System.out.println(String.format("GetComputerNameW: %s, %d, %s\n", Boolean.toString(b), nSize.getValue(), str));
        }
        // System.getProperties
        System.out.println("Charset.defaultCharset:\t " + Charset.defaultCharset().name());
        System.out.println("Native.getDefaultStringEncoding:\t " + Native.getDefaultStringEncoding());
        System.out.println("System.getProperties:");
        System.getProperties().list(System.out);
    }
}
