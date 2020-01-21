package org.e3.studyjni;

import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;

import java.nio.charset.Charset;

/** Test string in on Windows api (测试Windows Api中的字符串输入参数)
 *
 * <p>User32.dll:</p>
 * <ul>
 *     <li>MessageBox function: https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-messagebox
 *     <br/><c>int MessageBox(HWND hWnd, LPCTSTR lpText, LPCTSTR lpCaption, UINT uType);</c>
 *     </li>
 * </ul>
 *
 * <p>Kernel32.dll:</p>
 * <ul>
 *     <li>GetComputerNameA function: https://docs.microsoft.com/zh-cn/windows/win32/api/winbase/nf-winbase-getcomputernamea
 *     <br/><c>BOOL GetComputerNameA(LPSTR lpBuffer, LPDWORD nSize);</c>
 *     </li>
 * </ul>
 */
public class TestWinStringIn {
    /** Windows library - User32.dll
     */
    public interface User32Library extends StdCallLibrary
    {
        int MessageBoxA(NativeLong hWnd, Pointer lpText, String lpCaption, int uType);  // Point.
        int MessageBoxA(NativeLong hWnd, String lpText, String lpCaption, int uType);  // String (narrow string).
        int MessageBoxW(NativeLong hWnd, WString lpText, WString lpCaption, int uType);
    }

    public static void main(String[] args) {
        System.out.println("TestWinStringIn");
        // init
        User32Library lib = (User32Library) Native.load("user32", User32Library.class);
        NativeLong hWnd = new NativeLong(0);    // 0表示没有父窗口.
        int uType = 0x40;  // 0x40: MB_OK | MB_ICONINFORMATION | MB_DEFBUTTON1 | MB_APPLMODAL
        final String caption = "TestWinStringIn";
        final String textA = "param in: narrow string (窄字符串)";
        final String textW = "param in: wide string (宽字符串)";
        // param in: narrow string (窄字符串).
        // 修正Windows窄字符串乱码的办法:`VM options` add `-Dfile.encoding=GBK`.
        int n = lib.MessageBoxA(hWnd, textA, caption, uType);
        System.out.println(String.format("MessageBoxA: %d\n", n));
        // param in: wide string (宽字符串).
        n = lib.MessageBoxW(hWnd, new WString(textW), new WString(caption), uType);
        System.out.println(String.format("MessageBoxW: %d\n", n));
        // narrow string by Memory.
        byte[] textABytes = textA.getBytes();   // 按操作系统的文本编码转为字节数组.
        Memory memory = new Memory(textABytes.length+1);    // +1是为了留出最后的 `\0` 结束符.
        memory.write(0L, textABytes, 0, textABytes.length);
        memory.setByte((long)textABytes.length, (byte)0);   // `\0` 结束符.
        n = lib.MessageBoxA(hWnd, memory, caption, uType);
        System.out.println(String.format("MessageBoxA by Memory: %d\n", n));
        // System.getProperties
        System.out.println("Charset.defaultCharset:\t " + Charset.defaultCharset().name());
        System.out.println("Native.getDefaultStringEncoding:\t " + Native.getDefaultStringEncoding());
        System.out.println("System.getProperties:");
        System.getProperties().list(System.out);
    }
}
